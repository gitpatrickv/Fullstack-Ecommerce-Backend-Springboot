package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.entity.constants.Role;
import com.practice.fullstackbackendspringboot.model.UserModel;
import com.practice.fullstackbackendspringboot.model.request.LoginRequest;
import com.practice.fullstackbackendspringboot.model.response.*;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.security.JwtAuthenticationFilter;
import com.practice.fullstackbackendspringboot.security.JwtService;
import com.practice.fullstackbackendspringboot.service.UserService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.UserMapper;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse register(UserModel userModel) {

        boolean isEmailExists = userRepository.existsByEmailIgnoreCase(userModel.getEmail());

        if (isEmailExists) {
            throw new EntityExistsException(StringUtil.ACCOUNT_EXISTS);
        }

        User user = mapper.mapUserModelToUserEntity(userModel);
        userRepository.save(user);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userModel.getEmail(), userModel.getPassword()));

            return LoginResponse.builder()
                    .jwtToken(jwtService.generateToken(authentication))
                    .role(authentication.getAuthorities().iterator().next().getAuthority())
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(StringUtil.INVALID_CREDENTIALS);
        }

    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            return LoginResponse.builder()
                    .jwtToken(jwtService.generateToken(authentication))
                    .role(authentication.getAuthorities().iterator().next().getAuthority())
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(StringUtil.INVALID_CREDENTIALS);
        }

    }
    @Override
    public String getUserFromToken(String email){
        email = JwtAuthenticationFilter.CURRENT_USER;
        userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND));
        return email;
    }

    @Override
    public UserModel getUser(String email) {
        User user = userRepository.findByEmail(email).get();
        return mapper.mapUserEntityToUserModel(user);
    }

    @Override
    public UserCount getUserCount(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        double count = userRepository.count();
        UserCount userCount = new UserCount();
        userCount.setUserCount(count);
        return userCount;
    }

    @Override
    public PaginateUserResponse getAllUsers(String email,  int pageNo, int pageSize, String sortBy) {

        Sort sorts = Sort.by(StringUtil.Role).ascending();

        if (StringUtil.Admin.equals(sortBy)) {
            sorts = Sort.by(StringUtil.Role).descending();
        } else if (StringUtil.False.equals(sortBy)){
            sorts = Sort.by(StringUtil.Frozen).ascending();
        } else if (StringUtil.True.equals(sortBy)) {
            sorts = Sort.by(StringUtil.Frozen).descending();
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, sorts);
        Page<User> users = userRepository.findAll(pageable);
        List<UserModel> userModels = new ArrayList<>();

        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(users.getNumber());
        pageResponse.setPageSize(users.getSize());
        pageResponse.setTotalElements(users.getTotalElements());
        pageResponse.setTotalPages(users.getTotalPages());
        pageResponse.setLast(users.isLast());

        for(User user : users ) {
            UserModel userModel = mapper.mapUserEntityToUserModel(user);
            userModels.add(userModel);
        }
        return new PaginateUserResponse(userModels,pageResponse);
    }

    @Override
    public void freezeAccount(String admin, String email) {
        User administrator = userRepository.findByEmail(admin).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));

        if(!administrator.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException(StringUtil.ACCESS_DENIED);
        }

        user.setFrozen(!user.isFrozen());
        userRepository.save(user);
    }
}


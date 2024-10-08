package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Store;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.entity.constants.Role;
import com.practice.fullstackbackendspringboot.model.UserModel;
import com.practice.fullstackbackendspringboot.model.request.LoginRequest;
import com.practice.fullstackbackendspringboot.model.response.LoginResponse;
import com.practice.fullstackbackendspringboot.model.response.PageResponse;
import com.practice.fullstackbackendspringboot.model.response.PaginateUserResponse;
import com.practice.fullstackbackendspringboot.model.response.UserCount;
import com.practice.fullstackbackendspringboot.repository.StoreRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.security.JwtService;
import com.practice.fullstackbackendspringboot.service.StoreService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final StoreService storeService;
    private final StoreRepository storeRepository;

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
                    .authUser(authentication.getName())
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
                    .authUser(authentication.getName())
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException(StringUtil.INVALID_CREDENTIALS);
        }

    }

    @Override
    public UserModel getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        return mapper.mapUserEntityToUserModel(user);
    }

    @Override
    public UserCount getUserCount() {           //ADMIN
        double count = userRepository.count();
        UserCount userCount = new UserCount();
        userCount.setUserCount(count);
        return userCount;
    }

    @Override
    public PaginateUserResponse getAllUsers(int pageNo, int pageSize, String sortBy) {      //ADMIN

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
    public void freezeAccount(String admin, String email) {     //ADMIN
        User administrator = userRepository.findByEmail(admin).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + admin));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));

        if(!administrator.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException(StringUtil.ACCESS_DENIED);
        }

        user.setFrozen(!user.isFrozen());
        userRepository.save(user);

        if(user.getRole().equals(Role.SELLER)){

            Optional<Store> store = storeRepository.findByUserEmail(user.getEmail());

            if(store.isPresent()) {
                storeService.suspendStoreAndProductListing(store.get().getStoreId(), admin);
            }
        }

    }

    @Override
    public String getAuthenticatedUser(){
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}


package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.LoginRequest;
import com.practice.fullstackbackendspringboot.model.LoginResponse;
import com.practice.fullstackbackendspringboot.model.UserModel;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.security.JwtService;
import com.practice.fullstackbackendspringboot.service.UserService;
import com.practice.fullstackbackendspringboot.utils.StringUtils;
import com.practice.fullstackbackendspringboot.utils.mapper.UserMapper;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public UserModel register(UserModel userModel) {

        boolean isEmailExists = userRepository.existsByEmailIgnoreCase(userModel.getEmail());

        if(isEmailExists){
            throw new EntityExistsException(StringUtils.ACCOUNT_EXISTS);
        }

        User user = mapper.mapUserModelToUserEntity(userModel);
        User saveUser = userRepository.save(user);
        return mapper.mapUserEntityToUserModel(saveUser);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            return LoginResponse.builder()
                    .jwtToken(jwtService.generateToken(authentication))
                    .role(authentication.getAuthorities().toString())
                    .build();
        }catch (AuthenticationException e){
            throw new BadCredentialsException(StringUtils.INVALID_CREDENTIALS);
        }

    }
}

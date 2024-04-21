package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.LoginRequest;
import com.practice.fullstackbackendspringboot.model.LoginResponse;
import com.practice.fullstackbackendspringboot.model.UserModel;

public interface UserService {
    UserModel register(UserModel userModel);
    LoginResponse login(LoginRequest loginRequest);
}

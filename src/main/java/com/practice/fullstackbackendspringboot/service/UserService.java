package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.request.LoginRequest;
import com.practice.fullstackbackendspringboot.model.response.LoginResponse;
import com.practice.fullstackbackendspringboot.model.UserModel;

public interface UserService {
    UserModel register(UserModel userModel);
    LoginResponse login(LoginRequest loginRequest);
    String getUserFromToken(String email);
    UserModel getUser(String email);

}

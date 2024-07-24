package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.request.LoginRequest;
import com.practice.fullstackbackendspringboot.model.response.LoginResponse;
import com.practice.fullstackbackendspringboot.model.UserModel;
import com.practice.fullstackbackendspringboot.model.response.UserCount;

import java.util.List;

public interface UserService {
    LoginResponse register(UserModel userModel);
    LoginResponse login(LoginRequest loginRequest);
    String getUserFromToken(String email);
    UserModel getUser(String email);
    UserCount getUserCount(String email);
    List<UserModel> getAllUsers(String email, String sortBy);

}

package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.UserModel;
import com.practice.fullstackbackendspringboot.model.request.LoginRequest;
import com.practice.fullstackbackendspringboot.model.response.LoginResponse;
import com.practice.fullstackbackendspringboot.model.response.PaginateUserResponse;
import com.practice.fullstackbackendspringboot.model.response.UserCount;

public interface UserService {
    LoginResponse register(UserModel userModel);
    LoginResponse login(LoginRequest loginRequest);
    String getAuthenticatedUser();
    UserModel getUser(String email);
    UserCount getUserCount();
    PaginateUserResponse getAllUsers(int pageNo, int pageSize, String sortBy);
    void freezeAccount(String admin, String email);

}

package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.request.ChangePasswordRequest;
import com.practice.fullstackbackendspringboot.model.request.UpdateUserRequest;

import java.security.Principal;

public interface AccountService {

    UpdateUserRequest updateAccountInfo(String email,UpdateUserRequest updateUserRequest);
    void changePassword(String email, ChangePasswordRequest request, Principal user);
}

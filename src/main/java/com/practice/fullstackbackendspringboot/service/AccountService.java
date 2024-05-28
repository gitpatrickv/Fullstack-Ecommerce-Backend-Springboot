package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.request.UpdateUserRequest;

public interface AccountService {

    UpdateUserRequest updateAccountInfo(String email,UpdateUserRequest updateUserRequest);
}

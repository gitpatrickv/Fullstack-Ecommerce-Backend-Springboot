package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.request.ChangePasswordRequest;
import com.practice.fullstackbackendspringboot.model.request.UpdateUserRequest;
import com.practice.fullstackbackendspringboot.service.AccountService;
import com.practice.fullstackbackendspringboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final AccountService accountService;

    @PutMapping("/account/profile")
    @ResponseStatus(HttpStatus.OK)
    public UpdateUserRequest updateAccountInfo(@RequestBody UpdateUserRequest updateUserRequest){
        String user = userService.getAuthenticatedUser();
        return accountService.updateAccountInfo(user,updateUserRequest);
    }
    @PutMapping("/account/password")
    @ResponseStatus(HttpStatus.OK)
    public void changePassword(@RequestBody @Valid ChangePasswordRequest request, Principal user){
        String currentUser = userService.getAuthenticatedUser();
    accountService.changePassword(currentUser,request,user);
    }

}

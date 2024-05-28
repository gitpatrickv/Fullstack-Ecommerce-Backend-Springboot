package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.request.UpdateUserRequest;
import com.practice.fullstackbackendspringboot.service.AccountService;
import com.practice.fullstackbackendspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;
    private final AccountService accountService;

    @PutMapping("/account")
    @ResponseStatus(HttpStatus.OK)
    public UpdateUserRequest updateAccountInfo(@RequestHeader("Authorization") String email, @RequestBody UpdateUserRequest updateUserRequest){
        String user = userService.getUserFromToken(email);
        return accountService.updateAccountInfo(user,updateUserRequest);
    }

}

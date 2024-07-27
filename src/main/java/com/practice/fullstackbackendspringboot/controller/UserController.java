package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.UserModel;
import com.practice.fullstackbackendspringboot.model.request.LoginRequest;
import com.practice.fullstackbackendspringboot.model.response.LoginResponse;
import com.practice.fullstackbackendspringboot.model.response.PaginateUserResponse;
import com.practice.fullstackbackendspringboot.model.response.UserCount;
import com.practice.fullstackbackendspringboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class  UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public LoginResponse register(@RequestBody @Valid UserModel userModel){
        return userService.register(userModel);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse login(@RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UserModel getUser(@RequestHeader("Authorization") String email) {
        String user = userService.getUserFromToken(email);
        return userService.getUser(user);
    }
    @GetMapping("/count")
    public UserCount getUserCount(@RequestHeader("Authorization") String email) {
        String user = userService.getUserFromToken(email);
        return userService.getUserCount(user);
    }
    @GetMapping("/all")
    public PaginateUserResponse getAllUsers(@RequestHeader("Authorization") String email,
                                            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                            @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
                                            @RequestParam(defaultValue = "user", required = false) String sortBy) {
        String user = userService.getUserFromToken(email);
        return userService.getAllUsers(user,pageNo,pageSize,sortBy);
    }

    @PutMapping("/freeze/{email}")
    public void freezeAccount(@RequestHeader("Authorization") String admin, @PathVariable String email){
            String user = userService.getUserFromToken(admin);
            userService.freezeAccount(user, email);
    }
}

package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.StoreModel;
import com.practice.fullstackbackendspringboot.service.StoreService;
import com.practice.fullstackbackendspringboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/store")
@RequiredArgsConstructor
public class StoreController {

    private final UserService userService;
    private final StoreService storeService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StoreModel createStore(@RequestBody @Valid StoreModel storeModel, @RequestHeader("Authorization") String email){
        String user = userService.getUserFromToken(email);
        return storeService.createStore(storeModel,user);
    }
}

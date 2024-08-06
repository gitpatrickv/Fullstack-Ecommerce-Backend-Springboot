package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.response.FollowedStore;
import com.practice.fullstackbackendspringboot.service.StoreFollowerService;
import com.practice.fullstackbackendspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreFollowerController {

    private final StoreFollowerService storeFollowerService;
    private final UserService userService;
    @PutMapping("/follow/{storeId}")
    public void followStore(@PathVariable String storeId){
        String user = userService.getAuthenticatedUser();
        storeFollowerService.followStore(storeId,user);
    }
    @GetMapping("/follow/{storeId}")
    public FollowedStore getFollowedStatus(@PathVariable String storeId){
        String user = userService.getAuthenticatedUser();
        return storeFollowerService.getFollowedStatus(storeId,user);
    }
}

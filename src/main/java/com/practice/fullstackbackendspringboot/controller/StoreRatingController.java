package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.request.RateStoreRequest;
import com.practice.fullstackbackendspringboot.model.response.TotalStoreRating;
import com.practice.fullstackbackendspringboot.service.StoreRatingService;
import com.practice.fullstackbackendspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class StoreRatingController {

    private final StoreRatingService storeRatingService;
    private final UserService userService;
    @PostMapping("/rate/store")
    public void rateStore(@RequestHeader("Authorization") String email, @RequestBody RateStoreRequest request) {
        String user = userService.getUserFromToken(email);
        storeRatingService.rateStore(user,request);
    }
    @GetMapping("/rate/store/count/{storeId}")
    public TotalStoreRating getStoreRatingCount(@PathVariable String storeId){
        return storeRatingService.getStoreRatingCount(storeId);
    }
}

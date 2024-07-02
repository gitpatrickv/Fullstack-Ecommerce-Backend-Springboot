package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.request.RateProductRequest;
import com.practice.fullstackbackendspringboot.service.RatingService;
import com.practice.fullstackbackendspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final UserService userService;
    @PostMapping("/product/rate")
    @ResponseStatus(HttpStatus.OK)
    public void rateProduct(@RequestHeader("Authorization") String email, @RequestBody RateProductRequest request){
        String user = userService.getUserFromToken(email);
        ratingService.rateProduct(user,request);
    }
}

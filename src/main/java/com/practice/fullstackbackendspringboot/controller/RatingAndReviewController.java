package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.request.RatingAverageRequest;
import com.practice.fullstackbackendspringboot.model.request.RateProductRequest;
import com.practice.fullstackbackendspringboot.service.RatingAndReviewService;
import com.practice.fullstackbackendspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RatingAndReviewController {

    private final RatingAndReviewService ratingAndReviewService;
    private final UserService userService;
    @PostMapping("/product/review")   //TODO: not yet implemented in the frontend
    @ResponseStatus(HttpStatus.OK)
    public void rateAndReviewProduct(@RequestHeader("Authorization") String email, @RequestBody RateProductRequest request){
        String user = userService.getUserFromToken(email);
        ratingAndReviewService.rateAndReviewProduct(user,request);
    }
    @GetMapping("/rating/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public RatingAverageRequest getProductRatingAverage(@PathVariable String productId){
        return ratingAndReviewService.getProductRatingAverage(productId);
    }
}

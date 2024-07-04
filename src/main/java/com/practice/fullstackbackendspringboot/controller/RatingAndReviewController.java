package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.RatingAndReviewModel;
import com.practice.fullstackbackendspringboot.model.request.RateProductRequest;
import com.practice.fullstackbackendspringboot.model.response.NumberOfUserRatingResponse;
import com.practice.fullstackbackendspringboot.model.response.RatingAverageResponse;
import com.practice.fullstackbackendspringboot.service.RatingAndReviewService;
import com.practice.fullstackbackendspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public RatingAverageResponse getProductRatingAverage(@PathVariable String productId){
        return ratingAndReviewService.getProductRatingAverage(productId);
    }
    @GetMapping("/product/review/get/all/{productId}")
    public List<RatingAndReviewModel> getAllRatingAndReview(@PathVariable String productId){
        Double rating = 0.0;
        String status = "";
        return ratingAndReviewService.getAllRatingAndReview(productId, rating, status);
    }

    @GetMapping("/product/review/get/5/{productId}")
    public List<RatingAndReviewModel> get5StarRating(@PathVariable String productId){
        Double rating = 5.0;
        String status = "5";
        return ratingAndReviewService.getAllRatingAndReview(productId, rating, status);
    }

    @GetMapping("/product/review/get/4/{productId}")
    public List<RatingAndReviewModel> get4StarRating(@PathVariable String productId){
        Double rating = 4.0;
        String status = "4";
        return ratingAndReviewService.getAllRatingAndReview(productId, rating, status);
    }

    @GetMapping("/product/review/get/3/{productId}")
    public List<RatingAndReviewModel> get3StarRating(@PathVariable String productId){
        Double rating = 3.0;
        String status = "3";
        return ratingAndReviewService.getAllRatingAndReview(productId, rating, status);
    }

    @GetMapping("/product/review/get/2/{productId}")
    public List<RatingAndReviewModel> get2StarRating(@PathVariable String productId){
        Double rating = 2.0;
        String status = "2";
        return ratingAndReviewService.getAllRatingAndReview(productId, rating, status);
    }

    @GetMapping("/product/review/get/1/{productId}")
    public List<RatingAndReviewModel> get1StarRating(@PathVariable String productId){
        Double rating = 1.0;
        String status = "1";
        return ratingAndReviewService.getAllRatingAndReview(productId, rating, status);
    }
    @GetMapping("/product/rating/get/{productId}")
    public NumberOfUserRatingResponse getTotalUserRating(@PathVariable String productId){
        return ratingAndReviewService.getTotalUserRating(productId);
    }
}

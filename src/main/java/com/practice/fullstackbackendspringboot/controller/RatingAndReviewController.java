package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.request.RateProductRequest;
import com.practice.fullstackbackendspringboot.model.request.ReplyToReviewRequest;
import com.practice.fullstackbackendspringboot.model.response.NumberOfUserRatingResponse;
import com.practice.fullstackbackendspringboot.model.response.RatingAndReviewResponse;
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
    @PostMapping("/product/review")
    @ResponseStatus(HttpStatus.OK)
    public void rateAndReviewProduct(@RequestBody RateProductRequest request){
        String user = userService.getAuthenticatedUser();
        ratingAndReviewService.rateAndReviewProduct(user,request);
    }

    @PostMapping("/product/review/reply")
    @ResponseStatus(HttpStatus.OK)
    public void replyToReview(@RequestBody ReplyToReviewRequest request){
        String user = userService.getAuthenticatedUser();
        ratingAndReviewService.replyToReview(user,request);
    }

    @GetMapping("/seller/customer/service/review/{storeId}")
    public RatingAndReviewResponse manageAllProductReview(@PathVariable String storeId,
                                                                @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                                @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
                                                                @RequestParam(defaultValue = "productName", required = false) String sortBy
    ){
        return ratingAndReviewService.manageAllProductReview(storeId, pageNo, pageSize, sortBy);
    }

    @GetMapping("/product/review/get/all/{productId}")
    public RatingAndReviewResponse getAllProductRatingAndReview(@PathVariable String productId,
                                                         @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                         @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        return ratingAndReviewService.getAllProductRatingAndReview(productId, pageNo, pageSize);
    }

    @GetMapping("/product/review/get/5/{productId}")
    public RatingAndReviewResponse get5StarRating(@PathVariable String productId,
                                                  @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        Double rating = 5.0;
        return ratingAndReviewService.getReviewByRatingValue(productId, pageNo, pageSize, rating);
    }

    @GetMapping("/product/review/get/4/{productId}")
    public RatingAndReviewResponse get4StarRating(@PathVariable String productId,
                                                  @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        Double rating = 4.0;
        return ratingAndReviewService.getReviewByRatingValue(productId, pageNo, pageSize, rating);
    }

    @GetMapping("/product/review/get/3/{productId}")
    public RatingAndReviewResponse get3StarRating(@PathVariable String productId,
                                                  @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        Double rating = 3.0;
        return ratingAndReviewService.getReviewByRatingValue(productId, pageNo, pageSize, rating);
    }

    @GetMapping("/product/review/get/2/{productId}")
    public RatingAndReviewResponse get2StarRating(@PathVariable String productId,
                                                  @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        Double rating = 2.0;
        return ratingAndReviewService.getReviewByRatingValue(productId, pageNo, pageSize, rating);
    }

    @GetMapping("/product/review/get/1/{productId}")
    public RatingAndReviewResponse get1StarRating(@PathVariable String productId,
                                                  @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                  @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize){
        Double rating = 1.0;
        return ratingAndReviewService.getReviewByRatingValue(productId, pageNo, pageSize, rating);
    }
    @GetMapping("/product/rating/get/{productId}")
    public NumberOfUserRatingResponse getTotalUserRating(@PathVariable String productId){
        return ratingAndReviewService.getTotalUserRating(productId);
    }
}

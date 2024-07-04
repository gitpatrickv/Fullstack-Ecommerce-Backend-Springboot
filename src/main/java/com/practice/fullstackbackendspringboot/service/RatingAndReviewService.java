package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.RatingAndReviewModel;
import com.practice.fullstackbackendspringboot.model.request.RateProductRequest;
import com.practice.fullstackbackendspringboot.model.response.NumberOfUserRatingResponse;
import com.practice.fullstackbackendspringboot.model.response.RatingAverageResponse;

import java.util.List;

public interface RatingAndReviewService {

    void rateAndReviewProduct(String email, RateProductRequest request);
    RatingAverageResponse getProductRatingAverage(String productId);
    List<RatingAndReviewModel> getAllRatingAndReview(String productId, Double rating, String status);
    NumberOfUserRatingResponse getTotalUserRating(String productId);

}

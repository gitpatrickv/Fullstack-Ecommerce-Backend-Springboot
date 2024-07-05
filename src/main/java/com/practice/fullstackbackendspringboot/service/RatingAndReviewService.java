package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.RatingAndReviewModel;
import com.practice.fullstackbackendspringboot.model.request.RateProductRequest;
import com.practice.fullstackbackendspringboot.model.response.NumberOfUserRatingResponse;
import com.practice.fullstackbackendspringboot.model.response.RatingAndReviewResponse;
import com.practice.fullstackbackendspringboot.model.response.RatingAverageResponse;

import java.util.List;

public interface RatingAndReviewService {

    void rateAndReviewProduct(String email, RateProductRequest request);
    RatingAverageResponse getProductRatingAverage(String productId);
    RatingAndReviewResponse getAllRatingAndReview(String productId, int pageNo, int pageSize, Double rating);
    NumberOfUserRatingResponse getTotalUserRating(String productId);
    RatingAndReviewResponse getAllRating(String productId, int pageNo, int pageSize);
}

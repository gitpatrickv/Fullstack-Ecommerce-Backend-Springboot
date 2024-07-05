package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.request.RateProductRequest;
import com.practice.fullstackbackendspringboot.model.response.NumberOfUserRatingResponse;
import com.practice.fullstackbackendspringboot.model.response.RatingAndReviewResponse;

public interface RatingAndReviewService {

    void rateAndReviewProduct(String email, RateProductRequest request);
    RatingAndReviewResponse getAllRatingAndReview(String productId, int pageNo, int pageSize, Double rating);
    NumberOfUserRatingResponse getTotalUserRating(String productId);
    RatingAndReviewResponse getAllRating(String productId, int pageNo, int pageSize);
}

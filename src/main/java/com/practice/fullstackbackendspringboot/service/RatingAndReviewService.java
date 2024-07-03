package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.request.RatingAverageRequest;
import com.practice.fullstackbackendspringboot.model.request.RateProductRequest;

public interface RatingAndReviewService {

    void rateAndReviewProduct(String email, RateProductRequest request);
    RatingAverageRequest getProductRatingAverage(String productId);
}

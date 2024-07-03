package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.RatingAndReviewModel;
import com.practice.fullstackbackendspringboot.model.request.RatingAverageRequest;
import com.practice.fullstackbackendspringboot.model.request.RateProductRequest;

import java.util.List;

public interface RatingAndReviewService {

    void rateAndReviewProduct(String email, RateProductRequest request);
    RatingAverageRequest getProductRatingAverage(String productId);
    List<RatingAndReviewModel> getAllRatingAndReview(String productId);
}

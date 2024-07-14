package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.request.RateProductRequest;
import com.practice.fullstackbackendspringboot.model.request.ReplyToReviewRequest;
import com.practice.fullstackbackendspringboot.model.response.NumberOfUserRatingResponse;
import com.practice.fullstackbackendspringboot.model.response.RatingAndReviewResponse;

public interface RatingAndReviewService {

    void rateAndReviewProduct(String email, RateProductRequest request);
    void replyToReview(String email, ReplyToReviewRequest request);
    RatingAndReviewResponse getReviewByRatingValue(String productId, int pageNo, int pageSize, Double rating);
    NumberOfUserRatingResponse getTotalUserRating(String productId);
    RatingAndReviewResponse getAllProductRatingAndReview(String productId, int pageNo, int pageSize);
    RatingAndReviewResponse manageAllProductReview(String email, String storeId, int pageNo, int pageSize);
}

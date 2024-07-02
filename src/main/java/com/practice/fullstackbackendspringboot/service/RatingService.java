package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.RatingModel;
import com.practice.fullstackbackendspringboot.model.request.RateProductRequest;

public interface RatingService {

    void rateProduct(String email, RateProductRequest request);
    RatingModel getProductRatingAverage(String productId);
}

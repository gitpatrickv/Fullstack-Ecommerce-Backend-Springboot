package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.request.RateStoreRequest;
import com.practice.fullstackbackendspringboot.model.response.TotalStoreRating;

public interface StoreRatingService {

    void rateStore(String email, RateStoreRequest request);
    TotalStoreRating getStoreRatingCount(String storeId);
    public void checkStatus(String orderId, String email);
}

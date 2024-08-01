package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.request.RateStoreRequest;

public interface StoreRatingService {

    void rateStore(String email, RateStoreRequest request);
    public void checkStatus(String orderId, String email);
}

package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.StoreModel;
import com.practice.fullstackbackendspringboot.model.request.CreateStoreRequest;

public interface StoreService {

    void createStore(CreateStoreRequest request, String email);
    StoreModel getStoreInfo(String email);


}

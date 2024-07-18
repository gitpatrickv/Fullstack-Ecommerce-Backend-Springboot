package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.StoreModel;
import com.practice.fullstackbackendspringboot.model.request.CreateStoreRequest;
import com.practice.fullstackbackendspringboot.model.request.UpdateShopInfoRequest;

public interface StoreService {

    void createStore(CreateStoreRequest request, String email);
    StoreModel getStoreInfo(String email);
    void updateShopInfo(String email, String storeId, UpdateShopInfoRequest request);
}

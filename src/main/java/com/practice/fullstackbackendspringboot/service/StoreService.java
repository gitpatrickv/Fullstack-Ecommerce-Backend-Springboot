package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.StoreModel;

public interface StoreService {

    StoreModel createStore(StoreModel storeModel, String email);
    StoreModel getStoreInfo(String email);


}

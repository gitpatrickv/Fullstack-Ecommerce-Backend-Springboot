package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.StoreModel;
import com.practice.fullstackbackendspringboot.model.request.CreateStoreRequest;
import com.practice.fullstackbackendspringboot.model.request.UpdateShopInfoRequest;
import com.practice.fullstackbackendspringboot.model.response.PaginateStoreResponse;
import com.practice.fullstackbackendspringboot.model.response.StoreCount;

public interface StoreService {

    void createStore(CreateStoreRequest request, String email);
    StoreModel getStoreInfo(String email);
    void updateShopInfo(String email, String storeId, UpdateShopInfoRequest request);
    PaginateStoreResponse getAllStores(String email,int pageNo, int pageSize, String sortBy);
    StoreCount getStoreCount(String email);
    void suspendStoreAndProductListing(String storeId, String email);
}

package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.StoreFollowerModel;
import com.practice.fullstackbackendspringboot.model.response.FollowedStore;
import com.practice.fullstackbackendspringboot.model.response.StoreFollowerCount;

import java.util.List;

public interface StoreFollowerService {

    void followStore(String storeId, String email);
    FollowedStore getFollowedStatus(String storeId, String email);
    StoreFollowerCount getStoreFollowerCount(String storeId);
    List<StoreFollowerModel> getAllFollowedStore(String email);

}

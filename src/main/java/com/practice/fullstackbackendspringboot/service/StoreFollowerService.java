package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.response.FollowedStore;

public interface StoreFollowerService {

    void followStore(String storeId, String email);
    FollowedStore getFollowedStatus(String storeId, String email);
}

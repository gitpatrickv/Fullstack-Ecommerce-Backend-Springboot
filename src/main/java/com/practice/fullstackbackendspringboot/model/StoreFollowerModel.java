package com.practice.fullstackbackendspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreFollowerModel {

    private Long storeFollowerId;
    private boolean followed;
    private String storeId;
    private String storeName;
    private String storePhotoUrl;
}

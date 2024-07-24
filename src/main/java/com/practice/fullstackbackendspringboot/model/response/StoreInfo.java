package com.practice.fullstackbackendspringboot.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreInfo {

    private String storeName;
    private String storePhotoUrl;
    private boolean online;
}

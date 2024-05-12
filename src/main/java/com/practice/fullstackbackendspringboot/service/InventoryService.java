package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.InventoryModel;

public interface InventoryService {

    InventoryModel getInventoryByProductId(String productId);
}

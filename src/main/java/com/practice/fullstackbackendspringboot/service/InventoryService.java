package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.request.AddStockRequest;

public interface InventoryService {

    void addInventoryStock(String email, AddStockRequest request);

}

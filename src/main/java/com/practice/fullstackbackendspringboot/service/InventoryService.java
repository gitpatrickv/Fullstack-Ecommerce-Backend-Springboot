package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.request.AddStockRequest;
import com.practice.fullstackbackendspringboot.model.request.UpdatePriceRequest;

public interface InventoryService {

    void addInventoryStock(String email, AddStockRequest request);
    void updatePrice(String email, UpdatePriceRequest request);

}

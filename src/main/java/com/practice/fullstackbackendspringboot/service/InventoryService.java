package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.InventoryModel;
import com.practice.fullstackbackendspringboot.model.request.AddStockRequest;
import com.practice.fullstackbackendspringboot.model.request.UpdatePriceRequest;

import java.util.List;

public interface InventoryService {

    void addInventoryStock(AddStockRequest request);
    void updatePrice(UpdatePriceRequest request);
    List<InventoryModel> getAllOutOfStock(String storeId);

}

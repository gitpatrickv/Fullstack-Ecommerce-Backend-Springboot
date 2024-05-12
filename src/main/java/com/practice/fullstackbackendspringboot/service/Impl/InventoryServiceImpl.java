package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Inventory;
import com.practice.fullstackbackendspringboot.model.InventoryModel;
import com.practice.fullstackbackendspringboot.repository.InventoryRepository;
import com.practice.fullstackbackendspringboot.repository.ProductRepository;
import com.practice.fullstackbackendspringboot.service.InventoryService;
import com.practice.fullstackbackendspringboot.utils.mapper.InventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final InventoryMapper mapper;

    @Override
    public InventoryModel getInventoryByProductId(String productId) {

        Inventory inventory = inventoryRepository.findByProduct_ProductId(productId).get();

        return mapper.mapInventoryEntityToInventoryModel(inventory);

    }
}

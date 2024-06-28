package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Inventory;
import com.practice.fullstackbackendspringboot.model.request.AddStockRequest;
import com.practice.fullstackbackendspringboot.repository.InventoryRepository;
import com.practice.fullstackbackendspringboot.service.InventoryService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    @Override
    public void addInventoryStock(String email, AddStockRequest request) {

        Optional<Inventory> inventory = inventoryRepository.findById(request.getInventoryId());

        if(inventory.isPresent()){
            Inventory inv = inventory.get();
            inv.setQuantity(request.getQuantity());
            inventoryRepository.save(inv);
        }else{
            throw new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND);
        }
    }
}

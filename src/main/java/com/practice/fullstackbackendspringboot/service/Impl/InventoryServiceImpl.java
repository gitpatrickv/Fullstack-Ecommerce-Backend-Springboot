package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Inventory;
import com.practice.fullstackbackendspringboot.entity.Product;
import com.practice.fullstackbackendspringboot.model.InventoryModel;
import com.practice.fullstackbackendspringboot.model.request.AddStockRequest;
import com.practice.fullstackbackendspringboot.model.request.UpdatePriceRequest;
import com.practice.fullstackbackendspringboot.repository.InventoryRepository;
import com.practice.fullstackbackendspringboot.repository.ProductRepository;
import com.practice.fullstackbackendspringboot.repository.StoreRepository;
import com.practice.fullstackbackendspringboot.service.InventoryService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.InventoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final InventoryMapper inventoryMapper;
    @Override
    public void addInventoryStock(AddStockRequest request) {

        Optional<Inventory> inventory = inventoryRepository.findById(request.getInventoryId());

        if(inventory.isPresent()){
            Inventory inv = inventory.get();
            inv.setQuantity(request.getQuantity());
            inventoryRepository.save(inv);
        }else{
            throw new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND);
        }
    }

    @Override
    public void updatePrice(UpdatePriceRequest request) {

        Optional<Inventory> inventory = inventoryRepository.findById(request.getInventoryId());

        if(inventory.isPresent()){
            Inventory inv = inventory.get();
            inv.setPrice(request.getPrice());
            inventoryRepository.save(inv);
        }else{
            throw new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND);
        }
    }

    @Override
    public List<InventoryModel> getAllOutOfStock(String storeId) {

        List<Product> products = productRepository.findAllByDeletedFalseAndListedTrueAndSuspendedFalseAndStore_StoreId(storeId);
        List<InventoryModel> inventoryModels = new ArrayList<>();

        for(Product product : products){
            Set<Inventory> inventories = inventoryRepository.findAllByProduct_ProductId(product.getProductId());

            for(Inventory inventory : inventories){
                if(inventory.getQuantity() == 0){
                    InventoryModel inventoryModel = inventoryMapper.mapInventoryEntityToInventoryModel(inventory);
                    inventoryModel.setProductName(product.getProductName());
                    inventoryModels.add(inventoryModel);
                }
            }
        }

        return inventoryModels;
    }
}

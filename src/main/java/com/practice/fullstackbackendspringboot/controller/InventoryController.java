package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.InventoryModel;
import com.practice.fullstackbackendspringboot.model.request.AddStockRequest;
import com.practice.fullstackbackendspringboot.model.request.UpdatePriceRequest;
import com.practice.fullstackbackendspringboot.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    @PutMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addInventoryStock(@RequestBody AddStockRequest request){
        inventoryService.addInventoryStock(request);
    }
    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void updatePrice(@RequestBody UpdatePriceRequest request){
        inventoryService.updatePrice(request);
    }
    @GetMapping("/stock/{storeId}")
    public List<InventoryModel> getAllOutOfStock(@PathVariable String storeId){
        return inventoryService.getAllOutOfStock(storeId);
    }
}

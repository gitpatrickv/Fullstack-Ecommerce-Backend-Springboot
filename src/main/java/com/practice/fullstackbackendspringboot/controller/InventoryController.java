package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.InventoryModel;
import com.practice.fullstackbackendspringboot.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public InventoryModel getInventoryByProductId(@PathVariable (value="productId") String productId){
        return inventoryService.getInventoryByProductId(productId);
    }
}

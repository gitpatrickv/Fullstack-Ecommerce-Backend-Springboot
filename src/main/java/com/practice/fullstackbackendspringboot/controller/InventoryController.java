package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.request.AddStockRequest;
import com.practice.fullstackbackendspringboot.service.InventoryService;
import com.practice.fullstackbackendspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;
    private final UserService userService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public void addInventoryStock(@RequestHeader("Authorization") String email, @RequestBody AddStockRequest request){
        String user = userService.getUserFromToken(email);
        inventoryService.addInventoryStock(user, request);
    }
}

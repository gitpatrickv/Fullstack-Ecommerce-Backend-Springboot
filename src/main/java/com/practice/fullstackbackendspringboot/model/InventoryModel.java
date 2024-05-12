package com.practice.fullstackbackendspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryModel {

    private Long inventoryId;
    private Long quantity;
    private Double price;
    private String skuCode;
    private String productId;
}

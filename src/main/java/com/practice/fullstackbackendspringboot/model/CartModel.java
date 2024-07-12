package com.practice.fullstackbackendspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartModel {
    private String cartId;
    private Long quantity;
    private Double price;
    private Double totalAmount;
    private String storeName;
    private String productName;
    private boolean filter;
    private String photoUrl;
    private String productId;
    private String colors;
    private String sizes;
    private Long inventoryId;
    private Long stockRemaining;
    private String storeId;
}

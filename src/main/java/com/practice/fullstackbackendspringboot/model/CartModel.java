package com.practice.fullstackbackendspringboot.model;

import com.practice.fullstackbackendspringboot.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartModel {
    private Long cartId;
    private Long quantity;
    private Double price;
    private Double totalAmount;
    private String shopName;
    private String productName;
    private boolean filter;
    private String photoUrl;
    private String productId;
}

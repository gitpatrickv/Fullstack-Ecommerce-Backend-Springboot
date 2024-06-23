package com.practice.fullstackbackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.practice.fullstackbackendspringboot.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(NON_DEFAULT)
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
}

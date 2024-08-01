package com.practice.fullstackbackendspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemModel {

    private Long id;

    private Long quantity;
    private Double price;
    private Double totalAmount;
    private String storeName;
    private String productName;
    private String photoUrl;
    private String orderId;
    private boolean active;
    private String orderStatus;
    private String orderStatusInfo;
    private Double orderTotalAmount;
    private String storeId;
    private String fullName;
    private String colors;
    private String sizes;
    private String productId;
    private boolean rated;
    private boolean storeRated;
}

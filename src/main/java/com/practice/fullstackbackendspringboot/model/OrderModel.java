package com.practice.fullstackbackendspringboot.model;

import com.practice.fullstackbackendspringboot.entity.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {

    private String orderId;

    private Double orderTotalAmount;

    private String paymentMethod;

    private String orderStatus;
    private String deliveryAddress;
    private String fullName;
    private String contactNumber;
    private List<OrderItem> orderItems;

}

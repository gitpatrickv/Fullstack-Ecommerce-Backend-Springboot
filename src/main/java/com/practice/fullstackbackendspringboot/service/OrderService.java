package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.OrderItemModel;
import com.practice.fullstackbackendspringboot.model.OrderModel;

import java.util.List;

public interface OrderService {

    void placeOrder(String email);
    void cancelOrder(String email, String orderId);
    void buyAgain(String email, String orderId);
    void shipOrder(String email, String orderId);
    List<OrderItemModel> getCustomerOrdersByStatus(String email, String status1, String status2);
    List<OrderItemModel> getStoreOrdersByStatus(String email, String storeId, String status1);
}

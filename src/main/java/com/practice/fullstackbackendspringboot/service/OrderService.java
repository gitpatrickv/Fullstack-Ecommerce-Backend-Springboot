package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.OrderItemModel;
import com.practice.fullstackbackendspringboot.model.OrderModel;

import java.util.List;

public interface OrderService {

    void placeOrder(String email);
    void cancelOrder(String email, String orderId);
    List<OrderItemModel> getOrdersByToPayStatus(String email);
    List<OrderItemModel> getOrdersByCancelledStatus(String email);
}

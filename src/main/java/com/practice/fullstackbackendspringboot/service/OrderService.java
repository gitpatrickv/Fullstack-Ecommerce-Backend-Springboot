package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.OrderModel;

import java.util.List;

public interface OrderService {

    void placeOrder(String email);

    List<OrderModel> getOrdersByToPayStatus(String email);
    List<OrderModel> getOrdersByToShipStatus(String email);
}

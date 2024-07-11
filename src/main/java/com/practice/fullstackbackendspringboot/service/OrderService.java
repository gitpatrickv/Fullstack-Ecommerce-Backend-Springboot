package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.OrderItemModel;
import com.practice.fullstackbackendspringboot.model.response.AllOrdersResponse;

import java.util.List;
import java.util.Set;

public interface OrderService {

    void placeOrder(String email);
    void cancelOrder(String email, String orderId);
    void buyAgain(String email, String orderId);
    void processOrder(String email, String orderId);
    List<OrderItemModel> getCustomerOrdersByStatus(String email, String status1);
    AllOrdersResponse getStoreOrdersByStatus(String email, String storeId, String status1);
    Set<OrderItemModel> getCustomerOrdersByOrderIdToRate(String email, String orderId);
}

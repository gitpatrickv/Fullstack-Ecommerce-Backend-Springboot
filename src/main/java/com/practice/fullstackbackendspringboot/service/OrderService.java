package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.OrderItemModel;
import com.practice.fullstackbackendspringboot.model.response.*;
import com.stripe.exception.StripeException;

import java.util.List;
import java.util.Set;

public interface OrderService {

    PaymentResponse placeOrder(String email, String paymentMethod) throws StripeException;
    void cancelOrder(String orderId);
    void confirmCancelOrder(String orderId);
    void buyAgain(String email, String orderId);
    void processOrder(String orderId);
    List<OrderItemModel> getCustomerOrdersByStatus(String email, String status1);
    List<OrderItemModel> getCustomerOrdersByCompletedAndRatedStatus(String email);
    AllOrdersResponse getStoreOrdersByStatus(String storeId, String status1);
    AllOrdersResponse getStoreOrdersByCompletedAndRatedStatus(String storeId);
    Set<OrderItemModel> getCustomerOrdersByOrderIdToRate(String orderId);
    TodoListTotal getSellersTodoListTotal(String storeId);
    TotalSales getTotalSales(String storeId);
    OrderCount getOrderCountAndTotalSales();
    PaginateOrderResponse getAllOrders(int pageNo, int pageSize);
}

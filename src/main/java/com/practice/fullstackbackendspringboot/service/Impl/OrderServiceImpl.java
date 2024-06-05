package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Cart;
import com.practice.fullstackbackendspringboot.entity.Order;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.OrderModel;
import com.practice.fullstackbackendspringboot.repository.CartRepository;
import com.practice.fullstackbackendspringboot.repository.OrderRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.OrderService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;
    @Override
    public void placeOrder(String email) {
        User user = userRepository.findByEmail(email).get();
        List<Cart> cart = cartRepository.findAllByFilterTrueAndUserEmail(email);

        for(Cart carts : cart) {
            Order order = new Order();
            order.setQuantity(carts.getQuantity());
            order.setPrice(carts.getPrice());
            order.setTotalAmount(carts.getTotalAmount());
            order.setStoreName(carts.getStoreName());
            order.setProductName(carts.getProductName());
            order.setPhotoUrl(carts.getPhotoUrl());
            order.setPaymentMethod(StringUtil.CASH_ON_DELIVERY);
            order.setOrderStatus(StringUtil.TO_PAY);
            order.setUser(user);
            orderRepository.save(order);
        }

        cartRepository.deleteAllByFilterTrueAndUserEmail(email);
    }

    @Override
    public  List<OrderModel> getOrdersByToPayStatus(String email) {
        List<Order> order = orderRepository.findAllByUserEmail(email);
        List<OrderModel> orderModels = new ArrayList<>();

        for(Order orders : order){
            if(orders.getOrderStatus().equals(StringUtil.TO_PAY)) {
                OrderModel orderModel = orderMapper.mapOrderEntityToOrderModel(orders);
                this.getOrders(orders,orderModel);
                orderModels.add(orderModel);
            }
        }
        return orderModels;
    }

    @Override
    public  List<OrderModel> getOrdersByToShipStatus(String email) {
        List<Order> order = orderRepository.findAllByUserEmail(email);
        List<OrderModel> orderModels = new ArrayList<>();

        for(Order orders : order){
            if(orders.getOrderStatus().equals(StringUtil.TO_SHIP)) {
                OrderModel orderModel = orderMapper.mapOrderEntityToOrderModel(orders);
                this.getOrders(orders,orderModel);
                orderModels.add(orderModel);
            }
        }
        return orderModels;
    }

    private void getOrders(Order order, OrderModel orderModel){
        orderModel.setQuantity(order.getQuantity());
        orderModel.setPrice(order.getPrice());
        orderModel.setTotalAmount(order.getTotalAmount());
        orderModel.setStoreName(order.getStoreName());
        orderModel.setProductName(order.getProductName());
        orderModel.setPhotoUrl(order.getPhotoUrl());
        orderModel.setPaymentMethod(order.getPaymentMethod());
        orderModel.setOrderStatus(order.getOrderStatus());
    }
}

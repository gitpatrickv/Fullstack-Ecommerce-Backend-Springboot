package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.*;
import com.practice.fullstackbackendspringboot.model.OrderItemModel;
import com.practice.fullstackbackendspringboot.repository.*;
import com.practice.fullstackbackendspringboot.service.OrderService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.OrderItemMapper;
import com.practice.fullstackbackendspringboot.utils.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(rollbackOn = Exception.class)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    @Override
    public void placeOrder(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        List<Cart> cart = cartRepository.findAllByFilterTrueAndUserEmail(email);

        Map<String, List<Cart>> cartsByStore = cart.stream()
                .collect(Collectors.groupingBy(Cart::getStoreName));

        for (Map.Entry<String, List<Cart>> cartMap : cartsByStore.entrySet()) {
            List<Cart> storeCarts = cartMap.getValue();
            String storeName = cartMap.getKey();

            Optional<Store> store = storeRepository.findByStoreName(storeName);

            Order order = new Order();
            order.setStore(store.get());
            order.setDeliveryAddress(user.get().getAddress());
            order.setFullName(user.get().getName());
            order.setContactNumber(user.get().getContactNumber());
            order.setOrderStatus(StringUtil.TO_PAY);
            order.setPaymentMethod(StringUtil.CASH_ON_DELIVERY);

            Double storeTotalAmount = 0.0;

            List<OrderItem> orderItems = new ArrayList<>();

            for (Cart carts : storeCarts) {
                OrderItem orderItem = new OrderItem();

                orderItem.setQuantity(carts.getQuantity());
                orderItem.setTotalAmount(carts.getTotalAmount());
                orderItem.setPrice(carts.getPrice());
                orderItem.setStoreName(carts.getStoreName());
                orderItem.setProductName(carts.getProductName());
                orderItem.setPhotoUrl(carts.getPhotoUrl());
                orderItem.setUser(user.get());
                orderItem.setOrder(order);
                storeTotalAmount += orderItem.getTotalAmount();
                OrderItem savedOrderItems = orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItems);
            }

            order.setOrderTotalAmount(storeTotalAmount);
            order.setOrderItems(orderItems);
            Order savedOrder = orderRepository.save(order);

            store.get().getOrder().add(savedOrder);
        }
        cartRepository.deleteAllByFilterTrueAndUserEmail(email);
    }

    @Override       //TODO: Sort latest item will be place at the top
    public List<OrderItemModel> getOrdersByToPayStatus(String email) {
        List<OrderItem> orderItems = orderItemRepository.findAllByUserEmail(email);

        List<OrderItemModel> orderModels = new ArrayList<>();

        for(OrderItem orderItem : orderItems){
            Order order = orderRepository.findById(orderItem.getOrder().getOrderId()).get();

            if(order.getOrderStatus().equals(StringUtil.TO_PAY)) {
                OrderItemModel orderItemModel = orderItemMapper.mapEntityToModel(orderItem);
                orderItemModel.setOrderTotalAmount(order.getOrderTotalAmount());
                orderItemModel.setOrderStatus(order.getOrderStatus());
                orderModels.add(orderItemModel);
            }
        }
        return orderModels;
    }


//    @Override
//    public  List<OrderModel> getOrdersByToPayStatus(String email) {
//        List<Order> order = orderRepository.findAllByUserEmail(email);
//        List<OrderModel> orderModels = new ArrayList<>();
//
//        for(Order orders : order){
//            if(orders.getOrderStatus().equals(StringUtil.TO_PAY)) {
//                OrderModel orderModel = orderMapper.mapOrderEntityToOrderModel(orders);
//                orderModels.add(orderModel);
//            }
//        }
//        return orderModels;
//    }

//        private void getOrders(Order order, OrderModel orderModel){
//        orderModel.setQuantity(order.getQuantity());
//        orderModel.setPrice(order.getPrice());
//        orderModel.setTotalAmount(order.getTotalAmount());
//        orderModel.setStoreName(order.getStoreName());
//        orderModel.setProductName(order.getProductName());
//        orderModel.setPhotoUrl(order.getPhotoUrl());
//        orderModel.setPaymentMethod(order.getPaymentMethod());
//        orderModel.setOrderStatus(order.getOrderStatus());
//    }
}


//    @Override
//    public void placeOrder(String email) {
//        User user = userRepository.findByEmail(email).get();
//        List<Cart> cart = cartRepository.findAllByFilterTrueAndUserEmail(email);
//
//        for(Cart carts : cart) {
//            Order order = new Order();
//            order.setQuantity(carts.getQuantity());
//            order.setPrice(carts.getPrice());
//            order.setTotalAmount(carts.getTotalAmount());
//            order.setStoreName(carts.getStoreName());
//            order.setProductName(carts.getProductName());
//            order.setPhotoUrl(carts.getPhotoUrl());
//            order.setPaymentMethod(StringUtil.CASH_ON_DELIVERY);
//            order.setOrderStatus(StringUtil.TO_PAY);
//            order.setDeliveryAddress(user.getAddress());
//            order.setFullName(user.getName());
//            order.setContactNumber(user.getContactNumber());
//            order.setUser(user);
//            orderRepository.save(order);
//        }
//
//        cartRepository.deleteAllByFilterTrueAndUserEmail(email);
//    }
//

//
//    @Override
//    public  List<OrderModel> getOrdersByToShipStatus(String email) {
//        List<Order> order = orderRepository.findAllByUserEmail(email);
//        List<OrderModel> orderModels = new ArrayList<>();
//
//        for(Order orders : order){
//            if(orders.getOrderStatus().equals(StringUtil.TO_SHIP)) {
//                OrderModel orderModel = orderMapper.mapOrderEntityToOrderModel(orders);
//                this.getOrders(orders,orderModel);
//                orderModels.add(orderModel);
//            }
//        }
//        return orderModels;
//    }
//
//    private void getOrders(Order order, OrderModel orderModel){
//        orderModel.setQuantity(order.getQuantity());
//        orderModel.setPrice(order.getPrice());
//        orderModel.setTotalAmount(order.getTotalAmount());
//        orderModel.setStoreName(order.getStoreName());
//        orderModel.setProductName(order.getProductName());
//        orderModel.setPhotoUrl(order.getPhotoUrl());
//        orderModel.setPaymentMethod(order.getPaymentMethod());
//        orderModel.setOrderStatus(order.getOrderStatus());
//    }

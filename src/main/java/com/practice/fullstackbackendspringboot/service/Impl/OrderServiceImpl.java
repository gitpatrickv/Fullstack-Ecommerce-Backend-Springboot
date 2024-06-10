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
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
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
            order.setActive(true);
            order.setOrderStatus(StringUtil.TO_PAY);
            order.setPaymentMethod(StringUtil.CASH_ON_DELIVERY);
            order = orderRepository.save(order);
            Double storeTotalAmount = 0.0;

            List<OrderItem> orderItems = new ArrayList<>();

            for (Cart carts : storeCarts) {
                Product product = productRepository.findById(carts.getProduct().getProductId()).get();
                OrderItem orderItem = new OrderItem();

                orderItem.setQuantity(carts.getQuantity());
                orderItem.setTotalAmount(carts.getTotalAmount());
                orderItem.setPrice(carts.getPrice());
                orderItem.setStoreName(carts.getStoreName());
                orderItem.setProductName(carts.getProductName());
                orderItem.setPhotoUrl(carts.getPhotoUrl());
                orderItem.setUser(user.get());
                orderItem.setOrder(order);
                orderItem.setProduct(product);
                storeTotalAmount += orderItem.getTotalAmount();
                OrderItem savedOrderItems = orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItems);

                Optional<Inventory> inventory = inventoryRepository.findByProduct_ProductId(carts.getProduct().getProductId());
                if(inventory.isPresent()) {
                    if (carts.getQuantity() > inventory.get().getQuantity()) {
                        throw new IllegalArgumentException(StringUtil.OUT_OF_STOCK);
                    } else {
                        inventory.get().setQuantity(inventory.get().getQuantity() - carts.getQuantity());
                    }
                }else{
                    throw new IllegalArgumentException(StringUtil.PRODUCT_NOT_FOUND);
                }
            }

            order.setOrderTotalAmount(storeTotalAmount + store.get().getShippingFee());
            order.setOrderItems(orderItems);
            orderRepository.save(order);
        }
        cartRepository.deleteAllByFilterTrueAndUserEmail(email);
    }

    @Override
    public void cancelOrder(String email, String orderId) {
        Optional<User> user = userRepository.findByEmail(email);
        Optional<Order> order = orderRepository.findById(orderId);
        List<OrderItem> orderItems = orderItemRepository.findAllByUserEmailAndOrder_OrderId(email, orderId);

        if(user.isPresent()){
            if(order.isPresent()){
                Order orders = order.get();
                orders.setOrderStatus(StringUtil.ORDER_CANCELLED);
                orders.setActive(false);
                orderRepository.save(orders);
            }else{
                throw new IllegalArgumentException(StringUtil.ORDER_NOT_FOUND);
            }
        }

        for(OrderItem orderItem: orderItems){
            Optional<Inventory> inventory = inventoryRepository.findByProduct_ProductId(orderItem.getProduct().getProductId());
            if(inventory.isPresent()){
                inventory.get().setQuantity(inventory.get().getQuantity() + orderItem.getQuantity());
            }else{
                throw new IllegalArgumentException(StringUtil.PRODUCT_NOT_FOUND);
            }
        }
    }

    @Override
    public void buyAgain(String email, String orderId) {
        Optional<User> user = userRepository.findByEmail(email);
        List<OrderItem> orderItems = orderItemRepository.findAllByUserEmailAndOrder_OrderId(email,orderId);
        Long quantity = 1L;

        for(OrderItem orderItem : orderItems){
            Product product = productRepository.findById(orderItem.getProduct().getProductId()).get();
            Cart cart = new Cart();
            cart.setPhotoUrl(orderItem.getPhotoUrl());
            cart.setPrice(orderItem.getPrice());
            cart.setProductName(orderItem.getProductName());
            cart.setQuantity(quantity);
            cart.setStoreName(orderItem.getStoreName());
            cart.setTotalAmount(orderItem.getPrice() * quantity);
            cart.setProduct(product);
            cart.setOrderItems(orderItems);
            cart.setUser(user.get());
            cartRepository.save(cart);
        }
    }

    @Override
    public List<OrderItemModel> getOrdersByToPayStatus(String email) {
        List<OrderItem> orderItems = orderItemRepository.findAllByUserEmail(email);

        List<OrderItemModel> orderModels = new ArrayList<>();

        for(OrderItem orderItem : orderItems){
            Order order = orderRepository.findById(orderItem.getOrder().getOrderId()).get();

            if(order.getOrderStatus().equals(StringUtil.TO_PAY) && order.isActive()) {
                OrderItemModel orderItemModel = orderItemMapper.mapEntityToModel(orderItem);
                orderItemModel.setOrderTotalAmount(order.getOrderTotalAmount());
                orderItemModel.setOrderStatus(order.getOrderStatus());
                orderItemModel.setActive(order.isActive());
                orderModels.add(orderItemModel);
            }
        }
        return orderModels;
    }

    @Override
    public List<OrderItemModel> getOrdersByCancelledStatus(String email) {
        List<OrderItem> orderItems = orderItemRepository.findAllByUserEmail(email);

        List<OrderItemModel> orderModels = new ArrayList<>();

        for(OrderItem orderItem : orderItems){
            Order order = orderRepository.findById(orderItem.getOrder().getOrderId()).get();

            if(order.getOrderStatus().equals(StringUtil.ORDER_CANCELLED) && !order.isActive()) {
                OrderItemModel orderItemModel = orderItemMapper.mapEntityToModel(orderItem);
                orderItemModel.setOrderTotalAmount(order.getOrderTotalAmount());
                orderItemModel.setOrderStatus(order.getOrderStatus());
                orderItemModel.setActive(order.isActive());
                orderModels.add(orderItemModel);
            }
        }
        return orderModels;
    }
}

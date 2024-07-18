package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.*;
import com.practice.fullstackbackendspringboot.model.OrderItemModel;
import com.practice.fullstackbackendspringboot.model.OrderModel;
import com.practice.fullstackbackendspringboot.model.response.AllOrdersResponse;
import com.practice.fullstackbackendspringboot.model.response.TodoListTotal;
import com.practice.fullstackbackendspringboot.model.response.TotalSales;
import com.practice.fullstackbackendspringboot.repository.*;
import com.practice.fullstackbackendspringboot.service.OrderService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.OrderItemMapper;
import com.practice.fullstackbackendspringboot.utils.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
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
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        List<Cart> cart = cartRepository.findAllByFilterTrueAndUserEmail(email);

        Map<String, List<Cart>> cartsByStore = cart.stream()
                .collect(Collectors.groupingBy(Cart::getStoreId));

        for (Map.Entry<String, List<Cart>> cartMap : cartsByStore.entrySet()) {
            List<Cart> storeCarts = cartMap.getValue();
            String storeId = cartMap.getKey();

            Optional<Store> store = storeRepository.findById(storeId);

            Order order = new Order();
            order.setStore(store.get());
            order.setBuyer(user);
            order.setDeliveryAddress(user.getAddress());
            order.setFullName(user.getName());
            order.setContactNumber(user.getContactNumber());
            order.setActive(true);
            order.setOrderStatus(StringUtil.PENDING);
            order.setOrderStatusInfo(StringUtil.ORDER_CONFIRMATION);
            order.setPaymentMethod(StringUtil.CASH_ON_DELIVERY);
            order = orderRepository.save(order);
            Double storeTotalAmount = 0.0;

            List<OrderItem> orderItems = new ArrayList<>();

            for (Cart carts : storeCarts) {
                Optional<Product> product = productRepository.findById(carts.getProduct().getProductId());
                Optional<Inventory> inventory = inventoryRepository.findById(carts.getInventory().getInventoryId());
                OrderItem orderItem = new OrderItem();

                orderItem.setQuantity(carts.getQuantity());
                orderItem.setTotalAmount(carts.getTotalAmount());
                orderItem.setPrice(inventory.get().getPrice());
                orderItem.setStoreName(product.get().getStore().getStoreName());
                orderItem.setProductName(product.get().getProductName());
                orderItem.setPhotoUrl(product.get().getImage().get(0).getPhotoUrl());
                orderItem.setUser(user);
                orderItem.setColors(carts.getColors());
                orderItem.setSizes(carts.getSizes());
                orderItem.setOrder(order);
                orderItem.setProduct(product.get());
                orderItem.setInventory(inventory.get());
                storeTotalAmount += orderItem.getTotalAmount();
                OrderItem savedOrderItems = orderItemRepository.save(orderItem);
                orderItems.add(savedOrderItems);

                if (carts.getQuantity() > inventory.get().getQuantity()) {
                    throw new IllegalArgumentException(StringUtil.OUT_OF_STOCK);
                } else {
                    Inventory inv = inventory.get();
                    inv.setQuantity(inv.getQuantity() - carts.getQuantity());
                    inventoryRepository.save(inv);
                }
                Product product1 = product.get();
                product1.setProductSold(product1.getProductSold() + carts.getQuantity());
                productRepository.save(product1);
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
                orders.setOrderStatusInfo(StringUtil.ORDER_IS_CANCELLED);
                orderRepository.save(orders);
            }else{
                throw new IllegalArgumentException(StringUtil.ORDER_NOT_FOUND);
            }
        }

        for(OrderItem orderItem: orderItems){
            Optional<Inventory> inventory = inventoryRepository.findById(orderItem.getInventory().getInventoryId());
            if(inventory.isPresent()){
                Inventory inv = inventory.get();
                inv.setQuantity(inv.getQuantity() + orderItem.getQuantity());
                inventoryRepository.save(inv);
            }else{
                throw new IllegalArgumentException(StringUtil.PRODUCT_NOT_FOUND);
            }

            Optional<Product> product = productRepository.findById(orderItem.getProduct().getProductId());
            if(product.isPresent()){
                Product product1 = product.get();
                product1.setProductSold(product1.getProductSold() - orderItem.getQuantity());
                productRepository.save(product1);
            }
        }
    }

    @Override
    public void confirmCancelOrder(String email, String orderId) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<Order> order = orderRepository.findById(orderId);

        if(order.isPresent()){
            Order orders = order.get();
            orders.setActive(false);
            orderRepository.save(orders);
        }
    }

    @Override
    public void buyAgain(String email, String orderId) {
        Optional<User> user = userRepository.findByEmail(email);
        List<OrderItem> orderItems = orderItemRepository.findAllByUserEmailAndOrder_OrderId(email,orderId);
        Long quantity = 1L;

        for(OrderItem orderItem : orderItems){
            Optional<Cart> existingCart = cartRepository.findByProduct_ProductIdAndInventory_InventoryIdAndUserEmail(
                            orderItem.getProduct().getProductId(),
                            orderItem.getInventory().getInventoryId(), email);

            Cart cart;

            if(existingCart.isPresent()){
                cart = existingCart.get();
                cartRepository.save(cart);
            } else {
                Optional<Product> product = productRepository.findById(orderItem.getProduct().getProductId());

                if(!product.get().isDeleted()) {
                    cart = new Cart();
                    cart.setQuantity(quantity);
                    cart.setStoreId(orderItem.getOrder().getStore().getStoreId());
                    cart.setTotalAmount(orderItem.getPrice() * quantity);
                    cart.setProduct(product.get());
                    cart.setOrderItems(orderItems);
                    cart.setSizes(orderItem.getSizes());
                    cart.setColors(orderItem.getColors());
                    cart.setInventory(orderItem.getInventory());
                    cart.setUser(user.get());
                    cartRepository.save(cart);
                }
            }
        }
    }

    @Override
    public void processOrder(String email, String orderId) {
        userRepository.findByEmail(email);
        Optional<Order> order = orderRepository.findById(orderId);

        Order orders = order.get();
        if(orders.isActive() && orders.getOrderStatus().equals(StringUtil.PENDING)) {
            orders.setOrderStatus(StringUtil.TO_PAY);
            orders.setOrderStatusInfo(StringUtil.PREPARE_ORDER);
            orderRepository.save(orders);

            Optional<Store> store = storeRepository.findById(orders.getStore().getStoreId());
            if(store.isPresent()){
                Store store1 = store.get();
                store1.setOrderCount(store1.getOrderCount() + 1L);
                storeRepository.save(store1);
            }
        }
        else if(orders.isActive() && orders.getOrderStatus().equals(StringUtil.TO_PAY)){
            orders.setOrderStatus(StringUtil.TO_SHIP);
            orders.setOrderStatusInfo(StringUtil.SHIPPING_ORDER);
            orderRepository.save(orders);
        }
        else if(orders.isActive() && orders.getOrderStatus().equals(StringUtil.TO_SHIP)){
            orders.setOrderStatus(StringUtil.TO_RECEIVE);
            orders.setOrderStatusInfo(StringUtil.OUT_FOR_DELIVERY);
            orderRepository.save(orders);
        }
        else if(orders.isActive() && orders.getOrderStatus().equals(StringUtil.TO_RECEIVE)){
            orders.setOrderStatus(StringUtil.ORDER_COMPLETED);
            orders.setOrderStatusInfo(StringUtil.ORDER_DELIVERED);
            orders.setActive(false);
            orderRepository.save(orders);
        }
    }

    @Override
    public List<OrderItemModel> getCustomerOrdersByStatus(String email, String status1) {
        Sort sort = Sort.by(Sort.Direction.DESC, StringUtil.Created_Date);
        List<OrderItem> orderItems = orderItemRepository.findAllByUserEmail(email, sort);

        List<OrderItemModel> orderModels = new ArrayList<>();

        for(OrderItem orderItem : orderItems){
            Order order = orderRepository.findById(orderItem.getOrder().getOrderId()).get();
            Product product = productRepository.findById(orderItem.getProduct().getProductId()).get();

            if(order.isActive() && order.getOrderStatus().equals(status1)
                    || !order.isActive() && order.getOrderStatus().equals(status1)
                    || status1.isEmpty()) {
                OrderItemModel orderItemModel = orderItemMapper.mapEntityToModel(orderItem);
                orderItemModel.setOrderTotalAmount(order.getOrderTotalAmount());
                orderItemModel.setOrderStatus(order.getOrderStatus());
                orderItemModel.setOrderStatusInfo(order.getOrderStatusInfo());
                orderItemModel.setActive(order.isActive());
                orderItemModel.setStoreId(order.getStore().getStoreId());
                orderItemModel.setProductId(product.getProductId());
                orderModels.add(orderItemModel);
            }
        }
        return orderModels;
    }

    @Override
    public List<OrderItemModel> getCustomerOrdersByCompletedAndRatedStatus(String email) {
        Sort sort = Sort.by(Sort.Direction.DESC, StringUtil.Created_Date);
        List<OrderItem> orderItems = orderItemRepository.findAllByUserEmail(email, sort);

        List<OrderItemModel> orderModels = new ArrayList<>();

        for(OrderItem orderItem : orderItems){
            Order order = orderRepository.findById(orderItem.getOrder().getOrderId()).get();
            Product product = productRepository.findById(orderItem.getProduct().getProductId()).get();

            if(order.getOrderStatus().equals(StringUtil.ORDER_COMPLETED) || order.getOrderStatus().equals(StringUtil.RATED)) {
                OrderItemModel orderItemModel = orderItemMapper.mapEntityToModel(orderItem);
                orderItemModel.setOrderTotalAmount(order.getOrderTotalAmount());
                orderItemModel.setOrderStatus(order.getOrderStatus());
                orderItemModel.setOrderStatusInfo(order.getOrderStatusInfo());
                orderItemModel.setActive(order.isActive());
                orderItemModel.setStoreId(order.getStore().getStoreId());
                orderItemModel.setProductId(product.getProductId());
                orderModels.add(orderItemModel);
            }
        }
        return orderModels;
    }

    @Override
    public AllOrdersResponse getStoreOrdersByStatus(String email, String storeId, String status1) {
        userRepository.findByEmail(email);
        List<Order> orders = orderRepository.findAllByStore_StoreId(storeId);
        List<OrderModel> orderModels = new ArrayList<>();

        Map<String, List<Order>> listOfOrders = orders.stream().collect(Collectors.groupingBy(Order::getOrderId));

        for(Map.Entry<String, List<Order>> orderMap : listOfOrders.entrySet()) {
            List<Order> orderList = orderMap.getValue();

            for(Order order: orderList) {
                if (order.isActive() && order.getOrderStatus().equals(status1)
                        || !order.isActive() && order.getOrderStatus().equals(status1)
                        || status1.isEmpty()) {
                    OrderModel orderModel = new OrderModel();
                    orderModel.setOrderId(order.getOrderId());
                    orderModel.setOrderTotalAmount(order.getOrderTotalAmount());
                    orderModel.setPaymentMethod(order.getPaymentMethod());
                    orderModel.setActive(order.isActive());
                    orderModel.setOrderStatus(order.getOrderStatus());
                    orderModel.setOrderStatusInfo(order.getOrderStatusInfo());
                    orderModel.setDeliveryAddress(order.getDeliveryAddress());
                    orderModel.setFullName(order.getFullName());
                    orderModel.setContactNumber(order.getContactNumber());
                    orderModel.setCreatedDate(order.getCreatedDate());

                    List<OrderItemModel> orderItemModels = new ArrayList<>();
                    List<OrderItem> orderItems = order.getOrderItems();

                    for (OrderItem orderItem : orderItems) {
                        OrderItemModel orderItemModel = orderItemMapper.mapEntityToModel(orderItem);
                        orderItemModel.setOrderTotalAmount(order.getOrderTotalAmount());
                        orderItemModel.setOrderStatus(order.getOrderStatus());
                        orderItemModel.setActive(order.isActive());
                        orderItemModel.setStoreId(order.getStore().getStoreId());
                        orderItemModel.setFullName(order.getFullName());
                        orderItemModels.add(orderItemModel);
                    }
                    orderModel.setOrderItemModels(orderItemModels);
                    orderModels.sort(Comparator.comparing(OrderModel::getCreatedDate).reversed());
                    orderModels.add(orderModel);
                }
            }
        }
        return new AllOrdersResponse(orderModels);
    }

    @Override
    public Set<OrderItemModel> getCustomerOrdersByOrderIdToRate(String email, String orderId) {
        Set<OrderItem> orderItems = orderItemRepository.findAllByRatedFalseAndOrder_OrderIdAndUserEmail(orderId, email);
        Set<OrderItemModel> orderItemModels = new HashSet<>();
        Set<String> productIds = new HashSet<>();

        for(OrderItem orderItem : orderItems){
            String productId = orderItem.getProduct().getProductId();

            if(!productIds.contains(productId)) {
                OrderItemModel orderItemModel = orderItemMapper.mapEntityToModel(orderItem);
                orderItemModels.add(orderItemModel);
                productIds.add(productId);
            }
        }
        return orderItemModels;
    }

    @Override
    public TodoListTotal getSellersTodoListTotal(String email, String storeId) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<Store> store = storeRepository.findById(storeId);
        List<Order> orders = orderRepository.findAllByActiveTrueAndStore_StoreId(storeId);

        long pendingTotal = 0L;
        long toProcessTotal = 0L;
        long processShipmentTotal = 0L;
        long pendingCancelledTotal = 0L;

        if(store.isPresent()) {
            for (Order order : orders) {
                if (order.getOrderStatus().equals(StringUtil.PENDING)) {
                    long activeOrder = 1L;
                    pendingTotal += activeOrder;
                }

                if (order.getOrderStatus().equals(StringUtil.TO_PAY)) {
                    long activeOrder = 1L;
                    toProcessTotal += activeOrder;
                }

                if (order.getOrderStatus().equals(StringUtil.TO_SHIP)) {
                    long activeOrder = 1L;
                    processShipmentTotal += activeOrder;
                }

                if (order.getOrderStatus().equals(StringUtil.ORDER_CANCELLED)) {
                    long activeOrder = 1L;
                    pendingCancelledTotal += activeOrder;
                }
            }
        }

        List<Product> products = productRepository.findAllByDeletedFalseAndStore_StoreId(storeId);
        long outOfStock = 0L;

        for(Product product : products){
            List<Inventory> inventories = product.getInventory().stream().toList();

            for(Inventory inventory : inventories){
                long quantity = inventory.getQuantity();
                long qty = 1L;
                if(quantity == 0){
                    outOfStock += qty;
                }
            }
        }

        TodoListTotal total = new TodoListTotal();
        total.setPendingOrderTotal(pendingTotal);
        total.setToProcessShipmentTotal(toProcessTotal);
        total.setProcessedShipmentTotal(processShipmentTotal);
        total.setPendingCancelledOrders(pendingCancelledTotal);
        total.setOutOfStock(outOfStock);

        return total;
    }

    @Override
    public TotalSales getTotalSales(String email, String storeId) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<Store> store = storeRepository.findById(storeId);
        List<Order> orders = orderRepository.findAllByActiveFalseAndStore_StoreId(storeId);
        double totalSale = 0.0;

        if(store.isPresent()) {
            for (Order order : orders) {
                if (order.getOrderStatus().equals(StringUtil.ORDER_COMPLETED)) {
                    double sales = order.getOrderTotalAmount() - store.get().getShippingFee();
                    totalSale += sales;
                }
            }
        }
        TotalSales totalSales = new TotalSales();
        totalSales.setTotalSales(totalSale);

        return totalSales;
    }
}

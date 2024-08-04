package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.*;
import com.practice.fullstackbackendspringboot.model.OrderItemModel;
import com.practice.fullstackbackendspringboot.model.OrderModel;
import com.practice.fullstackbackendspringboot.model.response.*;
import com.practice.fullstackbackendspringboot.repository.*;
import com.practice.fullstackbackendspringboot.service.OrderService;
import com.practice.fullstackbackendspringboot.service.PaymentService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.OrderItemMapper;
import com.practice.fullstackbackendspringboot.utils.mapper.OrderMapper;
import com.stripe.exception.StripeException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final PaymentService paymentService;

    @Override
    public PaymentResponse placeOrder(String email, String paymentMethod) throws StripeException {      //CUSTOMER
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        List<Cart> cart = cartRepository.findAllByFilterTrueAndUserEmail(email);

        Map<String, List<Cart>> cartsByStore = cart.stream()
                .collect(Collectors.groupingBy(Cart::getStoreId));

        double totalAmount = 0.0;

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
            if(paymentMethod.equals(StringUtil.Stripe)){
                order.setPaymentMethod(StringUtil.StripePayment);
                order.setOrderStatus(StringUtil.TO_SHIP);
                order.setOrderStatusInfo(StringUtil.SHIPPING_ORDER);

                Store store1 = store.get();
                store1.setOrderCount(store1.getOrderCount() + 1L);
                storeRepository.save(store1);

            } else if(paymentMethod.equals(StringUtil.Cash)){
                order.setPaymentMethod(StringUtil.CASH_ON_DELIVERY);
                order.setOrderStatus(StringUtil.PENDING);
                order.setOrderStatusInfo(StringUtil.ORDER_CONFIRMATION);
            }
            order = orderRepository.save(order);
            Double storeTotalAmount = 0.0;

            List<OrderItem> orderItems = new ArrayList<>();

            for (Cart carts : storeCarts) {
                Optional<Product> optionalProduct = productRepository.findByProductIdAndListedTrueAndSuspendedFalseAndDeletedFalse(carts.getProduct().getProductId());
                Optional<Inventory> inventory = inventoryRepository.findById(carts.getInventory().getInventoryId());

                if(optionalProduct.isPresent()) {
                    Product product = optionalProduct.get();

                    OrderItem orderItem = new OrderItem();
                    orderItem.setQuantity(carts.getQuantity());
                    orderItem.setTotalAmount(carts.getTotalAmount());
                    orderItem.setPrice(inventory.get().getPrice());
                    orderItem.setStoreName(product.getStore().getStoreName());
                    orderItem.setProductName(product.getProductName());
                    orderItem.setPhotoUrl(product.getImage().get(0).getPhotoUrl());
                    orderItem.setUser(user);
                    orderItem.setColors(carts.getColors());
                    orderItem.setSizes(carts.getSizes());
                    orderItem.setOrder(order);
                    orderItem.setProduct(product);
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

                    product.setProductSold(product.getProductSold() + carts.getQuantity());
                    productRepository.save(product);

                }
            }

            order.setOrderTotalAmount(storeTotalAmount + store.get().getShippingFee());
            totalAmount += order.getOrderTotalAmount();
            order.setOrderItems(orderItems);
            orderRepository.save(order);
        }
        cartRepository.deleteAllByFilterTrueAndUserEmail(email);

        return paymentService.paymentLink(totalAmount, paymentMethod);
    }

    @Override
    public void buyAgain(String email, String orderId) {        //CUSTOMER
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
                Optional<Product> optionalProduct = productRepository.findByProductIdAndListedTrueAndSuspendedFalseAndDeletedFalse(orderItem.getProduct().getProductId());
                if(optionalProduct.isPresent()){
                    Product product = optionalProduct.get();
                    cart = new Cart();
                    cart.setQuantity(quantity);
                    cart.setStoreId(orderItem.getOrder().getStore().getStoreId());
                    cart.setTotalAmount(orderItem.getPrice() * quantity);
                    cart.setProduct(product);
                    cart.setOrderItems(orderItems);
                    cart.setSizes(orderItem.getSizes());
                    cart.setColors(orderItem.getColors());
                    cart.setInventory(orderItem.getInventory());
                    cart.setUser(user.get());
                    cartRepository.save(cart);
                } else {
                    throw new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND);
                }
            }
        }
    }

    @Override
    public void cancelOrder(String orderId) {     //CUSTOMER
        Optional<Order> order = orderRepository.findById(orderId);
        List<OrderItem> orderItems = orderItemRepository.findAllByOrder_OrderId(orderId);

            if(order.isPresent()){
                Order orders = order.get();
                orders.setOrderStatus(StringUtil.ORDER_CANCELLED);
                orders.setOrderStatusInfo(StringUtil.ORDER_IS_CANCELLED);
                orderRepository.save(orders);
            }else{
                throw new NoSuchElementException(StringUtil.ORDER_NOT_FOUND);
            }

        for(OrderItem orderItem: orderItems){
            Optional<Inventory> inventory = inventoryRepository.findById(orderItem.getInventory().getInventoryId());
            if(inventory.isPresent()){
                Inventory inv = inventory.get();
                inv.setQuantity(inv.getQuantity() + orderItem.getQuantity());
                inventoryRepository.save(inv);
            }else{
                throw new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND);
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
    public List<OrderItemModel> getCustomerOrdersByStatus(String email, String status1) {       //CUSTOMER
        Sort sort = Sort.by(Sort.Direction.DESC, StringUtil.Created_Date);
        List<OrderItem> orderItems = orderItemRepository.findAllByUserEmail(email, sort);

        List<OrderItemModel> orderModels = new ArrayList<>();

        for(OrderItem orderItem : orderItems){
            Order order = orderRepository.findById(orderItem.getOrder().getOrderId()).get();
            Product product = productRepository.findById(orderItem.getProduct().getProductId()).get();

            if(order.isActive() && order.getOrderStatus().equals(status1)
                    || !order.isActive() && order.getOrderStatus().equals(status1)
                    || status1.isEmpty()) {
                orderModels.add(this.mapOrderItems(order,orderItem,product));
            }
        }
        return orderModels;
    }


    @Override
    public List<OrderItemModel> getCustomerOrdersByCompletedAndRatedStatus(String email) {      //CUSTOMER
        Sort sort = Sort.by(Sort.Direction.DESC, StringUtil.Created_Date);
        List<OrderItem> orderItems = orderItemRepository.findAllByUserEmail(email, sort);

        List<OrderItemModel> orderModels = new ArrayList<>();

        for(OrderItem orderItem : orderItems){
            Order order = orderRepository.findById(orderItem.getOrder().getOrderId()).get();
            Product product = productRepository.findById(orderItem.getProduct().getProductId()).get();

            if(order.getOrderStatus().equals(StringUtil.ORDER_COMPLETED) || order.getOrderStatus().equals(StringUtil.RATED)) {
                orderModels.add(this.mapOrderItems(order,orderItem,product));
            }
        }
        return orderModels;
    }

    @Override
    public Set<OrderItemModel> getCustomerOrdersByOrderIdToRate(String orderId) {     //CUSTOMER
        Set<OrderItem> orderItems = orderItemRepository.findAllByRatedFalseAndOrder_OrderId(orderId);
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
    public void processOrder(String orderId) {        //SELLER
        Optional<Order> order = orderRepository.findById(orderId);

        if(order.isPresent()) {
            Order orders = order.get();
            if (orders.isActive() && orders.getOrderStatus().equals(StringUtil.PENDING)) {
                orders.setOrderStatus(StringUtil.TO_PAY);
                orders.setOrderStatusInfo(StringUtil.PREPARE_ORDER);
                orderRepository.save(orders);

                Optional<Store> store = storeRepository.findById(orders.getStore().getStoreId());
                if (store.isPresent()) {
                    Store store1 = store.get();
                    store1.setOrderCount(store1.getOrderCount() + 1L);
                    storeRepository.save(store1);
                }
            } else if (orders.isActive() && orders.getOrderStatus().equals(StringUtil.TO_PAY)) {
                orders.setOrderStatus(StringUtil.TO_SHIP);
                orders.setOrderStatusInfo(StringUtil.SHIPPING_ORDER);
                orderRepository.save(orders);
            } else if (orders.isActive() && orders.getOrderStatus().equals(StringUtil.TO_SHIP)) {
                orders.setOrderStatus(StringUtil.TO_RECEIVE);
                orders.setOrderStatusInfo(StringUtil.OUT_FOR_DELIVERY);
                orderRepository.save(orders);
            } else if (orders.isActive() && orders.getOrderStatus().equals(StringUtil.TO_RECEIVE)) {
                orders.setOrderStatus(StringUtil.ORDER_COMPLETED);
                orders.setOrderStatusInfo(StringUtil.ORDER_DELIVERED);
                orders.setActive(false);
                orderRepository.save(orders);
            }
        }
    }

    @Override
    public void confirmCancelOrder(String orderId) {      //SELLER
        Optional<Order> order = orderRepository.findById(orderId);
        if(order.isPresent()){
            Order orders = order.get();
            orders.setActive(false);
            orderRepository.save(orders);
        }
    }

    @Override
    public AllOrdersResponse getStoreOrdersByStatus(String storeId, String status1) {     //SELLER

        List<Order> orders = orderRepository.findAllByStore_StoreId(storeId);
        List<OrderModel> orderModels = new ArrayList<>();

        Map<String, List<Order>> listOfOrders = orders.stream().collect(Collectors.groupingBy(Order::getOrderId));

        for(Map.Entry<String, List<Order>> orderMap : listOfOrders.entrySet()) {
            List<Order> orderList = orderMap.getValue();

            for(Order order: orderList) {
                if (order.isActive() && order.getOrderStatus().equals(status1)
                        || !order.isActive() && order.getOrderStatus().equals(status1)
                        || status1.isEmpty()) {

                    OrderModel orderModel = this.mapOrders(order);
                    orderModels.sort(Comparator.comparing(OrderModel::getCreatedDate).reversed());
                    orderModels.add(orderModel);
                }
            }
        }
        return new AllOrdersResponse(orderModels);
    }

    @Override
    public AllOrdersResponse getStoreOrdersByCompletedAndRatedStatus(String storeId) {    //SELLER

        List<Order> orders = orderRepository.findAllByStore_StoreId(storeId);
        List<OrderModel> orderModels = new ArrayList<>();

        Map<String, List<Order>> listOfOrders = orders.stream().collect(Collectors.groupingBy(Order::getOrderId));

        for(Map.Entry<String, List<Order>> orderMap : listOfOrders.entrySet()) {
            List<Order> orderList = orderMap.getValue();

            for(Order order: orderList) {
                if (order.getOrderStatus().equals(StringUtil.RATED)
                        || order.getOrderStatus().equals(StringUtil.ORDER_COMPLETED)) {
                    OrderModel orderModel = this.mapOrders(order);
                    orderModels.sort(Comparator.comparing(OrderModel::getCreatedDate).reversed());
                    orderModels.add(orderModel);
                }
            }
        }
        return new AllOrdersResponse(orderModels);
    }

    @Override
    public TodoListTotal getSellersTodoListTotal(String storeId) {    //SELLER
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

        List<Product> products = productRepository.findAllByDeletedFalseAndListedTrueAndSuspendedFalseAndStore_StoreId(storeId);
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
    public TotalSales getTotalSales(String storeId) {     //SELLER

        Optional<Store> store = storeRepository.findById(storeId);
        List<Order> orders = orderRepository.findAllByActiveFalseAndStore_StoreId(storeId);
        double totalSale = 0.0;

        if(store.isPresent()) {
            for (Order order : orders) {
                if (order.getOrderStatus().equals(StringUtil.ORDER_COMPLETED) || order.getOrderStatus().equals(StringUtil.RATED)) {
                    double sales = order.getOrderTotalAmount() - store.get().getShippingFee();
                    totalSale += sales;
                }
            }
        }else {
            throw new NoSuchElementException(StringUtil.STORE_NOT_FOUND);
        }

        TotalSales totalSales = new TotalSales();
        totalSales.setTotalSales(totalSale);

        return totalSales;
    }

    @Override
    public OrderCount getOrderCountAndTotalSales() {    //ADMIN

        List<Order> orders = orderRepository.findAll();

        double countOrder = orderRepository.count();
        double sales = 0.0;
        double shippingTotal = 0.0;

        for(Order order : orders){
            if(order.getOrderStatus().equals(StringUtil.ORDER_COMPLETED) || order.getOrderStatus().equals(StringUtil.RATED)){

                double shippingAmount = order.getStore().getShippingFee();
                shippingTotal += shippingAmount;

                double orderAmount = order.getOrderTotalAmount() - order.getStore().getShippingFee();
                sales+=orderAmount;
            }
        }
        OrderCount orderCount = new OrderCount();
        orderCount.setOrderCount(countOrder);
        orderCount.setTotalShippingFee(shippingTotal);
        orderCount.setTotalSales(sales);
        return orderCount;
    }

    @Override
    public PaginateOrderResponse getAllOrders(int pageNo, int pageSize) { //ADMIN

        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(StringUtil.LAST_MODIFIED).descending());
        Page<Order> orders = orderRepository.findAll(pageable);
        List<OrderModel> orderModels = new ArrayList<>();

        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(orders.getNumber());
        pageResponse.setPageSize(orders.getSize());
        pageResponse.setTotalElements(orders.getTotalElements());
        pageResponse.setTotalPages(orders.getTotalPages());
        pageResponse.setLast(orders.isLast());

        for(Order order : orders) {
            OrderModel orderModel = orderMapper.mapOrderEntityToOrderModel(order);
            orderModel.setShopName(order.getStore().getStoreName());
            orderModels.add(orderModel);
        }

        return new PaginateOrderResponse(orderModels,pageResponse);
    }

    private OrderItemModel mapOrderItems(Order order, OrderItem orderItem, Product product){
        OrderItemModel orderItemModel = orderItemMapper.mapEntityToModel(orderItem);
        orderItemModel.setOrderTotalAmount(order.getOrderTotalAmount());
        orderItemModel.setOrderStatus(order.getOrderStatus());
        orderItemModel.setOrderStatusInfo(order.getOrderStatusInfo());
        orderItemModel.setActive(order.isActive());
        orderItemModel.setStoreId(order.getStore().getStoreId());
        orderItemModel.setProductId(product.getProductId());
        orderItemModel.setStoreRated(orderItem.getOrder().isStoreRated());
        return orderItemModel;
    }

    private OrderModel mapOrders(Order order){
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
        return orderModel;
    }
}

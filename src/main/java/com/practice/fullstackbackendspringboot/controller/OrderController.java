package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.OrderItemModel;
import com.practice.fullstackbackendspringboot.model.response.*;
import com.practice.fullstackbackendspringboot.service.OrderService;
import com.practice.fullstackbackendspringboot.service.UserService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @PostMapping("/{paymentMethod}")
    public ResponseEntity<?> placeOrder(@PathVariable String paymentMethod) throws StripeException {
        String user = userService.getAuthenticatedUser();
        PaymentResponse response =  orderService.placeOrder(user,paymentMethod);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/buy/{orderId}")
    public void buyAgain(@PathVariable (value="orderId") String orderId){
        String user = userService.getAuthenticatedUser();
        orderService.buyAgain(user,orderId);
    }

    @PostMapping("/cancel/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public void cancelOrder(@PathVariable (value="orderId") String orderId) {
        orderService.cancelOrder(orderId);
    }

    @PostMapping("/confirm/cancel/{orderId}")
    public void confirmCancelOrder(@PathVariable (value="orderId") String orderId) {
        orderService.confirmCancelOrder(orderId);
    }

    @PutMapping("/process/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public void processOrder(@PathVariable (value="orderId") String orderId){
        orderService.processOrder(orderId);
    }
    @GetMapping("/get/all")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getAllCustomerOrders(){
        String user = userService.getAuthenticatedUser();
        String status1 = "";
        return orderService.getCustomerOrdersByStatus(user, status1);
    }
    @GetMapping("/get/pending")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getCustomerOrdersByToPendingStatus(){
        String user = userService.getAuthenticatedUser();
        String status1 = StringUtil.PENDING;
        return orderService.getCustomerOrdersByStatus(user, status1);
    }
    @GetMapping("/get/to-pay")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getCustomerOrdersByToPayStatus(){
        String user = userService.getAuthenticatedUser();
        String status1 = StringUtil.TO_PAY;
        return orderService.getCustomerOrdersByStatus(user, status1);
    }

    @GetMapping("/get/to-ship")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getCustomerOrdersByToShipStatus(){
        String user = userService.getAuthenticatedUser();
        String status = StringUtil.TO_SHIP;
        return orderService.getCustomerOrdersByStatus(user, status);
    }

    @GetMapping("/get/to-receive")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getCustomerOrdersByToReceiveStatus() {
        String user = userService.getAuthenticatedUser();
        String status = StringUtil.TO_RECEIVE;
        return orderService.getCustomerOrdersByStatus(user, status);
    }
    @GetMapping("/get/cancelled")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getCustomerOrdersByCancelledStatus(){
        String user = userService.getAuthenticatedUser();
        String status = StringUtil.ORDER_CANCELLED;
        return orderService.getCustomerOrdersByStatus(user, status);
    }

    @GetMapping("/get/completed")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getCustomerOrdersByCompletedStatus(){
        String user = userService.getAuthenticatedUser();
        return orderService.getCustomerOrdersByCompletedAndRatedStatus(user);
    }

    @GetMapping("/seller/get/pending/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public AllOrdersResponse getStoreOrdersByPendingStatus(@PathVariable(value="storeId") String storeId ){
        String status1 = StringUtil.PENDING;
        return orderService.getStoreOrdersByStatus(storeId, status1);
    }

    @GetMapping("/seller/get/unpaid/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public AllOrdersResponse getStoreOrdersByUnpaidStatus(@PathVariable(value="storeId") String storeId ){
        String status1 = StringUtil.TO_PAY;
        return orderService.getStoreOrdersByStatus(storeId, status1);
    }

    @GetMapping("/seller/get/to-ship/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public AllOrdersResponse getStoreOrdersByToShipStatus(@PathVariable(value="storeId") String storeId ){
        String status1 = StringUtil.TO_SHIP;
        return orderService.getStoreOrdersByStatus(storeId, status1);
    }

    @GetMapping("/seller/get/shipping/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public AllOrdersResponse getStoreOrdersByToReceiveStatus(@PathVariable(value="storeId") String storeId ){
        String status1 = StringUtil.TO_RECEIVE;
        return orderService.getStoreOrdersByStatus(storeId, status1);
    }

    @GetMapping("/seller/get/cancelled/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public AllOrdersResponse getStoreOrdersByToCancelled(@PathVariable(value="storeId") String storeId ){
        String status1 = StringUtil.ORDER_CANCELLED;
        return orderService.getStoreOrdersByStatus(storeId, status1);
    }

    @GetMapping("/seller/get/completed/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public AllOrdersResponse getStoreOrdersByCompletedOrders(@PathVariable(value="storeId") String storeId ){
        return orderService.getStoreOrdersByCompletedAndRatedStatus(storeId);
    }

    @GetMapping("/seller/get/all/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public AllOrdersResponse getAllStoreOrder(@PathVariable(value="storeId") String storeId ){
        String status1 = "";
        return orderService.getStoreOrdersByStatus(storeId, status1);
    }

    @GetMapping("/get/{orderId}")
    public Set<OrderItemModel> getCustomerOrdersByOrderIdToRate(@PathVariable(value="orderId") String orderId){
        return orderService.getCustomerOrdersByOrderIdToRate(orderId);
    }
    @GetMapping("/get/todo/total/{storeId}")
    public TodoListTotal getSellersTodoListTotal(String email, @PathVariable(value="storeId") String storeId){
        return orderService.getSellersTodoListTotal(storeId);
    }
    @GetMapping("/get/sales/total/{storeId}")
    public TotalSales getTotalSales(@PathVariable(value="storeId") String storeId) {
        return orderService.getTotalSales(storeId);
    }
    @GetMapping("/count")
    public OrderCount getOrderCountAndTotalSales() {
        return orderService.getOrderCountAndTotalSales();
    }

    @GetMapping("/all")
    public PaginateOrderResponse getAllOrders(
                                              @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                              @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize){
        return orderService.getAllOrders(pageNo,pageSize);
    }

}

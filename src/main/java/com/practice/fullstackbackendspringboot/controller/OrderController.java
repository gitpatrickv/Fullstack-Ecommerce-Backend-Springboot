package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.OrderItemModel;
import com.practice.fullstackbackendspringboot.model.response.AllOrdersResponse;
import com.practice.fullstackbackendspringboot.service.OrderService;
import com.practice.fullstackbackendspringboot.service.UserService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public void placeOrder(@RequestHeader("Authorization") String email){
        String user = userService.getUserFromToken(email);
        orderService.placeOrder(user);
    }
    @PostMapping("/buy/{orderId}")
    public void buyAgain(@RequestHeader("Authorization") String email, @PathVariable (value="orderId") String orderId){
        String user = userService.getUserFromToken(email);
        orderService.buyAgain(user,orderId);
    }

    @PostMapping("/cancel/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public void cancelOrder(@RequestHeader("Authorization") String email, @PathVariable (value="orderId") String orderId) {
        String user = userService.getUserFromToken(email);
        orderService.cancelOrder(user, orderId);
    }
    @PutMapping("/ship/{orderId}")
    public void shipOrder(@RequestHeader("Authorization") String email, @PathVariable (value="orderId") String orderId){
        String user = userService.getUserFromToken(email);
        orderService.shipOrder(user,orderId);
    }

    @GetMapping("/get/to-pay")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getCustomerOrdersByToPayStatus(@RequestHeader("Authorization") String email ){
        String user =  userService.getUserFromToken(email);
        String status1 = StringUtil.TO_PAY;
        String status2 = StringUtil.PENDING;
        return orderService.getCustomerOrdersByStatus(user, status1, status2);
    }

    @GetMapping("/get/to-ship")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getCustomerOrdersByToShipStatus(@RequestHeader("Authorization") String email){
        String user =  userService.getUserFromToken(email);
        String status = StringUtil.TO_SHIP;
        return orderService.getCustomerOrdersByStatus(user, status, status);
    }

    @GetMapping("/get/cancelled")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getCustomerOrdersByCancelledStatus(@RequestHeader("Authorization") String email){
        String user =  userService.getUserFromToken(email);
        String status = StringUtil.ORDER_CANCELLED;
        return orderService.getCustomerOrdersByStatus(user, status, status);
    }

    @GetMapping("/seller/get/pending/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public AllOrdersResponse getStoreOrdersByPendingStatus(@RequestHeader("Authorization") String email, @PathVariable(value="storeId") String storeId ){
        String user =  userService.getUserFromToken(email);
        String status1 = StringUtil.PENDING;
        return orderService.getStoreOrdersByStatus(user, storeId, status1);
    }

    @GetMapping("/seller/get/unpaid/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public AllOrdersResponse getStoreOrdersByUnpaidStatus(@RequestHeader("Authorization") String email, @PathVariable(value="storeId") String storeId ){
        String user =  userService.getUserFromToken(email);
        String status1 = StringUtil.TO_PAY;
        return orderService.getStoreOrdersByStatus(user, storeId, status1);
    }
}

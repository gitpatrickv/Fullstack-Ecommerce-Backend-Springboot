package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.OrderItemModel;
import com.practice.fullstackbackendspringboot.model.OrderModel;
import com.practice.fullstackbackendspringboot.service.OrderService;
import com.practice.fullstackbackendspringboot.service.UserService;
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

    @GetMapping("/get/to-pay")
    public List<OrderItemModel> getOrdersByToPayStatus(@RequestHeader("Authorization") String email){
        userService.getUserFromToken(email);
        return orderService.getOrdersByToPayStatus();
    }

//    @GetMapping("/get/to-pay")
//    public List<OrderModel> getOrdersByToPayStatus(@RequestHeader("Authorization") String email){
//        String user = userService.getUserFromToken(email);
//        return orderService.getOrdersByToPayStatus(user);
//    }

}

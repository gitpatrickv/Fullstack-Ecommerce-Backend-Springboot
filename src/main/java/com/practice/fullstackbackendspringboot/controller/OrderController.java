package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.OrderItemModel;
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
    public List<OrderItemModel> getOrdersByToPayStatus(@RequestHeader("Authorization") String email ){
        String user =  userService.getUserFromToken(email);
        String status = StringUtil.TO_PAY;
        return orderService.getOrdersByStatus(user, status);
    }

    @GetMapping("/get/to-ship")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getOrdersByToShipStatus(@RequestHeader("Authorization") String email){
        String user =  userService.getUserFromToken(email);
        String status = StringUtil.TO_SHIP;
        return orderService.getOrdersByStatus(user, status);
    }

    @GetMapping("/get/cancelled")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getOrdersByCancelledStatus(@RequestHeader("Authorization") String email){
        String user =  userService.getUserFromToken(email);
        String status = StringUtil.ORDER_CANCELLED;
        return orderService.getOrdersByStatus(user, status);
    }
}

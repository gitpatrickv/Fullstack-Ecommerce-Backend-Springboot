package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.OrderItemModel;
import com.practice.fullstackbackendspringboot.model.OrderModel;
import com.practice.fullstackbackendspringboot.model.response.AllOrdersResponse;
import com.practice.fullstackbackendspringboot.model.response.OrderCount;
import com.practice.fullstackbackendspringboot.model.response.TodoListTotal;
import com.practice.fullstackbackendspringboot.model.response.TotalSales;
import com.practice.fullstackbackendspringboot.service.OrderService;
import com.practice.fullstackbackendspringboot.service.UserService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    @PostMapping("/confirm/cancel/{orderId}")
    public void confirmCancelOrder(@RequestHeader("Authorization") String email, @PathVariable (value="orderId") String orderId) {
        String user = userService.getUserFromToken(email);
        orderService.confirmCancelOrder(user,orderId);
    }

    @PutMapping("/process/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public void processOrder(@RequestHeader("Authorization") String email, @PathVariable (value="orderId") String orderId){
        String user = userService.getUserFromToken(email);
        orderService.processOrder(user,orderId);
    }
    @GetMapping("/get/all")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getAllCustomerOrders(@RequestHeader("Authorization") String email ){
        String user =  userService.getUserFromToken(email);
        String status1 = "";
        return orderService.getCustomerOrdersByStatus(user, status1);
    }
    @GetMapping("/get/pending")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getCustomerOrdersByToPendingStatus(@RequestHeader("Authorization") String email ){
        String user =  userService.getUserFromToken(email);
        String status1 = StringUtil.PENDING;
        return orderService.getCustomerOrdersByStatus(user, status1);
    }
    @GetMapping("/get/to-pay")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getCustomerOrdersByToPayStatus(@RequestHeader("Authorization") String email ){
        String user =  userService.getUserFromToken(email);
        String status1 = StringUtil.TO_PAY;
        return orderService.getCustomerOrdersByStatus(user, status1);
    }

    @GetMapping("/get/to-ship")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getCustomerOrdersByToShipStatus(@RequestHeader("Authorization") String email){
        String user =  userService.getUserFromToken(email);
        String status = StringUtil.TO_SHIP;
        return orderService.getCustomerOrdersByStatus(user, status);
    }

    @GetMapping("/get/to-receive")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getCustomerOrdersByToReceiveStatus(@RequestHeader("Authorization") String email) {
        String user = userService.getUserFromToken(email);
        String status = StringUtil.TO_RECEIVE;
        return orderService.getCustomerOrdersByStatus(user, status);
    }
    @GetMapping("/get/cancelled")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getCustomerOrdersByCancelledStatus(@RequestHeader("Authorization") String email){
        String user =  userService.getUserFromToken(email);
        String status = StringUtil.ORDER_CANCELLED;
        return orderService.getCustomerOrdersByStatus(user, status);
    }

    @GetMapping("/get/completed")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderItemModel> getCustomerOrdersByCompletedStatus(@RequestHeader("Authorization") String email){
        String user =  userService.getUserFromToken(email);
        return orderService.getCustomerOrdersByCompletedAndRatedStatus(user);
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

    @GetMapping("/seller/get/to-ship/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public AllOrdersResponse getStoreOrdersByToShipStatus(@RequestHeader("Authorization") String email, @PathVariable(value="storeId") String storeId ){
        String user =  userService.getUserFromToken(email);
        String status1 = StringUtil.TO_SHIP;
        return orderService.getStoreOrdersByStatus(user, storeId, status1);
    }

    @GetMapping("/seller/get/shipping/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public AllOrdersResponse getStoreOrdersByToReceiveStatus(@RequestHeader("Authorization") String email, @PathVariable(value="storeId") String storeId ){
        String user =  userService.getUserFromToken(email);
        String status1 = StringUtil.TO_RECEIVE;
        return orderService.getStoreOrdersByStatus(user, storeId, status1);
    }

    @GetMapping("/seller/get/cancelled/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public AllOrdersResponse getStoreOrdersByToCancelled(@RequestHeader("Authorization") String email, @PathVariable(value="storeId") String storeId ){
        String user =  userService.getUserFromToken(email);
        String status1 = StringUtil.ORDER_CANCELLED;
        return orderService.getStoreOrdersByStatus(user, storeId, status1);
    }

    @GetMapping("/seller/get/completed/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public AllOrdersResponse getStoreOrdersByCompletedOrders(@RequestHeader("Authorization") String email, @PathVariable(value="storeId") String storeId ){
        String user =  userService.getUserFromToken(email);
        String status1 = StringUtil.ORDER_COMPLETED;
        return orderService.getStoreOrdersByStatus(user, storeId, status1);
    }

    @GetMapping("/seller/get/all/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public AllOrdersResponse getAllStoreOrder(@RequestHeader("Authorization") String email, @PathVariable(value="storeId") String storeId ){
        String user =  userService.getUserFromToken(email);
        String status1 = "";
        return orderService.getStoreOrdersByStatus(user, storeId, status1);
    }

    @GetMapping("/get/{orderId}")
    public Set<OrderItemModel> getCustomerOrdersByOrderIdToRate(@RequestHeader("Authorization") String email, @PathVariable(value="orderId") String orderId){
        String user =  userService.getUserFromToken(email);
        return orderService.getCustomerOrdersByOrderIdToRate(user,orderId);
    }
    @GetMapping("/get/todo/total/{storeId}")
    public TodoListTotal getSellersTodoListTotal(@RequestHeader("Authorization") String email, @PathVariable(value="storeId") String storeId){
        String user =  userService.getUserFromToken(email);
        return orderService.getSellersTodoListTotal(user,storeId);
    }
    @GetMapping("/get/sales/total/{storeId}")
    public TotalSales getTotalSales(@RequestHeader("Authorization") String email, @PathVariable(value="storeId") String storeId) {
        String user =  userService.getUserFromToken(email);
        return orderService.getTotalSales(user,storeId);
    }
    @GetMapping("/count")
    public OrderCount getOrderCountAndTotalSales(@RequestHeader("Authorization") String email) {
        String user =  userService.getUserFromToken(email);
        return orderService.getOrderCountAndTotalSales(user);
    }
    @GetMapping("/all")
    public List<OrderModel> getAllOrders(@RequestHeader("Authorization") String email){
        String user =  userService.getUserFromToken(email);
        return orderService.getAllOrders(user);
    }

}

package com.practice.fullstackbackendspringboot.utils.mapper;

import com.practice.fullstackbackendspringboot.entity.Order;
import com.practice.fullstackbackendspringboot.model.OrderModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderMapper {
    private final ModelMapper mapper = new ModelMapper();

    public OrderModel mapOrderEntityToOrderModel(Order order){
        return mapper.map(order, OrderModel.class);
    }

    public Order mapOrderModelToOrderEntity(OrderModel orderModel){
        return mapper.map(orderModel, Order.class);
    }
}

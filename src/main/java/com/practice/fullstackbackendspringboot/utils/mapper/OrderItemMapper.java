package com.practice.fullstackbackendspringboot.utils.mapper;

import com.practice.fullstackbackendspringboot.entity.Order;
import com.practice.fullstackbackendspringboot.entity.OrderItem;
import com.practice.fullstackbackendspringboot.model.OrderItemModel;
import com.practice.fullstackbackendspringboot.model.OrderModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderItemMapper {

    private final ModelMapper mapper = new ModelMapper();

    public OrderItemModel mapEntityToModel(OrderItem orderItem){
        return mapper.map(orderItem, OrderItemModel.class);
    }

    public OrderItem mapModelToEntity(OrderItemModel orderItemModel){
        return mapper.map(orderItemModel, OrderItem.class);
    }


}

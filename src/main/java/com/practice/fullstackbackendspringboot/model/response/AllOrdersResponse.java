package com.practice.fullstackbackendspringboot.model.response;

import com.practice.fullstackbackendspringboot.model.OrderModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllOrdersResponse {

    private List<OrderModel> orderModel;
}

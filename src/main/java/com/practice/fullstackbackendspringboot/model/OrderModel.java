package com.practice.fullstackbackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderModel {

    private String orderId;

    private Double orderTotalAmount;

    private String paymentMethod;
    private boolean active;
    private String orderStatus;
    private String orderStatusInfo;
    private String deliveryAddress;
    private String fullName;
    private String contactNumber;
    private List<OrderItemModel> orderItemModels;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdDate;

}

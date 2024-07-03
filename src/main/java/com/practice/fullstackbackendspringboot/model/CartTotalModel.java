package com.practice.fullstackbackendspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartTotalModel {

    private Double cartTotal;
    private Long cartItems;
    private Long qty;
    private Double totalShippingFee;
    private Double totalPayment;
}

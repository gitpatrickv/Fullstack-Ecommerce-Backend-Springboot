package com.practice.fullstackbackendspringboot.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateProductRequest {

    private String productId;
    private String orderId;
    private Double rating;
    private String review;
}

package com.practice.fullstackbackendspringboot.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderCount {

    private Double orderCount;
    private Double totalSales;
}

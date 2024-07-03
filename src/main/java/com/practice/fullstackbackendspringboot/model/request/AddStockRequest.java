package com.practice.fullstackbackendspringboot.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddStockRequest {

    private Long inventoryId;
    private Long quantity;
}

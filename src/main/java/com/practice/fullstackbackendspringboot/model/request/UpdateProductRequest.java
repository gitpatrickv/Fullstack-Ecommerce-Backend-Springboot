package com.practice.fullstackbackendspringboot.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {

    private String productId;
    private String productName;
    private String productDescription;
}

package com.practice.fullstackbackendspringboot.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuantityRequest {

    @Valid

    @NotNull(message = "{product.id.must.not.be.null}")
    private String productId;
    @NotNull(message = "{cart.id.must.not.be.null}")
    private String cartId;
}

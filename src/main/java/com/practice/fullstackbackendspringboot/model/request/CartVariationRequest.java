package com.practice.fullstackbackendspringboot.model.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartVariationRequest {

    @Valid

    @NotNull(message = "{inventory.id.must.not.be.null}")
    private String productId;
    @NotNull(message = "{quantity.not.null}")
    private Long quantity;
    @NotNull
    private String colors;
    @NotNull
    private String sizes;

}

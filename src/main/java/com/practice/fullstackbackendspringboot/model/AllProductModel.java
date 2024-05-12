package com.practice.fullstackbackendspringboot.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllProductModel {

    @Valid

    @NotNull(message = "{product.id.must.not.be.null}")
    private String productId;
    @NotBlank(message = "{product.name.required}")
    private String productName;
    @NotNull(message = "{this.field.cannot.be.empty}")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;

    private String photoUrl;

}

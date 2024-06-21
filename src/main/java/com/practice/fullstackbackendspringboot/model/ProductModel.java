package com.practice.fullstackbackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_DEFAULT)
public class ProductModel {

    @Valid

    private String productId;
    @NotBlank(message = "{product.name.required}")
    private String productName;
    @NotBlank(message = "{this.field.cannot.be.empty}")
    private String productDescription;
    @NotNull(message = "{this.field.cannot.be.empty}")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;
    @NotNull(message = "{this.field.cannot.be.empty}")
    private Long quantity;
    private String skuCode;
    private String storeName;
    private List<String> productImage = new ArrayList<>();
    private String storeId;
    private Long categoryId;
}

package com.practice.fullstackbackendspringboot.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {

    @Valid

    private String productId;
    @NotBlank(message = "{product.name.required}")
    private String productName;
    @NotBlank(message = "{this.field.cannot.be.empty}")
    private String productDescription;
    private String storeName;
    private List<String> productImage = new ArrayList<>();
    private String storeId;
    private String categoryId;
    private List<InventoryModel> inventoryModels = new ArrayList<>();
    private Long productSold;
    private String storePhotoUrl;
    private boolean listed;
    private boolean suspended;
}

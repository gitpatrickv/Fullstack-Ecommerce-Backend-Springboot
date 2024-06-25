package com.practice.fullstackbackendspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveProductModel{

    private String productName;
    private String productDescription;
    private String categoryId;
    private List<InventoryModel> inventoryModels = new ArrayList<>();
}

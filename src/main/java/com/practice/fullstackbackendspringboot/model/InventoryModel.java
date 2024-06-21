package com.practice.fullstackbackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(NON_DEFAULT)
public class InventoryModel {

    private Long inventoryId;
    private Long quantity;
    private Double price;
    private String colors;
    private String sizes;
    private String productId;
}

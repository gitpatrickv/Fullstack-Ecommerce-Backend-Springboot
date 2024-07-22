package com.practice.fullstackbackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_DEFAULT;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreModel {
    @Valid
    private String storeId;
    @NotBlank(message = "{store.name.required}")
    private String storeName;
    @NotBlank
    private String storeDescription;
    @NotBlank(message = "{address.required}")
    private String address;
    @NotBlank(message = "{phone.number.required}")
    private String contactNumber;
    private Double shippingFee;
    private String email;
    private String photoUrl;
    private Long productCount;
    private Long orderCount;
    private boolean online;
}

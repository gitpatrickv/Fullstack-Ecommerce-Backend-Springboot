package com.practice.fullstackbackendspringboot.model;

import com.practice.fullstackbackendspringboot.validation.ConfirmPasswordValid;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StoreModel {
    @Valid

    private Long storeId;
    @NotBlank(message = "{store.name.required}")
    private String storeName;
    @NotBlank
    private String storeDescription;
    @NotBlank(message = "{address.required}")
    private String address;
    @NotBlank(message = "{phone.number.required}")
    private String contactNumber;
}

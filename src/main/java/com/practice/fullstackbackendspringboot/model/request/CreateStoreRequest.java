package com.practice.fullstackbackendspringboot.model.request;

import com.practice.fullstackbackendspringboot.validation.UniqueStoreValid;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateStoreRequest {

    @Valid
    @UniqueStoreValid
    @NotBlank
    private String storeName;
    @NotBlank
    private String storeDescription;
    @NotBlank(message = "{address.required}")
    private String address;
    @NotBlank(message = "{phone.number.required}")
    private String contactNumber;
    @NotNull
    private Double shippingFee;
}

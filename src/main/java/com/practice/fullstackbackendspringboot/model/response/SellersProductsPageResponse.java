package com.practice.fullstackbackendspringboot.model.response;

import com.practice.fullstackbackendspringboot.model.AllProductModel;
import com.practice.fullstackbackendspringboot.model.SellersProductModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellersProductsPageResponse {
    private List<SellersProductModel> allProductModels;
    private PageResponse pageResponse;
}

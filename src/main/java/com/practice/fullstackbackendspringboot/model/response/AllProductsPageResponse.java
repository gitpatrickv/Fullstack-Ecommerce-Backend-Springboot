package com.practice.fullstackbackendspringboot.model.response;

import com.practice.fullstackbackendspringboot.model.AllProductModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllProductsPageResponse {

    private List<AllProductModel> allProductModels;
    private PageResponse pageResponse;
}

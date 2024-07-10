package com.practice.fullstackbackendspringboot.utils.mapper;

import com.practice.fullstackbackendspringboot.entity.Product;
import com.practice.fullstackbackendspringboot.model.AllProductModel;
import com.practice.fullstackbackendspringboot.model.SellersProductModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SellersProductMapper {

    private final ModelMapper mapper = new ModelMapper();

    public SellersProductModel mapProductEntityToProductModel(Product product){
        return mapper.map(product, SellersProductModel.class);
    }

    public Product mapProductModelToProductEntity(SellersProductModel allProductModel){
        return mapper.map(allProductModel, Product.class);
    }
}

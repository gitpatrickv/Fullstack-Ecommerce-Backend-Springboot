package com.practice.fullstackbackendspringboot.utils.mapper;

import com.practice.fullstackbackendspringboot.entity.Product;
import com.practice.fullstackbackendspringboot.model.AllProductModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AllProductMapper {
    private final ModelMapper mapper = new ModelMapper();

    public AllProductModel mapProductEntityToProductModel(Product product){
        return mapper.map(product, AllProductModel.class);
    }

    public Product mapProductModelToProductEntity(AllProductModel allProductModel){
        return mapper.map(allProductModel, Product.class);
    }
}

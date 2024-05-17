package com.practice.fullstackbackendspringboot.utils.mapper;

import com.practice.fullstackbackendspringboot.entity.CartTotal;
import com.practice.fullstackbackendspringboot.model.CartTotalModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CartTotalMapper {

    private final ModelMapper mapper = new ModelMapper();

    public CartTotalModel mapEntityToModel(CartTotal cartTotal){
        return mapper.map(cartTotal, CartTotalModel.class);
    }

    public CartTotal mapModelToEntity(CartTotalModel cartTotalModel){
        return mapper.map(cartTotalModel, CartTotal.class);
    }
}

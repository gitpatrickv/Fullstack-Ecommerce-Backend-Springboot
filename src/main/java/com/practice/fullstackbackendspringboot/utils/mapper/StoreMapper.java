package com.practice.fullstackbackendspringboot.utils.mapper;

import com.practice.fullstackbackendspringboot.entity.Store;
import com.practice.fullstackbackendspringboot.model.StoreModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class StoreMapper {

    private final ModelMapper mapper = new ModelMapper();

    public StoreModel mapEntityToModel(Store store){
        return mapper.map(store, StoreModel.class);
    }

    public Store mapModelToEntity(StoreModel storeModel){
        return mapper.map(storeModel, Store.class);
    }
}

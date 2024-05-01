package com.practice.fullstackbackendspringboot.utils.mapper;

import com.practice.fullstackbackendspringboot.entity.Inventory;
import com.practice.fullstackbackendspringboot.model.InventoryModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InventoryMapper {

    private final ModelMapper mapper = new ModelMapper();

    public InventoryModel mapInventoryEntityToInventoryModel(Inventory inventory){
        return mapper.map(inventory, InventoryModel.class);
    }

    public Inventory mapInventoryModelToInventoryEntity(InventoryModel inventoryModel){
    return mapper.map(inventoryModel, Inventory.class);
    }
}

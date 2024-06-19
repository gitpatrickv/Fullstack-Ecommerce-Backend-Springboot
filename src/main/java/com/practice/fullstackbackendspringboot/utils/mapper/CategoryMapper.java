package com.practice.fullstackbackendspringboot.utils.mapper;

import com.practice.fullstackbackendspringboot.entity.Category;
import com.practice.fullstackbackendspringboot.model.CategoryModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CategoryMapper {

    private final ModelMapper mapper = new ModelMapper();

    public CategoryModel mapEntityToModel(Category category){
        return mapper.map(category, CategoryModel.class);
    }

    public Category mapModelToEntity(CategoryModel categoryModel){
        return mapper.map(categoryModel, Category.class);
    }
}

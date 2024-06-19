package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.entity.Category;
import com.practice.fullstackbackendspringboot.model.CategoryModel;

import java.util.List;

public interface CategoryService {

    CategoryModel createCategory(CategoryModel categoryModel);
    CategoryModel findCategoryById(Long categoryId);
    List<CategoryModel>getAllCategory();
}

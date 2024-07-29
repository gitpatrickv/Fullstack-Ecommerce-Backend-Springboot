package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.CategoryModel;
import com.practice.fullstackbackendspringboot.model.request.CategoryRequest;

import java.util.List;

public interface CategoryService {

    List<CategoryModel>getAllCategory();
    void createCategory(String email, CategoryRequest request);
    void updateCategory(String email, CategoryRequest request);
}

package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.CategoryModel;
import com.practice.fullstackbackendspringboot.model.request.CategoryRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {

    List<CategoryModel>getAllCategory();
    void createCategory(CategoryRequest request, MultipartFile file);
    void updateCategory(CategoryRequest request);
}

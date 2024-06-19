package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Category;
import com.practice.fullstackbackendspringboot.model.CategoryModel;
import com.practice.fullstackbackendspringboot.repository.CategoryRepository;
import com.practice.fullstackbackendspringboot.service.CategoryService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    @Override
    public CategoryModel createCategory(CategoryModel categoryModel) {

        Category category;

        if(categoryModel.getCategoryId() != null && categoryRepository.existsById(categoryModel.getCategoryId())){
            category = categoryRepository.findById(categoryModel.getCategoryId()).get();
            category.setCategoryName(category.getCategoryName());
            category.setCategoryPhotoUrl(category.getCategoryPhotoUrl());
        }else{
            category = categoryMapper.mapModelToEntity(categoryModel);
        }
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.mapEntityToModel(savedCategory);

    }

    @Override
    public CategoryModel findCategoryById(Long categoryId) {

        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);

        if(optionalCategory.isPresent()){
            Category category = optionalCategory.get();
            return categoryMapper.mapEntityToModel(category);
        }else{
            throw new NoSuchElementException(StringUtil.CATEGORY_NOT_FOUND);
        }
    }

    @Override
    public List<CategoryModel> getAllCategory() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::mapEntityToModel)
                .toList();
    }


}

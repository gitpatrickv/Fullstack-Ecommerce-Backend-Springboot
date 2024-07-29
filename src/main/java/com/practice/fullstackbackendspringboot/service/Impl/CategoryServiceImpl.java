package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Category;
import com.practice.fullstackbackendspringboot.model.CategoryModel;
import com.practice.fullstackbackendspringboot.model.request.CategoryRequest;
import com.practice.fullstackbackendspringboot.repository.CategoryRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Override
    public List<CategoryModel> getAllCategory() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::mapEntityToModel)
                .toList();
    }

    @Override
    public void createCategory(String email, CategoryRequest request) {
        userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));

        Category category = new Category();
        category.setCategoryName(request.getCategoryName());
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(String email, CategoryRequest request) {
        userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<Category> optionalCategory = categoryRepository.findById(request.getCategoryId());

        if (optionalCategory.isPresent()){
            Category category = optionalCategory.get();
            category.setCategoryName(request.getCategoryName() != null ? request.getCategoryName() : optionalCategory.get().getCategoryName());
            categoryRepository.save(category);
        }
    }
}

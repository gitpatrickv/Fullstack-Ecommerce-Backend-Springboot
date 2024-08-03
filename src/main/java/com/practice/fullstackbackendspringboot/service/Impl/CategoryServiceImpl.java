package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Category;
import com.practice.fullstackbackendspringboot.model.CategoryModel;
import com.practice.fullstackbackendspringboot.model.request.CategoryRequest;
import com.practice.fullstackbackendspringboot.repository.CategoryRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.CategoryService;
import com.practice.fullstackbackendspringboot.service.ImageService;
import com.practice.fullstackbackendspringboot.utils.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private final UserRepository userRepository;
    private final ImageService imageService;

    @Override
    public List<CategoryModel> getAllCategory() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::mapEntityToModel)
                .toList();
    }

    @Override
    public void createCategory(CategoryRequest request, MultipartFile file) {
        Category category = new Category();
        category.setCategoryName(request.getCategoryName());
        Category savedCategory = categoryRepository.save(category);

        imageService.uploadCategoryPhoto(savedCategory.getCategoryId(), file);
    }

    @Override
    public void updateCategory(CategoryRequest request) {
        Optional<Category> optionalCategory = categoryRepository.findById(request.getCategoryId());

        if (optionalCategory.isPresent()){
            Category category = optionalCategory.get();
            category.setCategoryName(request.getCategoryName() != null ? request.getCategoryName() : optionalCategory.get().getCategoryName());
            categoryRepository.save(category);
        }
    }
}

package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.CategoryModel;
import com.practice.fullstackbackendspringboot.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    @PostMapping("/category/add")
    @ResponseStatus(HttpStatus.OK)
    public CategoryModel createCategory(@RequestBody CategoryModel categoryModel) {
        return categoryService.createCategory(categoryModel);
    }
    @GetMapping("/category/get/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public CategoryModel findCategoryById(@PathVariable(value="categoryId") Long categoryId) {
        return categoryService.findCategoryById(categoryId);
    }
    @GetMapping("/category")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryModel> getAllCategory() {
        return categoryService.getAllCategory();
    }

}

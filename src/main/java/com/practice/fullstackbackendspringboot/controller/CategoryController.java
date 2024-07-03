package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.CategoryModel;
import com.practice.fullstackbackendspringboot.service.CategoryService;
import com.practice.fullstackbackendspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;
    @PostMapping("/category/add") //TODO: not yet implemented in the frontend
    @ResponseStatus(HttpStatus.OK)
    public CategoryModel createCategory(@RequestBody CategoryModel categoryModel, @RequestHeader("Authorization") String email) {
        String user = userService.getUserFromToken(email);
        return categoryService.createCategory(categoryModel,user);
    }
    @GetMapping("/category/get/{categoryId}") //TODO: not yet implemented in the frontend
    @ResponseStatus(HttpStatus.OK)
    public CategoryModel findCategoryById(@PathVariable(value="categoryId") String categoryId) {
        return categoryService.findCategoryById(categoryId);
    }
    @GetMapping("/category")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryModel> getAllCategory() {
        return categoryService.getAllCategory();
    }

}

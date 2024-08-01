package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.CategoryModel;
import com.practice.fullstackbackendspringboot.model.request.CategoryRequest;
import com.practice.fullstackbackendspringboot.service.CategoryService;
import com.practice.fullstackbackendspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;

    @GetMapping("/category")
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryModel> getAllCategory() {
        return categoryService.getAllCategory();
    }

    @PostMapping("/category/create")
    public void createCategory(@RequestHeader("Authorization") String email,
                               @RequestPart("category") CategoryRequest request,
                               @RequestPart("file") MultipartFile file) {
        String user = userService.getUserFromToken(email);
        categoryService.createCategory(user,request,file);
    }

    @PutMapping("/category/update")
    public void updateCategory(@RequestHeader("Authorization") String email, @RequestBody CategoryRequest request){
        String user = userService.getUserFromToken(email);
        categoryService.updateCategory(user,request);
    }


}

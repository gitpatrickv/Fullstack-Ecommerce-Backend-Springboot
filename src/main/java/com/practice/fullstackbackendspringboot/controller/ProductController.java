package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.ProductModel;
import com.practice.fullstackbackendspringboot.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/save")
    @ResponseStatus(HttpStatus.OK)
    public ProductModel saveProduct(@RequestBody @Valid ProductModel model){
        return productService.saveProduct(model);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductModel> getAllProducts(){
        return productService.getAllProducts();
    }
}

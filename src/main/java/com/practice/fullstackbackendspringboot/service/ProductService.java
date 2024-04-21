package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.ProductModel;

import java.util.List;

public interface ProductService {

    ProductModel saveProduct(ProductModel model);
    List<ProductModel> getAllProducts();
}

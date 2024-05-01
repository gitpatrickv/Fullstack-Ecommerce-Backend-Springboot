package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.AllProductModel;
import com.practice.fullstackbackendspringboot.model.ProductModel;

import java.util.List;

public interface ProductService {

//    ProductModel saveProduct(ProductModel model);
    List<AllProductModel> getAllProducts();
    ProductModel getProductById(String productId);
    ProductModel saveProduct(ProductModel model, String email);
}

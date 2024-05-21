package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.AllProductModel;
import com.practice.fullstackbackendspringboot.model.ProductModel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductModel saveProduct(ProductModel model, String email, MultipartFile file);
    List<AllProductModel> getAllProducts();
    ProductModel getProductById(String productId);
    void delete(String productId, String email);
}

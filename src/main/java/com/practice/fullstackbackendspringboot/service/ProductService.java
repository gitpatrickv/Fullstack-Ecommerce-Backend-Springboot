package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.AllProductModel;
import com.practice.fullstackbackendspringboot.model.ProductModel;
import com.practice.fullstackbackendspringboot.model.response.AllProductsPageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    ProductModel saveProduct(ProductModel model, String email, MultipartFile file);
    AllProductsPageResponse getAllProducts(int pageNo, int pageSize);
    ProductModel getProductById(String productId);
    void delete(String productId, String email);
    List<AllProductModel> searchProduct(String search);
}

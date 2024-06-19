package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.ProductModel;
import com.practice.fullstackbackendspringboot.model.response.AllProductsPageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ProductModel saveProduct(ProductModel model, String email, MultipartFile file);
    AllProductsPageResponse getAllProducts(int pageNo, int pageSize);
    AllProductsPageResponse getAllStoreProducts(String storeId, int pageNo, int PageSize);
    ProductModel getProductById(String productId);
    void delete(String productId, String email);
    AllProductsPageResponse searchProduct(String search, int pageNo, int pageSize);
    AllProductsPageResponse getAllSellersProducts(String email, int pageNo, int pageSize);
    AllProductsPageResponse getAllProductsByCategory(Long categoryId, int pageNo, int pageSize);


}

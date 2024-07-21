package com.practice.fullstackbackendspringboot.service;

import com.practice.fullstackbackendspringboot.model.ProductModel;
import com.practice.fullstackbackendspringboot.model.SaveProductModel;
import com.practice.fullstackbackendspringboot.model.request.UpdateProductRequest;
import com.practice.fullstackbackendspringboot.model.response.AllProductsPageResponse;
import com.practice.fullstackbackendspringboot.model.response.ProductCount;
import com.practice.fullstackbackendspringboot.model.response.SellersProductsPageResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    void saveProduct(SaveProductModel model, String email, MultipartFile[] files);
    void updateProduct(UpdateProductRequest request, String email);
    AllProductsPageResponse getAllProducts(int pageNo, int pageSize);
    AllProductsPageResponse getAllStoreProducts(String storeId, int pageNo, int PageSize, String sortBy);
    ProductModel getProductById(String productId);
    void delete(String productId, String email);
    AllProductsPageResponse searchProduct(String search, int pageNo, int pageSize, String sortBy);
    SellersProductsPageResponse getAllSellersProducts(String email, int pageNo, int pageSize);
    AllProductsPageResponse getAllProductsByCategory(String categoryId, int pageNo, int pageSize);
    ProductCount getProductCount(String email);

}

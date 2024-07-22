package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.ProductModel;
import com.practice.fullstackbackendspringboot.model.SaveProductModel;
import com.practice.fullstackbackendspringboot.model.request.UpdateProductRequest;
import com.practice.fullstackbackendspringboot.model.response.AllProductsPageResponse;
import com.practice.fullstackbackendspringboot.model.response.ProductCount;
import com.practice.fullstackbackendspringboot.model.response.SellersProductsPageResponse;
import com.practice.fullstackbackendspringboot.model.response.StoreResponse;
import com.practice.fullstackbackendspringboot.service.ProductService;
import com.practice.fullstackbackendspringboot.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;

    @PostMapping(value = {"/save"},  consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseStatus(HttpStatus.OK)
    public void saveProduct(@RequestPart("product") SaveProductModel model,
                                        @RequestPart("file") MultipartFile[] files,
                                        @RequestHeader("Authorization") String email){
        String user = userService.getUserFromToken(email);
        productService.saveProduct(model,user,files);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@RequestBody @Valid UpdateProductRequest request,
                                     @RequestHeader("Authorization") String email){
        String user = userService.getUserFromToken(email);
        productService.updateProduct(request,user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AllProductsPageResponse getAllProducts(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                 @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize){
        return productService.getAllProducts(pageNo,pageSize);
    }
    @GetMapping("/store/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public StoreResponse getAllStoreProducts(@PathVariable (value="storeId") String storeId,
                                             @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                             @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
                                             @RequestParam(defaultValue = "productName", required = false) String sortBy){
        return productService.getAllStoreProducts(storeId,pageNo,pageSize, sortBy);
    }

    @GetMapping("/store")
    @ResponseStatus(HttpStatus.OK)
    public SellersProductsPageResponse getAllSellersProducts(@RequestHeader("Authorization") String email,
                                                             @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                             @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize){
        String user = userService.getUserFromToken(email);
        return productService.getAllSellersProducts(user,pageNo,pageSize);
    }
    @GetMapping("/category/{categoryId}")
    @ResponseStatus(HttpStatus.OK)
    public AllProductsPageResponse getAllProductsByCategory(@PathVariable(value="categoryId") String categoryId,
                                                            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                            @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize){
        return productService.getAllProductsByCategory(categoryId,pageNo,pageSize);
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public ProductModel getProductById(@PathVariable (value="productId") String productId){
        return productService.getProductById(productId);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public AllProductsPageResponse searchProduct(@RequestParam (value = "keyword") String search,
                                                 @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                 @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
                                                 @RequestParam(defaultValue = "productName", required = false) String sortBy){
        return productService.searchProduct(search, pageNo, pageSize, sortBy);
    }
    @DeleteMapping("/delete/{productId}")
    public void delete(@PathVariable (value="productId", required = false) String productId, @RequestHeader("Authorization") String email){
        String user = userService.getUserFromToken(email);
        productService.delete(productId, user);
    }
    @GetMapping("/count")
    public ProductCount getProductCount(@RequestHeader("Authorization") String email) {
        String user = userService.getUserFromToken(email);
        return productService.getProductCount(user);
    }

}

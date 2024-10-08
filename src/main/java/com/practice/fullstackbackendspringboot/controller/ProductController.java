package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.ProductModel;
import com.practice.fullstackbackendspringboot.model.SaveProductModel;
import com.practice.fullstackbackendspringboot.model.request.UpdateProductRequest;
import com.practice.fullstackbackendspringboot.model.response.*;
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
                                        @RequestPart("file") MultipartFile[] files){
        String user = userService.getAuthenticatedUser();
        productService.saveProduct(model,user,files);
    }

    @PutMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public void updateProduct(@RequestBody @Valid UpdateProductRequest request){
        productService.updateProduct(request);
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
    public SellersProductsPageResponse getAllSellersProducts(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                             @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
                                                             @RequestParam(defaultValue = "productSold", required = false) String sortBy){
        String user = userService.getAuthenticatedUser();
        return productService.getAllSellersProducts(user,pageNo,pageSize, sortBy);
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
    public void delete(@PathVariable (value="productId", required = false) String productId){
        productService.delete(productId);
    }
    @GetMapping("/count")
    public ProductCount getProductCount() {
        return productService.getProductCount();
    }
    @PutMapping("/suspend/{productId}")
    public void suspendProduct(@PathVariable String productId) {
        String user = userService.getAuthenticatedUser();
        productService.suspendProduct(productId,user);
    }
    @GetMapping("/count/{storeId}")
    public SuspendedProductCount getSuspendedProductCount(@PathVariable String storeId){
        return productService.getSuspendedProductCount(storeId);
    }
    @PutMapping("/delist/{productId}")
    public void delistProduct(@PathVariable String productId) {
        productService.delistProduct(productId);

    }

}

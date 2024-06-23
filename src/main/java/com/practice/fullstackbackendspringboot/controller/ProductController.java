package com.practice.fullstackbackendspringboot.controller;

import com.practice.fullstackbackendspringboot.model.ProductModel;
import com.practice.fullstackbackendspringboot.model.response.AllProductsPageResponse;
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
    public ProductModel saveProduct( @RequestPart("product") ProductModel model,
                                     @RequestPart("file") MultipartFile file,
                                     @RequestHeader("Authorization") String email){
        String user = userService.getUserFromToken(email);
        return productService.saveProduct(model,user,file);
    }

    @PostMapping("/update")
    @ResponseStatus(HttpStatus.OK)
    public ProductModel updateProduct( @RequestPart("product") @Valid ProductModel model,
                                     @RequestHeader("Authorization") String email){
        String user = userService.getUserFromToken(email);
        return productService.updateProduct(model,user);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AllProductsPageResponse getAllProducts(@RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                 @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize){
        return productService.getAllProducts(pageNo,pageSize);
    }
    @GetMapping("/store/{storeId}")
    @ResponseStatus(HttpStatus.OK)
    public AllProductsPageResponse getAllStoreProducts(@PathVariable (value="storeId") String storeId,
                                                       @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                       @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize){
        return productService.getAllStoreProducts(storeId,pageNo,pageSize);
    }

    @GetMapping("/store")
    @ResponseStatus(HttpStatus.OK)
    public AllProductsPageResponse getAllSellersProducts(@RequestHeader("Authorization") String email,
                                                       @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
                                                       @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize){
        String user = userService.getUserFromToken(email);
        return productService.getAllSellersProducts(user,pageNo,pageSize);
    }
    @GetMapping("/category/{categoryId}") //TODO: not yet implemented in the frontend
    @ResponseStatus(HttpStatus.OK)
    public AllProductsPageResponse getAllProductsByCategory(@PathVariable(value="categoryId") Long categoryId,
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
                                                 @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize){
        return productService.searchProduct(search, pageNo, pageSize);
    }
    @DeleteMapping("/delete/{productId}")
    public void delete(@PathVariable (value="productId") String productId, @RequestHeader("Authorization") String email){
        String user = userService.getUserFromToken(email);
        productService.delete(productId, user);
    }

}

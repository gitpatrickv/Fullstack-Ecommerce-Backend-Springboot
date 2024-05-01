package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Inventory;
import com.practice.fullstackbackendspringboot.entity.Product;
import com.practice.fullstackbackendspringboot.entity.ProductImage;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.AllProductModel;
import com.practice.fullstackbackendspringboot.model.ProductModel;
import com.practice.fullstackbackendspringboot.repository.InventoryRepository;
import com.practice.fullstackbackendspringboot.repository.ProductImageRepository;
import com.practice.fullstackbackendspringboot.repository.ProductRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.security.JwtAuthenticationFilter;
import com.practice.fullstackbackendspringboot.service.InventoryService;
import com.practice.fullstackbackendspringboot.service.ProductImageService;
import com.practice.fullstackbackendspringboot.service.ProductService;
import com.practice.fullstackbackendspringboot.service.UserService;
import com.practice.fullstackbackendspringboot.utils.StringUtils;
import com.practice.fullstackbackendspringboot.utils.mapper.AllProductMapper;
import com.practice.fullstackbackendspringboot.utils.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final ProductMapper mapper;
    private final ProductImageRepository productImageRepository;
    private final UserService userService;
    private final InventoryService inventoryService;
    private final AllProductMapper allProductMapper;
    private final ProductImageService productImageService;

    @Override
    public ProductModel saveProduct(ProductModel model, String email) {
        boolean isNew = productRepository.existsById(model.getProductId());
        Product product;
//        String userEmail = userService.getUserFromToken(email);
        User user = userRepository.findByEmail(email).get();

        if(!isNew) {
            product = mapper.mapProductModelToProductEntity(model);
            product.setUser(user);
        } else {
            product = productRepository.findById(model.getProductId()).get();
            if (model.getShopName() != null) {
                product.setShopName(model.getShopName());
            }
            if (model.getProductName() != null) {
                product.setProductName(model.getProductName());
            }
            if(model.getProductDescription() != null){
                product.setProductDescription(model.getProductDescription());
            }
        }

        Product savedProduct = productRepository.save(product);

        boolean isExists = inventoryRepository.existsByProduct_ProductId(savedProduct.getProductId());
        if (!isExists) {
            Inventory inventory = Inventory.builder()
                    .product(savedProduct)
                    .price(model.getPrice())
                    .quantity(model.getQuantity())
                    .build();
            inventoryRepository.save(inventory);
        }
        return mapper.mapProductEntityToProductModel(savedProduct);
    }

    @Override
    public List<AllProductModel> getAllProducts() {

        List<Product> products = productRepository.findAll();
        List<AllProductModel> productModels = new ArrayList<>();

        for(Product product : products) {
            AllProductModel allProductModel = allProductMapper.mapProductEntityToProductModel(product);
            getPhotoUrl(product, allProductModel);
            getPriceAndQuantity(product, allProductModel);
            productModels.add(allProductModel);
        }
        return productModels;
    }

    @Override
    public ProductModel getProductById(String productId) {
        Optional<Product> product = productRepository.findById(productId);
        Product products = product.orElseThrow(() -> new NoSuchElementException(StringUtils.PRODUCT_NOT_FOUND + productId));
        Inventory inventory = inventoryRepository.findByProduct_ProductId(productId).get();
        List<ProductImage> productImages = productImageRepository.findAllByProduct_ProductId(productId);
        List<String> photoUrls = new ArrayList<>();

        ProductModel productModel = mapper.mapProductEntityToProductModel(products);

        for(ProductImage productImage : productImages){
            photoUrls.add(productImage.getPhotoUrl());
        }
        productModel.setProductImage(photoUrls);
        productModel.setPrice(inventory.getPrice());
        productModel.setQuantity(inventory.getQuantity());

        return productModel;
    }

    //TODO: refactor to avoid doing database reads one at a time in a loop it is better to use jpa @Query
    private void getPhotoUrl(Product product, AllProductModel productModel){
        List<ProductImage> productImages = product.getProductImage();
        if (productImages != null && !productImages.isEmpty()) {
            ProductImage image = productImages.get(0);
            productModel.setPhotoUrl(image.getPhotoUrl());
        }
    }

    private void getPriceAndQuantity(Product product, AllProductModel productModel){
        List<Inventory> inventories = product.getInventory();
        if(inventories != null && !inventories.isEmpty()){
            Inventory inventory = inventories.get(0);
            productModel.setPrice(inventory.getPrice());
        }
    }

}









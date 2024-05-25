package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.*;
import com.practice.fullstackbackendspringboot.model.AllProductModel;
import com.practice.fullstackbackendspringboot.model.ProductModel;
import com.practice.fullstackbackendspringboot.repository.*;
import com.practice.fullstackbackendspringboot.service.InventoryService;
import com.practice.fullstackbackendspringboot.service.ProductImageService;
import com.practice.fullstackbackendspringboot.service.ProductService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.AllProductMapper;
import com.practice.fullstackbackendspringboot.utils.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final ProductMapper mapper;
    private final ProductImageRepository productImageRepository;
    private final AllProductMapper allProductMapper;
    private final ProductImageService productImageService;
    private final InventoryService inventoryService;
    private final StoreRepository storeRepository;

    @Transactional(rollbackOn = Exception.class)
    @Override
    public ProductModel saveProduct(ProductModel model, String email, MultipartFile file) { //TODO: remove productID on productmodel
        boolean isNew = productRepository.existsById(model.getProductId());
        Product product;

        User user = userRepository.findByEmail(email).get();
        Store store = storeRepository.findByUserEmail(email).get();
        if(!isNew) {
            product = mapper.mapProductModelToProductEntity(model);
            product.setUser(user);
            product.setStore(store);
        } else {
            product = productRepository.findById(model.getProductId()).get();

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
            inventoryRepository.save(inventory);  //@TODO: implement item variation
        }

        productImageService.uploadPhoto(savedProduct.getProductId(),file);      //@TODO: Refactor to upload multiple images at the same time

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
    public List<AllProductModel> searchProduct(String search) {

        List<Product> products = productRepository.findByProductNameContainingIgnoreCaseOrStore_StoreNameContainingIgnoreCase(search,search);
        List<AllProductModel> productModels = new ArrayList<>();

        for(Product product : products) {
            AllProductModel allProductModel = allProductMapper.mapProductEntityToProductModel(product);
            getPhotoUrl(product, allProductModel);
            getPriceAndQuantity(product,allProductModel);
            productModels.add(allProductModel);
        }
        return productModels;
    }

    @Override
    public ProductModel getProductById(String productId) {
        Optional<Product> product = productRepository.findById(productId);
        Product products = product.orElseThrow(() -> new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND + productId));
        Inventory inventory = inventoryRepository.findByProduct_ProductId(productId).get();
        List<ProductImage> productImages = productImageRepository.findAllPhotoUrlByProduct_ProductId(productId);
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

    @Override
    public void delete(String productId, String email) {
        userRepository.findByEmail(email);
        productRepository.deleteById(productId);
    }

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









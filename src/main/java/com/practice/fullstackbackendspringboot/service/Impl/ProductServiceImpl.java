package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Inventory;
import com.practice.fullstackbackendspringboot.entity.Product;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.ProductModel;
import com.practice.fullstackbackendspringboot.repository.InventoryRepository;
import com.practice.fullstackbackendspringboot.repository.ProductImageRepository;
import com.practice.fullstackbackendspringboot.repository.ProductRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.security.JwtAuthenticationFilter;
import com.practice.fullstackbackendspringboot.service.ProductService;
import com.practice.fullstackbackendspringboot.utils.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

    @Override
    public ProductModel saveProduct(ProductModel model) {
        boolean isNew = productRepository.existsById(model.getProductId());
        Product product;
        String username = JwtAuthenticationFilter.CURRENT_USER;
        User user = userRepository.findById(username).get();

        if (!isNew) {
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
            if (model.getProductDescription() != null) {
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
}




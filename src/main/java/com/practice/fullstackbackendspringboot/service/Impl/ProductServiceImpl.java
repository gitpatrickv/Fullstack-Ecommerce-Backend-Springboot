package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.*;
import com.practice.fullstackbackendspringboot.model.AllProductModel;
import com.practice.fullstackbackendspringboot.model.InventoryModel;
import com.practice.fullstackbackendspringboot.model.ProductModel;
import com.practice.fullstackbackendspringboot.model.SaveProductModel;
import com.practice.fullstackbackendspringboot.model.response.AllProductsPageResponse;
import com.practice.fullstackbackendspringboot.model.response.PageResponse;
import com.practice.fullstackbackendspringboot.repository.*;
import com.practice.fullstackbackendspringboot.service.ImageService;
import com.practice.fullstackbackendspringboot.service.InventoryService;
import com.practice.fullstackbackendspringboot.service.ProductService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.AllProductMapper;
import com.practice.fullstackbackendspringboot.utils.mapper.InventoryMapper;
import com.practice.fullstackbackendspringboot.utils.mapper.ProductMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional(rollbackOn = Exception.class)
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final UserRepository userRepository;
    private final ProductMapper mapper;
    private final ImageRepository imageRepository;
    private final AllProductMapper allProductMapper;
    private final ImageService imageService;
    private final InventoryService inventoryService;
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryMapper inventoryMapper;

    @Override
    public void saveProduct(SaveProductModel model, String email, MultipartFile file) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Store store = storeRepository.findByUserEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.STORE_NOT_FOUND + email));
        Category category = categoryRepository.findById(model.getCategoryId()).orElseThrow(() -> new NoSuchElementException(StringUtil.CATEGORY_NOT_FOUND));

        Product product = new Product();
        product.setProductName(model.getProductName());
        product.setProductDescription(model.getProductDescription());
        product.setUser(user);
        product.setStore(store);
        product.setCategory(category);

            List<Inventory> inventories = new ArrayList<>();
            for(InventoryModel inv : model.getInventoryModels()) {
                Inventory inventory = new Inventory();
                inventory.setProduct(product);
                inventory.setPrice(inv.getPrice());
                inventory.setColors(inv.getColors());
                inventory.setSizes(inv.getSizes());
                inventory.setQuantity(inv.getQuantity());
                inventories.add(inventory);

            }
            product.setInventory(inventories);
            Product savedProduct = productRepository.save(product);

        imageService.uploadProductPhoto(savedProduct.getProductId(),file);
    }

    @Override
    public ProductModel getProductById(String productId) {
        Optional<Product> product = productRepository.findById(productId);
        Product products = product.orElseThrow(() -> new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND + productId));
        List<Image> images = imageRepository.findAllPhotoUrlByProduct_ProductId(productId);
        List<Inventory> inventories = inventoryRepository.findAllByProduct_ProductId(productId);

        List<InventoryModel> inventoryModels = new ArrayList<>();
        List<String> photoUrls = new ArrayList<>();

        ProductModel productModel = mapper.mapProductEntityToProductModel(products);

        for(Inventory inventory : inventories){
            InventoryModel inventoryModel = inventoryMapper.mapInventoryEntityToInventoryModel(inventory);
            inventoryModels.add(inventoryModel);
        }

        for(Image image : images){
            photoUrls.add(image.getPhotoUrl());
        }
        productModel.setInventoryModels(inventoryModels);
        productModel.setProductImage(photoUrls);
        productModel.setStoreId(products.getStore().getStoreId());
        return productModel;
    }

    @Override
    public ProductModel updateProduct(ProductModel model, String email) {  //TODO: not yet implemented in the frontend
        Product product = productRepository.findById(model.getProductId())
                .orElseThrow(() -> new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND));
        Inventory inventory = inventoryRepository.findByProduct_ProductId(model.getProductId())
                .orElseThrow(() -> new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND));

        if (model.getProductName() != null) {
            product.setProductName(model.getProductName());
        }
        if(model.getProductDescription() != null){
            product.setProductDescription(model.getProductDescription());
        }
        if(model.getPrice() != null){
            inventory.setPrice(model.getPrice());
        }
        if(model.getQuantity() != null){
            inventory.setQuantity(model.getQuantity());
        }

        productRepository.save(product);
        inventoryRepository.save(inventory);

        return mapper.mapProductEntityToProductModel(product);
    }

    @Override
    public AllProductsPageResponse getAllProducts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Product> products = productRepository.findAll(pageable);
        List<AllProductModel> productModels = new ArrayList<>();

        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(products.getNumber());
        pageResponse.setPageSize(products.getSize());
        pageResponse.setTotalElements(products.getTotalElements());
        pageResponse.setTotalPages(products.getTotalPages());
        pageResponse.setLast(products.isLast());

        for (Product product : products) {
            AllProductModel allProductModel = allProductMapper.mapProductEntityToProductModel(product);
            allProductModel.setStoreId(product.getStore().getStoreId());
            getPhotoUrl(product, allProductModel);
            getPriceAndQuantity(product, allProductModel);
            productModels.add(allProductModel);
        }

        return new AllProductsPageResponse(productModels, pageResponse);
    }

    @Override
    public AllProductsPageResponse getAllProductsByCategory(String categoryId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Product> products = productRepository.findAllByCategory_CategoryId(categoryId,pageable);
        List<AllProductModel> productModels = new ArrayList<>();

        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(products.getNumber());
        pageResponse.setPageSize(products.getSize());
        pageResponse.setTotalElements(products.getTotalElements());
        pageResponse.setTotalPages(products.getTotalPages());
        pageResponse.setLast(products.isLast());

        for (Product product : products) {
            AllProductModel allProductModel = allProductMapper.mapProductEntityToProductModel(product);
            allProductModel.setStoreId(product.getStore().getStoreId());
            getPhotoUrl(product, allProductModel);
            getPriceAndQuantity(product, allProductModel);
            allProductModel.setCategoryName(product.getCategory().getCategoryName());
            productModels.add(allProductModel);
        }
        return new AllProductsPageResponse(productModels, pageResponse);
    }

    @Override
    public AllProductsPageResponse getAllStoreProducts(String storeId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Product> products = productRepository.findAllByStore_StoreId(storeId, pageable);
        List<AllProductModel> productModels = new ArrayList<>();

        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(products.getNumber());
        pageResponse.setPageSize(products.getSize());
        pageResponse.setTotalElements(products.getTotalElements());
        pageResponse.setTotalPages(products.getTotalPages());
        pageResponse.setLast(products.isLast());

        for(Product product : products){
            AllProductModel allProductModel = allProductMapper.mapProductEntityToProductModel(product);
            getPhotoUrl(product, allProductModel);
            getPriceAndQuantity(product, allProductModel);
            allProductModel.setStoreName(product.getStore().getStoreName());
            productModels.add(allProductModel);
        }
        return new AllProductsPageResponse(productModels, pageResponse);
    }

    @Override
    public AllProductsPageResponse searchProduct(String search, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo,pageSize);
        Page<Product> products = productRepository.findByProductNameContainingIgnoreCaseOrStore_StoreNameContainingIgnoreCase(search,search, pageable);
        List<AllProductModel> productModels = new ArrayList<>();

        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(products.getNumber());
        pageResponse.setPageSize(products.getSize());
        pageResponse.setTotalElements(products.getTotalElements());
        pageResponse.setTotalPages(products.getTotalPages());
        pageResponse.setLast(products.isLast());

        for(Product product : products) {
            AllProductModel allProductModel = allProductMapper.mapProductEntityToProductModel(product);
            getPhotoUrl(product, allProductModel);
            getPriceAndQuantity(product,allProductModel);
            productModels.add(allProductModel);
        }
        return new AllProductsPageResponse(productModels, pageResponse);
    }

    @Override
    public AllProductsPageResponse getAllSellersProducts(String email, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Optional<User> user = userRepository.findByEmail(email);
        Page<Product> products = productRepository.findAllByUserEmail(user.get().getEmail(), pageable);
        List<AllProductModel> productModels = new ArrayList<>();

        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(products.getNumber());
        pageResponse.setPageSize(products.getSize());
        pageResponse.setTotalElements(products.getTotalElements());
        pageResponse.setTotalPages(products.getTotalPages());
        pageResponse.setLast(products.isLast());

        for(Product product : products){
            AllProductModel allProductModel = allProductMapper.mapProductEntityToProductModel(product);
            getPhotoUrl(product, allProductModel);
            getPriceAndQuantity(product, allProductModel);
            allProductModel.setStoreName(product.getStore().getStoreName());
            productModels.add(allProductModel);
        }
        return new AllProductsPageResponse(productModels, pageResponse);
    }

    @Override
    public void delete(String productId, String email) {
        userRepository.findByEmail(email);
        productRepository.deleteById(productId);
    }

    private void getPhotoUrl(Product product, AllProductModel productModel){
        List<Image> images = product.getImage();
        if (images != null && !images.isEmpty()) {
            Image image = images.get(0);
            productModel.setPhotoUrl(image.getPhotoUrl());
        }
    }

    private void getPriceAndQuantity(Product product, AllProductModel productModel){
        List<Inventory> inventories = product.getInventory();
        if(inventories != null && !inventories.isEmpty()){
            Inventory inventory = inventories.get(0);
            productModel.setPrice(inventory.getPrice());
            productModel.setQuantity(inventory.getQuantity());
        }
    }

}









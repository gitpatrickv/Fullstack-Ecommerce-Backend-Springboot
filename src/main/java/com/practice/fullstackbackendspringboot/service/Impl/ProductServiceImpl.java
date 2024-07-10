package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.*;
import com.practice.fullstackbackendspringboot.model.AllProductModel;
import com.practice.fullstackbackendspringboot.model.InventoryModel;
import com.practice.fullstackbackendspringboot.model.ProductModel;
import com.practice.fullstackbackendspringboot.model.SaveProductModel;
import com.practice.fullstackbackendspringboot.model.request.UpdateProductRequest;
import com.practice.fullstackbackendspringboot.model.response.AllProductsPageResponse;
import com.practice.fullstackbackendspringboot.model.response.PageResponse;
import com.practice.fullstackbackendspringboot.repository.*;
import com.practice.fullstackbackendspringboot.service.ImageService;
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

import java.util.*;
import java.util.stream.Collectors;

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
    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryMapper inventoryMapper;
    private final FavoritesRepository favoritesRepository;

    @Override
    public void saveProduct(SaveProductModel model, String email, MultipartFile[] files) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Store store = storeRepository.findByUserEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.STORE_NOT_FOUND + email));
        Category category = categoryRepository.findById(model.getCategoryId()).orElseThrow(() -> new NoSuchElementException(StringUtil.CATEGORY_NOT_FOUND));

        Product product = new Product();
        product.setProductName(model.getProductName());
        product.setProductDescription(model.getProductDescription());
        product.setUser(user);
        product.setStore(store);
        product.setCategory(category);

            Set<Inventory> inventories = new HashSet<>();

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

        imageService.uploadProductPhoto(savedProduct.getProductId(),files);
    }

    @Override
    public ProductModel getProductById(String productId) {
        Product products = productRepository.findById(productId).orElseThrow(() -> new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND + productId));
        List<Image> images = imageRepository.findAllPhotoUrlByProduct_ProductId(productId);
        Set<Inventory> inventories = inventoryRepository.findAllByProduct_ProductId(productId);

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
    public void updateProduct(UpdateProductRequest request, String email) {
        userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND));

        if (request.getProductName() != null) {
            product.setProductName(request.getProductName());
        }
        if(request.getProductDescription() != null){
            product.setProductDescription(request.getProductDescription());
        }
        productRepository.save(product);

    }

    @Override
    public AllProductsPageResponse getAllProducts(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<Product> products = productRepository.findAllByDeletedFalse(pageable);
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
        Page<Product> products = productRepository.findAllByDeletedFalseAndCategory_CategoryId(categoryId,pageable);
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
        Page<Product> products = productRepository.findAllByDeletedFalseAndStore_StoreId(storeId, pageable);
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
    public AllProductsPageResponse searchProduct(String search, int pageNo, int pageSize, String sortBy) {
        Sort sort = Sort.by("productName").ascending();

        if("productSold".equals(sortBy)){
            sort = Sort.by("productSold").descending();
        }
        if("createdDate".equals(sortBy)){
            sort = Sort.by("createdDate").descending();
        }

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Product> products = productRepository.findByDeletedFalseAndProductNameContainingIgnoreCaseOrStore_StoreNameContainingIgnoreCase(search,search, pageable);

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
        Page<Product> products = productRepository.findAllByDeletedFalseAndUserEmail(user.get().getEmail(), pageable);
        Sort sort = Sort.by(Sort.Direction.DESC, "colors");
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

            Set<Inventory> inventories = inventoryRepository.findAllByProduct_ProductId(product.getProductId(), sort);
            Set<InventoryModel> inv = inventories.stream()
                    .map(inventoryMapper::mapInventoryEntityToInventoryModel)
                    .collect(Collectors.toSet());

            allProductModel.setInventoryModels(inv);
            productModels.add(allProductModel);
        }
        return new AllProductsPageResponse(productModels, pageResponse);
    }

    @Override
    public void delete(String productId, String email) {
        userRepository.findByEmail(email);
        Optional<Product> product = productRepository.findById(productId);

        if(product.isPresent()){
            Product prod = product.get();
            prod.setDeleted(true);
        }

        favoritesRepository.deleteAllByProduct_ProductId(productId);
    }

    private void getPhotoUrl(Product product, AllProductModel productModel){
        List<Image> images = product.getImage();
        if (images != null && !images.isEmpty()) {
            Image image = images.get(0);
            productModel.setPhotoUrl(image.getPhotoUrl());
        }
    }

    private void getPriceAndQuantity(Product product, AllProductModel productModel){
        Set<Inventory> inventories = product.getInventory();
        if(inventories != null && !inventories.isEmpty()){
            Inventory inventory = inventories.iterator().next();
            productModel.setPrice(inventory.getPrice());
            productModel.setQuantity(inventory.getQuantity());
        }
    }

}









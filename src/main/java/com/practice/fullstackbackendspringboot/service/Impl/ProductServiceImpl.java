package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.*;
import com.practice.fullstackbackendspringboot.model.*;
import com.practice.fullstackbackendspringboot.model.request.UpdateProductRequest;
import com.practice.fullstackbackendspringboot.model.response.*;
import com.practice.fullstackbackendspringboot.repository.*;
import com.practice.fullstackbackendspringboot.service.ImageService;
import com.practice.fullstackbackendspringboot.service.ProductService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.AllProductMapper;
import com.practice.fullstackbackendspringboot.utils.mapper.InventoryMapper;
import com.practice.fullstackbackendspringboot.utils.mapper.ProductMapper;
import com.practice.fullstackbackendspringboot.utils.mapper.SellersProductMapper;
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
    private final SellersProductMapper sellersProductMapper;

    @Override
    public void saveProduct(SaveProductModel model, String email, MultipartFile[] files) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Store store = storeRepository.findByUserEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.STORE_NOT_FOUND + email));
        Category category = categoryRepository.findById(model.getCategoryId()).orElseThrow(() -> new NoSuchElementException(StringUtil.CATEGORY_NOT_FOUND));

        Product product = new Product();
        product.setProductName(model.getProductName());
        product.setProductDescription(model.getProductDescription());
        product.setListed(true);
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

        store.setProductCount(store.getProductCount() + 1L);
        storeRepository.save(store);
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
        productModel.setStorePhotoUrl(products.getStore().getPhotoUrl());
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
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, StringUtil.Created_Date));
        Page<Product> products = productRepository.findAllByDeletedFalseAndListedTrueAndSuspendedFalse(pageable);
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
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, StringUtil.Created_Date));
        Page<Product> products = productRepository.findAllByDeletedFalseAndListedTrueAndSuspendedFalseAndCategory_CategoryId(categoryId,pageable);
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
    public StoreResponse getAllStoreProducts(String storeId, int pageNo, int pageSize, String sortBy) {
        Sort sort = Sort.by(StringUtil.Product_Name).ascending();

        if(StringUtil.Product_Sold.equals(sortBy)){
            sort = Sort.by(StringUtil.Product_Sold).descending();
        } else if(StringUtil.Created_Date.equals(sortBy)){
            sort = Sort.by(StringUtil.Created_Date).descending();
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> products = productRepository.findAllByDeletedFalseAndStore_StoreId(storeId, pageable);
        Store store = storeRepository.findById(storeId).get();
        List<AllProductModel> productModels = new ArrayList<>();

        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(products.getNumber());
        pageResponse.setPageSize(products.getSize());
        pageResponse.setTotalElements(products.getTotalElements());
        pageResponse.setTotalPages(products.getTotalPages());
        pageResponse.setLast(products.isLast());

        StoreInfo storeInfo = new StoreInfo();
        storeInfo.setStoreName(store.getStoreName());
        storeInfo.setStorePhotoUrl(store.getPhotoUrl());
        storeInfo.setOnline(store.isOnline());

        for(Product product : products){
            AllProductModel allProductModel = allProductMapper.mapProductEntityToProductModel(product);
            getPhotoUrl(product, allProductModel);
            getPriceAndQuantity(product, allProductModel);
            productModels.add(allProductModel);
        }
        return new StoreResponse(productModels, storeInfo, pageResponse);
    }

    @Override
    public AllProductsPageResponse searchProduct(String search, int pageNo, int pageSize, String sortBy) {
        Sort sort = Sort.by(StringUtil.Product_Name).ascending();

        if(StringUtil.Product_Sold.equals(sortBy)){
            sort = Sort.by(StringUtil.Product_Sold).descending();
        } else if(StringUtil.Created_Date.equals(sortBy)){
            sort = Sort.by(StringUtil.Created_Date).descending();
        }

        Pageable pageable = PageRequest.of(pageNo,pageSize, sort);
        Page<Product> products = productRepository.findByDeletedFalseAndListedTrueAndSuspendedFalseAndProductNameContainingIgnoreCaseOrDeletedFalseAndListedTrueAndSuspendedFalseAndStore_StoreNameContainingIgnoreCase(search,search, pageable);

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
    public SellersProductsPageResponse getAllSellersProducts(String email, int pageNo, int pageSize, String sortBy) {
        Sort sorts = Sort.by(StringUtil.Product_Sold).descending();

        if(StringUtil.True.equals(sortBy)){
            sorts = Sort.by(StringUtil.Listed).ascending();
        } else if(StringUtil.False.equals(sortBy)){
            sorts = Sort.by(StringUtil.Listed).descending();
        } else if(StringUtil.Low_Product_Sold.equals(sortBy)){
            sorts = Sort.by(StringUtil.Product_Sold).ascending();
        } else if(StringUtil.Suspended.equals(sortBy)) {
            sorts = Sort.by(StringUtil.Suspended).descending();
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, sorts);
        Optional<User> user = userRepository.findByEmail(email);
        Page<Product> products = productRepository.findAllByDeletedFalseAndUserEmail(user.get().getEmail(), pageable);

        Sort sort = Sort.by(Sort.Direction.DESC, StringUtil.Color);
        List<SellersProductModel> productModels = new ArrayList<>();

        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(products.getNumber());
        pageResponse.setPageSize(products.getSize());
        pageResponse.setTotalElements(products.getTotalElements());
        pageResponse.setTotalPages(products.getTotalPages());
        pageResponse.setLast(products.isLast());

        for(Product product : products){
            SellersProductModel allProductModel = sellersProductMapper.mapProductEntityToProductModel(product);
            allProductModel.setPrice(product.getInventory().iterator().next().getPrice());
            allProductModel.setQuantity(product.getInventory().iterator().next().getQuantity());
            allProductModel.setPhotoUrl(product.getImage().get(0).getPhotoUrl());
            allProductModel.setStoreName(product.getStore().getStoreName());

            Set<Inventory> inventories = inventoryRepository.findAllByProduct_ProductId(product.getProductId(), sort);
            List<InventoryModel> inv = inventories.stream()
                    .map(inventoryMapper::mapInventoryEntityToInventoryModel)
                    .collect(Collectors.toList());

            allProductModel.setInventoryModels(inv);
            productModels.add(allProductModel);
        }
        return new SellersProductsPageResponse(productModels, pageResponse);
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

    @Override
    public ProductCount getProductCount(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        double count = productRepository.count();
        ProductCount productCount = new ProductCount();
        productCount.setProductCount(count);
        return productCount;
    }

    @Override
    public void suspendProduct(String productId, String email) {
        userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND));

        product.setSuspended(!product.isSuspended());
        productRepository.save(product);
    }

    @Override
    public SuspendedProductCount getSuspendedProductCount(String storeId, String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));

        double count = productRepository.findAllBySuspendedTrueAndStore_StoreId(storeId).stream().count();

        SuspendedProductCount suspendedProductCount = new SuspendedProductCount();
        suspendedProductCount.setSuspendedProductCount(count);

        return suspendedProductCount;
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









package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Product;
import com.practice.fullstackbackendspringboot.entity.Store;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.StoreModel;
import com.practice.fullstackbackendspringboot.model.request.CreateStoreRequest;
import com.practice.fullstackbackendspringboot.model.request.UpdateShopInfoRequest;
import com.practice.fullstackbackendspringboot.model.response.StoreCount;
import com.practice.fullstackbackendspringboot.repository.CartRepository;
import com.practice.fullstackbackendspringboot.repository.ProductRepository;
import com.practice.fullstackbackendspringboot.repository.StoreRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.StoreService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final StoreMapper mapper;

    @Override
    public void createStore(CreateStoreRequest request, String email){
        Optional<User> user = userRepository.findByEmail(email);
        boolean isExists = storeRepository.existsByStoreNameIgnoreCase(request.getStoreName());

        if(user.isPresent() && !isExists){
            Store store = new Store();
            store.setStoreName(request.getStoreName());
            store.setStoreDescription(request.getStoreDescription());
            store.setAddress(request.getAddress());
            store.setContactNumber(request.getContactNumber());
            store.setShippingFee(request.getShippingFee());
            store.setOnline(true);
            store.setUser(user.get());
            storeRepository.save(store);
        }
    }

    @Override
    public StoreModel getStoreInfo(String email) {
        Store store = storeRepository.findByUserEmail(email).get();
        StoreModel storeModel = mapper.mapEntityToModel(store);
        storeModel.setEmail(store.getUser().getEmail());
        return storeModel;
    }

    @Override
    public void updateShopInfo(String email, String storeId, UpdateShopInfoRequest request) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.STORE_NOT_FOUND + storeId));

        store.setStoreName(request.getStoreName() != null ? request.getStoreName() : store.getStoreName());
        store.setStoreDescription(request.getStoreDescription() != null ? request.getStoreDescription() : store.getStoreDescription());
        store.setAddress(request.getAddress() != null ? request.getAddress() :  store.getAddress());
        store.setContactNumber(request.getContactNumber() != null ? request.getContactNumber() : store.getContactNumber());
        store.setShippingFee(request.getShippingFee() != null ? request.getShippingFee() : store.getShippingFee());
        storeRepository.save(store);
    }

    @Override
    public List<StoreModel> getAllStores(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        return storeRepository.findAll()
                .stream()
                .map(store -> {
                    StoreModel storeModel = mapper.mapEntityToModel(store);
                    storeModel.setEmail(store.getUser().getEmail());
                    return storeModel;
                })
                .toList();
    }

    @Override
    public StoreCount getStoreCount(String email) {
        userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        double count = storeRepository.count();
        StoreCount storeCount = new StoreCount();
        storeCount.setStoreCount(count);
        return storeCount;
    }

    @Override
    public void suspendStoreAndProductListing(String storeId, String email) {
        List<Product> products = productRepository.findAllByDeletedFalseAndStore_StoreId(storeId);
        Optional<Store> store = storeRepository.findById(storeId);

        boolean suspendProducts = products.stream().allMatch(Product::isSuspended);
        boolean toggleSuspend = !suspendProducts;

        for(Product product : products){
            product.setSuspended(toggleSuspend);
            productRepository.save(product);
        }

        if (store.isPresent()) {
            Store store1 = store.get();
            store1.setOnline(!store1.isOnline());
            storeRepository.save(store1);
        }

    }
}










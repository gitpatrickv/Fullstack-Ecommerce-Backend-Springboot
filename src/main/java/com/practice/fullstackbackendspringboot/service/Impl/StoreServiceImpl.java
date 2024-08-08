package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Product;
import com.practice.fullstackbackendspringboot.entity.Store;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.entity.constants.Role;
import com.practice.fullstackbackendspringboot.model.StoreModel;
import com.practice.fullstackbackendspringboot.model.request.CreateStoreRequest;
import com.practice.fullstackbackendspringboot.model.request.UpdateShopInfoRequest;
import com.practice.fullstackbackendspringboot.model.response.PageResponse;
import com.practice.fullstackbackendspringboot.model.response.PaginateStoreResponse;
import com.practice.fullstackbackendspringboot.model.response.StoreCount;
import com.practice.fullstackbackendspringboot.repository.CartRepository;
import com.practice.fullstackbackendspringboot.repository.ProductRepository;
import com.practice.fullstackbackendspringboot.repository.StoreRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.StoreService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.StoreMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        boolean storeExists = storeRepository.existsByStoreNameIgnoreCase(request.getStoreName());

        if(!storeExists) {
            Store store = new Store();
            store.setStoreName(request.getStoreName());
            store.setStoreDescription(request.getStoreDescription());
            store.setAddress(request.getAddress());
            store.setContactNumber(request.getContactNumber());
            store.setShippingFee(request.getShippingFee());
            store.setOnline(true);
            store.setUser(user);
            Store savedStore = storeRepository.save(store);

            if(savedStore.isOnline()) {
                if (user.getRole().equals(Role.USER)) {
                    user.setRole(Role.SELLER);
                    userRepository.save(user);
                }
            }
        }
    }

    @Override
    public StoreModel getStoreInfo(String email) {
        Store store = storeRepository.findByUserEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.STORE_NOT_FOUND + email));
            StoreModel storeModel = mapper.mapEntityToModel(store);
            storeModel.setEmail(store.getUser().getEmail());
            return storeModel;
    }

    @Override
    public void updateShopInfo(String storeId, UpdateShopInfoRequest request) {

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
    public PaginateStoreResponse getAllStores(int pageNo, int pageSize, String sortBy) {

        Sort sorts = Sort.by(StringUtil.Online).descending();

        if (StringUtil.False.equals(sortBy)) {
            sorts = Sort.by(StringUtil.Online).ascending();
        }

        Pageable pageable = PageRequest.of(pageNo, pageSize, sorts);
        Page<Store> stores = storeRepository.findAll(pageable);
        List<StoreModel> storeModels = new ArrayList<>();

        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(stores.getNumber());
        pageResponse.setPageSize(stores.getSize());
        pageResponse.setTotalElements(stores.getTotalElements());
        pageResponse.setTotalPages(stores.getTotalPages());
        pageResponse.setLast(stores.isLast());

        for(Store store : stores) {
            StoreModel storeModel = mapper.mapEntityToModel(store);
            storeModel.setEmail(store.getUser().getEmail());
            storeModels.add(storeModel);
        }
        return new PaginateStoreResponse(storeModels,pageResponse);
    }

    @Override
    public StoreCount getStoreCount() {
        double count = storeRepository.count();
        StoreCount storeCount = new StoreCount();
        storeCount.setStoreCount(count);
        return storeCount;
    }

    @Override
    public void suspendStoreAndProductListing(String storeId, String email) {
        User admin = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        List<Product> products = productRepository.findAllByDeletedFalseAndStore_StoreId(storeId);
        Optional<Store> store = storeRepository.findById(storeId);

        if(!admin.getRole().equals(Role.ADMIN)) {
            throw new AccessDeniedException(StringUtil.ACCESS_DENIED);
        }

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










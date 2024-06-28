package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Store;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.StoreModel;
import com.practice.fullstackbackendspringboot.model.request.CreateStoreRequest;
import com.practice.fullstackbackendspringboot.repository.StoreRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.StoreService;
import com.practice.fullstackbackendspringboot.utils.mapper.StoreMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class StoreServiceImpl implements StoreService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
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
}










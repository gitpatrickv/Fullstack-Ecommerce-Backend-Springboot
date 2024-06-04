package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Store;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.StoreModel;
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
    public StoreModel createStore(StoreModel storeModel, String email) {
        Optional<User> user = userRepository.findByEmail(email);
        Optional<Store> existingStore = storeRepository.findByUserEmail(email);
        boolean isNew = storeRepository.existsByStoreNameIgnoreCase(storeModel.getStoreName());
        Store store;

        if (user.isPresent() && !isNew) {
            if (existingStore.isPresent()) {
                store = existingStore.get();
            } else {
                store = mapper.mapModelToEntity(storeModel);
                store.setUser(user.get());
            }

            if (storeModel.getStoreDescription() != null) {
                store.setStoreDescription(storeModel.getStoreDescription());
            }
            if (storeModel.getAddress() != null) {
                store.setAddress(storeModel.getAddress());
            }
            if (storeModel.getContactNumber() != null) {
                store.setContactNumber(storeModel.getContactNumber());
            }
            if (storeModel.getStoreName() != null) {
                store.setStoreName(storeModel.getStoreName());
            }
            Store saveStore = storeRepository.save(store);
            return mapper.mapEntityToModel(saveStore);
        } else {
            throw new IllegalArgumentException();
        }
    }
}










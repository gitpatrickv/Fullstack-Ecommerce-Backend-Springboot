package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Store;
import com.practice.fullstackbackendspringboot.entity.StoreFollower;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.response.FollowedStore;
import com.practice.fullstackbackendspringboot.model.response.StoreFollowerCount;
import com.practice.fullstackbackendspringboot.repository.StoreFollowerRepository;
import com.practice.fullstackbackendspringboot.repository.StoreRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.StoreFollowerService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class StoreFollowerServiceImpl implements StoreFollowerService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final StoreFollowerRepository storeFollowerRepository;

    @Override
    public void followStore(String storeId, String email) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NoSuchElementException(StringUtil.STORE_NOT_FOUND + storeId));
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<StoreFollower> storeFollowerOptional = storeFollowerRepository.findByStore_StoreIdAndUserEmail(storeId,email);

        if(storeFollowerOptional.isPresent()){
            StoreFollower storeFollower = storeFollowerOptional.get();
            storeFollowerRepository.delete(storeFollower);
        }else {
            StoreFollower storeFollower = new StoreFollower();
            storeFollower.setStore(store);
            storeFollower.setUser(user);
            storeFollower.setFollowed(true);
            storeFollowerRepository.save(storeFollower);
        }
    }

    @Override
    public FollowedStore getFollowedStatus(String storeId, String email) {
        Optional<StoreFollower> storeFollowerOptional = storeFollowerRepository.findByStore_StoreIdAndUserEmail(storeId,email);
        FollowedStore followedStore = new FollowedStore();

        followedStore.setFollowed(false);

        if(storeFollowerOptional.isPresent()) {
            StoreFollower storeFollower = storeFollowerOptional.get();
            followedStore.setFollowed(storeFollower.isFollowed());
        }

        return followedStore;
    }

    @Override
    public StoreFollowerCount getStoreFollowerCount(String storeId) {
        long count = storeFollowerRepository.findAllByStore_StoreId(storeId).stream().count();
        StoreFollowerCount storeFollowerCount = new StoreFollowerCount();
        storeFollowerCount.setStoreFollowerCount(count);
        return storeFollowerCount;
    }
}

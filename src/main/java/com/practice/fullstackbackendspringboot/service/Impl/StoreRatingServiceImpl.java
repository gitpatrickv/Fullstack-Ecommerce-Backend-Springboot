package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Order;
import com.practice.fullstackbackendspringboot.entity.Store;
import com.practice.fullstackbackendspringboot.entity.StoreRating;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.request.RateStoreRequest;
import com.practice.fullstackbackendspringboot.model.response.TotalStoreRating;
import com.practice.fullstackbackendspringboot.repository.*;
import com.practice.fullstackbackendspringboot.service.StoreRatingService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StoreRatingServiceImpl implements StoreRatingService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final StoreRatingRepository storeRatingRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public void rateStore(String email, RateStoreRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Optional<Order> optionalOrder = orderRepository.findByOrderIdAndStoreRatedFalse(request.getOrderId());

        if(request.getRating() < 1 || request.getRating() > 5){
            throw new IllegalArgumentException(StringUtil.RATING_EXCEEDS_LIMIT);
        }

        if(optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            Store store = storeRepository.findById(order.getStore().getStoreId())
                    .orElseThrow(() -> new NoSuchElementException(StringUtil.STORE_NOT_FOUND));
            StoreRating storeRating = new StoreRating();
            storeRating.setRating(request.getRating());
            storeRating.setStore(store);
            storeRatingRepository.save(storeRating);

            order.setStoreRated(true);
            orderRepository.save(order);
        }
        this.checkStatus(request.getOrderId(), user.getEmail());
    }

    @Override
    public TotalStoreRating getStoreRatingCount(String storeId) {
        List<StoreRating> storeRatings = storeRatingRepository.findAllByStore_StoreId(storeId);
        Store store = storeRepository.findById(storeId).get();
        double totalRatingCount = 0.0;
        double rating = 0.0;

        for(StoreRating storeRating : storeRatings){
            double ratingCount = 1.0;
            totalRatingCount+=ratingCount;

            double ratingTotal = storeRating.getRating();
            rating += ratingTotal;
        }

        double avg = rating / totalRatingCount;
        Double roundedAvg = Math.round(avg * 10.0) / 10.0;

        TotalStoreRating totalStoreRating = new TotalStoreRating();
        totalStoreRating.setStoreTotalRating(totalRatingCount);
        totalStoreRating.setStoreRatingAvg(roundedAvg);
        totalStoreRating.setProductCount(store.getProductCount());
        return totalStoreRating;
    }

    public void checkStatus(String orderId, String email){
        Boolean isExists = orderItemRepository.existsAllByRatedFalseAndOrder_OrderIdAndUserEmail(orderId, email);
        Boolean isStoreRated = orderRepository.existsByStoreRatedFalseAndOrderId(orderId);

        if(!isExists && !isStoreRated){
            Order order = orderRepository.findById(orderId).get();
            order.setOrderStatus(StringUtil.RATED);
            orderRepository.save(order);
        }
    }
}

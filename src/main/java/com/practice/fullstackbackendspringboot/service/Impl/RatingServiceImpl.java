package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Product;
import com.practice.fullstackbackendspringboot.entity.Rating;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.RatingModel;
import com.practice.fullstackbackendspringboot.model.request.RateProductRequest;
import com.practice.fullstackbackendspringboot.repository.ProductRepository;
import com.practice.fullstackbackendspringboot.repository.RatingRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.RatingService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class RatingServiceImpl implements RatingService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final RatingRepository ratingRepository;

    @Override   //TODO: not yet implemented in the frontend
    public void rateProduct(String email, RateProductRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND + request.getProductId()));
        boolean isExists = ratingRepository.existsByUserEmailAndProduct_ProductId(email, request.getProductId());

        if(request.getRating() > 5){
            throw new IllegalArgumentException(StringUtil.RATING_EXCEEDS_MAXIMUM);
        }

        if(!isExists){
            Rating rating = new Rating();
            rating.setRating(request.getRating());
            rating.setProduct(product);
            rating.setUser(user);
            ratingRepository.save(rating);
        }
    }

    @Override
    public RatingModel getProductRatingAverage(String productId) {
        List<Rating> ratings = ratingRepository.findAllByProduct_ProductId(productId);
        RatingModel ratingModel;
        Double ratingTotal = 0.0;
        double totalNumberOfUserRating = 0.0;

        for(Rating rating : ratings){
            Double rate = rating.getRating();
            ratingTotal += rate;

            double numberOfUser = 1.0;
            totalNumberOfUserRating += numberOfUser;
        }

        double avg = ratingTotal / totalNumberOfUserRating;
        Double roundedAvg = Math.round(avg * 10.0) / 10.0;

        ratingModel = new RatingModel();
        ratingModel.setRatingAverage(roundedAvg);
        ratingModel.setTotalNumberOfUserRating(totalNumberOfUserRating);
        ratingModel.setProductId(productId);
        return ratingModel;
    }

}

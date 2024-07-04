package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Product;
import com.practice.fullstackbackendspringboot.entity.RatingAndReview;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.RatingAndReviewModel;
import com.practice.fullstackbackendspringboot.model.request.RatingAverageRequest;
import com.practice.fullstackbackendspringboot.model.request.RateProductRequest;
import com.practice.fullstackbackendspringboot.repository.ProductRepository;
import com.practice.fullstackbackendspringboot.repository.RatingAndReviewRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.RatingAndReviewService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.RatingAndReviewMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
@Transactional(rollbackOn = Exception.class)
public class RatingAndReviewServiceImpl implements RatingAndReviewService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final RatingAndReviewRepository ratingAndReviewRepository;
    private final RatingAndReviewMapper ratingAndReviewMapper;

    @Override   //TODO: not yet implemented in the frontend
    public void rateAndReviewProduct(String email, RateProductRequest request) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new NoSuchElementException(StringUtil.USER_NOT_FOUND + email));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new NoSuchElementException(StringUtil.PRODUCT_NOT_FOUND + request.getProductId()));
        boolean isExists = ratingAndReviewRepository.existsByUserEmailAndProduct_ProductId(email, request.getProductId());

        if(request.getRating() > 5){
            throw new IllegalArgumentException(StringUtil.RATING_EXCEEDS_MAXIMUM);
        }

        if(!isExists){
            RatingAndReview rating = new RatingAndReview();
            rating.setRating(request.getRating());
            rating.setReview(request.getReview());
            rating.setProduct(product);
            rating.setUser(user);
            ratingAndReviewRepository.save(rating);
        }
    }

    @Override
    public RatingAverageRequest getProductRatingAverage(String productId) {
        List<RatingAndReview> ratings = ratingAndReviewRepository.findAllByProduct_ProductId(productId);
        Double ratingTotal = 0.0;
        double totalNumberOfUserRating = 0.0;

        for(RatingAndReview rating : ratings){
            Double rate = rating.getRating();
            ratingTotal += rate;

            double numberOfUser = 1.0;
            totalNumberOfUserRating += numberOfUser;
        }

        double avg = ratingTotal / totalNumberOfUserRating;
        Double roundedAvg = Math.round(avg * 10.0) / 10.0;

        RatingAverageRequest ratingAverageRequest = new RatingAverageRequest();
        ratingAverageRequest.setRatingAverage(roundedAvg);
        ratingAverageRequest.setTotalNumberOfUserRating(totalNumberOfUserRating);
        ratingAverageRequest.setProductId(productId);
        return ratingAverageRequest;
    }

    @Override
    public List<RatingAndReviewModel> getAllRatingAndReview(String productId, Double rating, String status) {
        List<RatingAndReview> ratingAndReviews = ratingAndReviewRepository.findAllByProduct_ProductId(productId);
        List<RatingAndReviewModel> ratingAndReviewModelList = new ArrayList<>();

        for(RatingAndReview ratingAndReview : ratingAndReviews){
            if(ratingAndReview.getRating().equals(rating) || status.isEmpty()) {
                RatingAndReviewModel ratingAndReviewModel = ratingAndReviewMapper.mapEntityToModel(ratingAndReview);
                ratingAndReviewModel.setName(ratingAndReview.getUser().getName());
                ratingAndReviewModel.setPhotoUrl(ratingAndReview.getUser().getPhotoUrl());
                ratingAndReviewModel.setCreatedDate(ratingAndReview.getCreatedDate());
                ratingAndReviewModelList.add(ratingAndReviewModel);
            }
        }

        return ratingAndReviewModelList;

    }

}

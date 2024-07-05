package com.practice.fullstackbackendspringboot.service.Impl;

import com.practice.fullstackbackendspringboot.entity.Product;
import com.practice.fullstackbackendspringboot.entity.RatingAndReview;
import com.practice.fullstackbackendspringboot.entity.User;
import com.practice.fullstackbackendspringboot.model.RatingAndReviewModel;
import com.practice.fullstackbackendspringboot.model.request.RateProductRequest;
import com.practice.fullstackbackendspringboot.model.response.NumberOfUserRatingResponse;
import com.practice.fullstackbackendspringboot.model.response.PageResponse;
import com.practice.fullstackbackendspringboot.model.response.RatingAndReviewResponse;
import com.practice.fullstackbackendspringboot.model.response.RatingAverageResponse;
import com.practice.fullstackbackendspringboot.repository.ProductRepository;
import com.practice.fullstackbackendspringboot.repository.RatingAndReviewRepository;
import com.practice.fullstackbackendspringboot.repository.UserRepository;
import com.practice.fullstackbackendspringboot.service.RatingAndReviewService;
import com.practice.fullstackbackendspringboot.utils.StringUtil;
import com.practice.fullstackbackendspringboot.utils.mapper.RatingAndReviewMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public RatingAverageResponse getProductRatingAverage(String productId) {
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

        RatingAverageResponse ratingAverageResponse = new RatingAverageResponse();
        ratingAverageResponse.setRatingAverage(roundedAvg);
        ratingAverageResponse.setTotalNumberOfUserRating(totalNumberOfUserRating);
        ratingAverageResponse.setProductId(productId);
        return ratingAverageResponse;
    }

    @Override
    public RatingAndReviewResponse getAllRatingAndReview(String productId, int pageNo, int pageSize, Double rating) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<RatingAndReview> ratingAndReviews = ratingAndReviewRepository.findAllByRatingAndProduct_ProductId(rating, productId, pageable);
        List<RatingAndReviewModel> ratingAndReviewModelList = new ArrayList<>();

        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(ratingAndReviews.getNumber());
        pageResponse.setPageSize(ratingAndReviews.getSize());
        pageResponse.setTotalElements(ratingAndReviews.getTotalElements());
        pageResponse.setTotalPages(ratingAndReviews.getTotalPages());
        pageResponse.setLast(ratingAndReviews.isLast());

        for(RatingAndReview ratingAndReview : ratingAndReviews){
            if(ratingAndReview.getRating().equals(rating)) {
                RatingAndReviewModel ratingAndReviewModel = ratingAndReviewMapper.mapEntityToModel(ratingAndReview);
                ratingAndReviewModel.setName(ratingAndReview.getUser().getName());
                ratingAndReviewModel.setPhotoUrl(ratingAndReview.getUser().getPhotoUrl());
                ratingAndReviewModel.setCreatedDate(ratingAndReview.getCreatedDate());
                ratingAndReviewModelList.add(ratingAndReviewModel);
            }
        }
        return new RatingAndReviewResponse(ratingAndReviewModelList, pageResponse);
    }

    @Override
    public RatingAndReviewResponse getAllRating(String productId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "createdDate"));
        Page<RatingAndReview> ratingAndReviews = ratingAndReviewRepository.findAllByProduct_ProductId(productId, pageable);
        List<RatingAndReviewModel> ratingAndReviewModelList = new ArrayList<>();

        PageResponse pageResponse = new PageResponse();
        pageResponse.setPageNo(ratingAndReviews.getNumber());
        pageResponse.setPageSize(ratingAndReviews.getSize());
        pageResponse.setTotalElements(ratingAndReviews.getTotalElements());
        pageResponse.setTotalPages(ratingAndReviews.getTotalPages());
        pageResponse.setLast(ratingAndReviews.isLast());

        for(RatingAndReview ratingAndReview : ratingAndReviews){
            RatingAndReviewModel ratingAndReviewModel = ratingAndReviewMapper.mapEntityToModel(ratingAndReview);
            ratingAndReviewModel.setName(ratingAndReview.getUser().getName());
            ratingAndReviewModel.setPhotoUrl(ratingAndReview.getUser().getPhotoUrl());
            ratingAndReviewModel.setCreatedDate(ratingAndReview.getCreatedDate());
            ratingAndReviewModelList.add(ratingAndReviewModel);
        }
        return new RatingAndReviewResponse(ratingAndReviewModelList, pageResponse);
    }

    @Override
    public NumberOfUserRatingResponse getTotalUserRating(String productId) {
        List<RatingAndReview> ratingAndReviews = ratingAndReviewRepository.findAllByProduct_ProductId(productId);
        double allStarRating = 0.0;
        double total5StarRating = 0.0;
        double total4StarRating = 0.0;
        double total3StarRating = 0.0;
        double total2StarRating = 0.0;
        double total1StarRating = 0.0;

        for(RatingAndReview ratingAndReview : ratingAndReviews){
            if(ratingAndReview.getRating().equals(5.0)){
                double numberOfUser = 1.0;
                total5StarRating += numberOfUser;
            }

            if(ratingAndReview.getRating().equals(4.0)){
                double numberOfUser = 1.0;
                total4StarRating += numberOfUser;
            }

            if(ratingAndReview.getRating().equals(3.0)){
                double numberOfUser = 1.0;
                total3StarRating += numberOfUser;
            }

            if(ratingAndReview.getRating().equals(2.0)){
                double numberOfUser = 1.0;
                total2StarRating += numberOfUser;
            }

            if(ratingAndReview.getRating().equals(1.0)){
                double numberOfUser = 1.0;
                total1StarRating += numberOfUser;
            }

            double numberOfUser = 1.0;
            allStarRating += numberOfUser;
        }
        NumberOfUserRatingResponse numberOfUserRatingResponse = new NumberOfUserRatingResponse();
        numberOfUserRatingResponse.setOverallTotalUserRating(allStarRating);
        numberOfUserRatingResponse.setTotal5StarUserRating(total5StarRating);
        numberOfUserRatingResponse.setTotal4StarUserRating(total4StarRating);
        numberOfUserRatingResponse.setTotal3StarUserRating(total3StarRating);
        numberOfUserRatingResponse.setTotal2StarUserRating(total2StarRating);
        numberOfUserRatingResponse.setTotal1StarUserRating(total1StarRating);

        return numberOfUserRatingResponse;
    }
}

package com.practice.fullstackbackendspringboot.utils.mapper;

import com.practice.fullstackbackendspringboot.entity.RatingAndReview;
import com.practice.fullstackbackendspringboot.model.RatingAndReviewModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RatingAndReviewMapper {

    private final ModelMapper mapper = new ModelMapper();

    public RatingAndReviewModel mapEntityToModel(RatingAndReview ratingAndReview){
        return mapper.map(ratingAndReview, RatingAndReviewModel.class);
    }

    public RatingAndReview mapModelToEntity(RatingAndReviewModel ratingAndReviewModel){
        return mapper.map(ratingAndReviewModel, RatingAndReview.class);
    }
}

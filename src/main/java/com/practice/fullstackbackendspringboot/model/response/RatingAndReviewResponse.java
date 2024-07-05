package com.practice.fullstackbackendspringboot.model.response;

import com.practice.fullstackbackendspringboot.model.RatingAndReviewModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingAndReviewResponse {

    private List<RatingAndReviewModel> ratingAndReviewModels;
    private PageResponse pageResponse;
}

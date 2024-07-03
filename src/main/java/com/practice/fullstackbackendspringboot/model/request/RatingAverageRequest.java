package com.practice.fullstackbackendspringboot.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingAverageRequest {

    private String productId;
    private Double totalNumberOfUserRating;
    private Double ratingAverage;

}

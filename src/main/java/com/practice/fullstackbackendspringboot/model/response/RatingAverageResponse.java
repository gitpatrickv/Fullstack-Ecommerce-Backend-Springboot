package com.practice.fullstackbackendspringboot.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingAverageResponse {

    private String productId;
    private Double totalNumberOfUserRating;
    private Double ratingAverage;

}

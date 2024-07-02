package com.practice.fullstackbackendspringboot.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingModel {

    private String productId;
    private Double totalNumberOfUserRating;
    private Double ratingAverage;

}

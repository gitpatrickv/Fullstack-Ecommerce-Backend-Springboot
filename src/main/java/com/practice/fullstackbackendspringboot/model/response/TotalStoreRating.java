package com.practice.fullstackbackendspringboot.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalStoreRating {

    private Double storeTotalRating;
    private Double storeRatingAvg;
    private Long productCount;
}

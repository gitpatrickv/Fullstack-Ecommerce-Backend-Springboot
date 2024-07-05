package com.practice.fullstackbackendspringboot.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NumberOfUserRatingResponse {
    private Double overallTotalUserRating;
    private Double total1StarUserRating;
    private Double total2StarUserRating;
    private Double total3StarUserRating;
    private Double total4StarUserRating;
    private Double total5StarUserRating;
}

package com.practice.fullstackbackendspringboot.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalStoreRating {

    private Double storeTotalRating;
    private Double storeRatingAvg;
    private Long productCount;
    @JsonFormat(pattern = "yyyy")
    private LocalDateTime createdDate;
}

package com.practice.fullstackbackendspringboot.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingAndReviewModel {

    private Long reviewId;
    private Double rating;
    private String review;
    private String sellersReply;
    private String name;
    private String photoUrl;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdDate;
    private String productName;
    private String productPhotoUrl;
    private String storeId;
    private boolean replied;
}

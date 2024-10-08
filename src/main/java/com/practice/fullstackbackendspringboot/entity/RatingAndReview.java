package com.practice.fullstackbackendspringboot.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "rating_and_review")
public class RatingAndReview extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long reviewId;

    private Double rating;
    @Column(length = 1000)
    private String review;
    @Column(length = 1000)
    private String sellersReply;
    private String storeId;
    private boolean replied;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;
}

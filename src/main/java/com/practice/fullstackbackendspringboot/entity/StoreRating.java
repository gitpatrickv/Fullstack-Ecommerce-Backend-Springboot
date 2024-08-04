package com.practice.fullstackbackendspringboot.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "store_rating")
public class StoreRating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long storeRatingId;

    private Double rating;

    @ManyToOne
    @JoinColumn(name="store_id")
    private Store store;

}

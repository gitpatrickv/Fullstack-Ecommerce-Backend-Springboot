package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.RatingAndReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingAndReviewRepository extends JpaRepository<RatingAndReview, Long> {

    Boolean existsByUserEmailAndProduct_ProductId(String email, String productId);
    List<RatingAndReview> findAllByProduct_ProductId(String productId);
}

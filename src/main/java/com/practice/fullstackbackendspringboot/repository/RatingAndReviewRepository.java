package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.RatingAndReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingAndReviewRepository extends JpaRepository<RatingAndReview, Long> {

    Boolean existsByUserEmailAndProduct_ProductId(String email, String productId);
    List<RatingAndReview> findAllByProduct_ProductId(String productId);
    Page<RatingAndReview> findAllByRatingAndProduct_ProductId(Double rating, String productId, Pageable pageable);
    Page<RatingAndReview> findAllByProduct_ProductId(String productId, Pageable pageable);
    Page<RatingAndReview> findAllByStoreId(String storeId, Pageable pageable);
    Optional<RatingAndReview> findByReviewIdAndStoreId(Long reviewId, String storeId);

}

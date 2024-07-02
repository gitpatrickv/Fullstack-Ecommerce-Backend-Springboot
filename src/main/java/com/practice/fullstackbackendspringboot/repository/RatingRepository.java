package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {

    Boolean existsByUserEmailAndProduct_ProductId(String email, String productId);
    List<Rating> findAllByProduct_ProductId(String productId);
}

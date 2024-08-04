package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.StoreRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreRatingRepository extends JpaRepository<StoreRating, Long> {

    List<StoreRating> findAllByStore_StoreId(String storeId);
}

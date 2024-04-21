package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.ProductVariation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductVariationRepository extends JpaRepository<ProductVariation, Long> {

}

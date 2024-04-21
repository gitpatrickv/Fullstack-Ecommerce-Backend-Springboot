package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String> {

}


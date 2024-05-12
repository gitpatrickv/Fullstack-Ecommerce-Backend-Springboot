package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String> {

    List<ProductImage> findAllPhotoUrlByProduct_ProductId(String productId);
    Optional<ProductImage> findByProduct_ProductId(String productId);
}


package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductImageRepository extends JpaRepository<ProductImage, String> {

    List<ProductImage> findAllByProduct_ProductId(String productId);
}


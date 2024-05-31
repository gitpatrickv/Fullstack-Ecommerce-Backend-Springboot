package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductImageRepository extends JpaRepository<Image, String> {

    List<Image> findAllPhotoUrlByProduct_ProductId(String productId);
    Optional<Image> findByProduct_ProductId(String productId);
}


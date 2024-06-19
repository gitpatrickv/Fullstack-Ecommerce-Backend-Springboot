package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByProductNameContainingIgnoreCaseOrStore_StoreNameContainingIgnoreCase(String search, String search1, Pageable pageable);
    Page<Product> findAllByStore_StoreId(String storeId, Pageable pageable);
    Page<Product> findAllByUserEmail(String email, Pageable pageable);
    Page<Product> findAllByCategory_CategoryId(Long categoryId, Pageable pageable);
}

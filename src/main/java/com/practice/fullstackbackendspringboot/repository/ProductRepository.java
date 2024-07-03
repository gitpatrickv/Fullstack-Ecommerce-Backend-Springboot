package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByDeletedFalseAndProductNameContainingIgnoreCaseOrStore_StoreNameContainingIgnoreCase(String search, String search1, Pageable pageable);
    Page<Product> findAllByDeletedFalseAndStore_StoreId(String storeId, Pageable pageable);
    Page<Product> findAllByDeletedFalseAndUserEmail(String email, Pageable pageable);
    Page<Product> findAllByDeletedFalseAndCategory_CategoryId(String categoryId, Pageable pageable);
    Page<Product> findAllByDeletedFalse(Pageable pageable);

}

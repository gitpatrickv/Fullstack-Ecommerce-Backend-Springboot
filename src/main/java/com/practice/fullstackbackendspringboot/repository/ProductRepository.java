package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Page<Product> findByDeletedFalseAndListedTrueAndSuspendedFalseAndProductNameContainingIgnoreCaseOrDeletedFalseAndListedTrueAndSuspendedFalseAndStore_StoreNameContainingIgnoreCase(String search, String search1, Pageable pageable);
    Page<Product> findAllByDeletedFalseAndListedTrueAndStore_StoreId(String storeId, Pageable pageable);
    Page<Product> findAllByDeletedFalseAndUserEmail(String email, Pageable pageable);
    Page<Product> findAllByDeletedFalseAndListedTrueAndSuspendedFalseAndCategory_CategoryId(String categoryId, Pageable pageable);
    Page<Product> findAllByDeletedFalseAndListedTrueAndSuspendedFalse(Pageable pageable);
    List<Product> findAllByDeletedFalseAndListedTrueAndSuspendedFalseAndStore_StoreId(String storeId);
    List<Product> findAllByDeletedFalseAndStore_StoreId(String storeId);
    List<Product> findAllBySuspendedTrueAndStore_StoreId(String storeId);

}

package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.Inventory;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProduct_ProductId(String productId);
    Set<Inventory> findAllByProduct_ProductId(String productId);
    Set<Inventory> findAllByProduct_ProductId(String productId, Sort sort);
    Optional<Inventory> findByColorsAndSizesAndProduct_ProductId(String color, String size, String productId);
}

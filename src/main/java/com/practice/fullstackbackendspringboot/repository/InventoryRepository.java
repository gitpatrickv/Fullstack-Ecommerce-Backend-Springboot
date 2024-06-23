package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProduct_ProductId(String productId);
    List<Inventory> findAllByProduct_ProductId(String productId);
    Optional<Inventory> findByColorsAndSizesAndProduct_ProductId(String color, String size, String productId);
}

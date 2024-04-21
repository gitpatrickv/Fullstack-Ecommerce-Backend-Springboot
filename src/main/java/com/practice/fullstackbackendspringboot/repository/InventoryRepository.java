package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Boolean existsByProduct_ProductId(String productId);
}

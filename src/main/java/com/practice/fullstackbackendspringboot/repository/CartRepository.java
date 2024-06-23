package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    Optional<Cart> findByProduct_ProductIdAndUserEmail(String productId, String email);
    Optional<Cart> findByProduct_ProductIdAndInventory_InventoryIdAndUserEmail(String productId, Long inventoryId, String email);
    Optional<Cart> findByColorsAndSizesAndProduct_ProductIdAndUserEmail(String colors, String sizes, String productId, String email);
    List<Cart> findAllByFilterAndUserEmail(boolean filter,String email);
    Optional<Cart> findByCartIdAndUserEmail(String cartId, String email);
    List<Cart> findAllByUserEmail(String email);
    List<Cart> findAllByStoreNameIgnoreCaseAndUserEmail(String storeName, String email);
    Optional<Cart> deleteByCartIdAndUserEmail(String cartId, String email);
    List<Cart> deleteAllByFilterTrueAndUserEmail(String email);
    List<Cart> findAllByFilterTrueAndUserEmail(String email);
    List<Cart> findAllByFilterTrueAndUserEmailOrderByCreatedDateDesc(String email);
    List<Cart> findAllByUserEmailOrderByCreatedDateDesc(String email);
}

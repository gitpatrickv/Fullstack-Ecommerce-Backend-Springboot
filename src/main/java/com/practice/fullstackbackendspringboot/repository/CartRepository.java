package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    Optional<Cart> findByProduct_ProductIdAndUserEmail(String productId, String email);
    List<Cart> findAllByFilterAndUserEmail(boolean filter,String email);
    Optional<Cart> findByCartIdAndUserEmail(String cartId, String email);
    List<Cart> findAllByUserEmail(String email);
    Optional<Cart> deleteByCartIdAndUserEmail(String cartId, String email);

}

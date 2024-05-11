package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByProduct_ProductIdAndUserEmail(String productId, String email);
    List<Cart> findAllTotalAmountByFilterAndUserEmail(Boolean isFilter,String email);
//    Optional<Cart> findByProduct_ProductId(String productId);
    Optional<Cart> findByCartIdAndUserEmail(String cartId, String email);
    List<Cart> findAllByUserEmail(String email);

}

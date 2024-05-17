package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.CartTotal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartTotalRepository extends JpaRepository<CartTotal, Long> {
    Optional<CartTotal> findByUserEmail(String email);
}

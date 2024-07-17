package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findAllByStore_StoreId(String storeId);
    List<Order> findAllByActiveTrueAndStore_StoreId(String storeId);
    List<Order> findAllByActiveFalseAndStore_StoreId(String storeId);

}

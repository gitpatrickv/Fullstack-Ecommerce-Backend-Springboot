package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.OrderItem;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findAllByUserEmail(String email, Sort sort);
    List<OrderItem> findAllByUserEmailAndOrder_OrderId(String email, String orderId);
    List<OrderItem> findAllByRatedFalseAndProduct_ProductIdAndOrder_OrderIdAndUserEmail(String productId, String orderId,String email);
    Set<OrderItem> findAllByRatedFalseAndOrder_OrderId(String orderId);
    Boolean existsAllByRatedFalseAndOrder_OrderIdAndUserEmail(String orderId, String email);
    List<OrderItem> findAllByOrder_OrderId(String orderId);

}

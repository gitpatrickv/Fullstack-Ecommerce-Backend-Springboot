package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
//    @EntityGraph(attributePaths = {"productImage", "inventory"})
//    List<Product> findAll();

//    @Query("SELECT p.productImage, i.quantity FROM Product p JOIN p.productImage pimg JOIN p.inventory i WHERE p.productId = :productId")
//    List<Object[]> findProductImagesAndInventoryByProductId(@Param("productId") String productId);

}

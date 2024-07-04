package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.Favorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites, Long> {

    Optional<Favorites> findByProduct_ProductIdAndUserEmail(String productId, String email);
    List<Favorites> findAllByUserEmail(String email);
    void deleteAllByProduct_ProductId(String productId);
}

package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.StoreFollower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoreFollowerRepository extends JpaRepository<StoreFollower, Long> {

    Optional<StoreFollower> findByStore_StoreIdAndUserEmail(String storeId, String email);
    List<StoreFollower> findAllByStore_StoreId(String storeId);
}

package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {

    List<Chat> findAllByUserEmail(String email);
    List<Chat> findAllByStore_StoreId(String storeId);
    Optional<Chat> findByStore_StoreIdAndUserEmail(String storeId, String email);
    Boolean existsByStore_StoreIdAndUserEmail(String storeId, String email);
}

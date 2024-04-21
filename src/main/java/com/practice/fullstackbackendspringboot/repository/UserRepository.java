package com.practice.fullstackbackendspringboot.repository;

import com.practice.fullstackbackendspringboot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    Boolean existsByEmailIgnoreCase(String email);
}
package com.auth.google.repository;

import com.auth.google.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByProviderId(String providerId);
    Optional<User> findByEmail(String email);
}

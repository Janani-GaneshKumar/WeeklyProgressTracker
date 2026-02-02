package com.janani.contentrecommendation.repository;


import com.janani.contentrecommendation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository //Marks the interface as UserRepo bean
public interface UserRepository extends JpaRepository<User, Long> {

    // Find user by email (used in login)
    Optional<User> findByEmail(String email);

    // Check if email already exists (used in registration validation)
    boolean existsByEmail(String email);
}

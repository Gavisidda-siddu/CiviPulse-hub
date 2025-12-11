package com.civicpulse.repository;

import com.civicpulse.model.Role;
import com.civicpulse.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByRole(Role role);   // to check if admin already created
}

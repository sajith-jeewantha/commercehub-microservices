package com.sajithjeewantha.auth_service.repo;

import com.sajithjeewantha.auth_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailEquals(String email);
    boolean existsByEmailEquals(String email);
}

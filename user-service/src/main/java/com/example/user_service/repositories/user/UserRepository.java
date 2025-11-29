package com.example.user_service.repositories.user;

import com.example.user_service.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);
    boolean existsById(Long id);
}

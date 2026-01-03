package com.community.demo.repository;

import com.community.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String > {
    Optional<Object> findByEmailWithAuth(String username);
}

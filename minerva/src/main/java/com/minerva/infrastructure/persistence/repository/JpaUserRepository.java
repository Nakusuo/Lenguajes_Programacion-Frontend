package com.minerva.infrastructure.persistence.repository;

import com.minerva.infrastructure.persistence.entity.UserEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByUsername(String username);
    Optional<UserEntity> findByUsername(String username);
}

package com.minerva.infrastructure.persistence.repository;

import com.minerva.infrastructure.persistence.entity.SupplierEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSupplierRepository extends JpaRepository<SupplierEntity, String> {
    Optional<SupplierEntity> findByRuc(String ruc);
    Optional<SupplierEntity> findByPhoneNumber(String phoneNumber);
    boolean existsByRuc(String ruc);
    boolean existsByPhoneNumber(String phoneNumber);
}

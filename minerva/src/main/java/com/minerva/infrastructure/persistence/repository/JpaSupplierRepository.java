package com.minerva.infrastructure.persistence.repository;

import com.minerva.infrastructure.persistence.entity.SupplierEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaSupplierRepository extends JpaRepository<SupplierEntity, String> {
}

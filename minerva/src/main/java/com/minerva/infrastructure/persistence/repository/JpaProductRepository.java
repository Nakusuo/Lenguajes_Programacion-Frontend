package com.minerva.infrastructure.persistence.repository;

import com.minerva.infrastructure.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, String> {

    Optional<ProductEntity> findByBarCode(String barCode);
    boolean existsByBarCode(String barCode);
}

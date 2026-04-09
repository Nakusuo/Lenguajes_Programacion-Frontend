package com.minerva.infrastructure.persistence.repository;

import com.minerva.infrastructure.persistence.entity.ProductReturnEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaProductReturnRepository extends JpaRepository<ProductReturnEntity, String> {

    List<ProductReturnEntity> findBySaleDetail_SaleDetailId(String saleDetailId);
}


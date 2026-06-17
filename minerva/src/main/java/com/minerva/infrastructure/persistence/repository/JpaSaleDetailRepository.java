package com.minerva.infrastructure.persistence.repository;

import com.minerva.infrastructure.persistence.entity.SaleDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaSaleDetailRepository extends JpaRepository<SaleDetailEntity, String> {

    List<SaleDetailEntity> findBySaleEntity_SaleId(String saleId);

    List<SaleDetailEntity> findByProductEntity_ProductNameId(String productNameId);
}

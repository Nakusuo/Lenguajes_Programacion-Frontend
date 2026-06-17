package com.minerva.infrastructure.persistence.repository;

import com.minerva.infrastructure.persistence.entity.StockEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaStockEntryRepository extends JpaRepository<StockEntryEntity, String> {

    List<StockEntryEntity> findByProductEntity_ProductNameId(String productNameId);

    List<StockEntryEntity> findBySupplierEntity_SupplierNameId(String supplierNameId);
}

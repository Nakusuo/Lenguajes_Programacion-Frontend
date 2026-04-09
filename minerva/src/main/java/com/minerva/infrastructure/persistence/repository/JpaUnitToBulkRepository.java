package com.minerva.infrastructure.persistence.repository;

import com.minerva.infrastructure.persistence.entity.UnitToBulkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaUnitToBulkRepository extends JpaRepository<UnitToBulkEntity, UnitToBulkEntity.UnitToBulkId> {

    Optional<UnitToBulkEntity> findByBulkProduct_ProductNameId(String bulkProductNameId);

    List<UnitToBulkEntity> findByUnitProduct_ProductNameId(String unitProductNameId);
}

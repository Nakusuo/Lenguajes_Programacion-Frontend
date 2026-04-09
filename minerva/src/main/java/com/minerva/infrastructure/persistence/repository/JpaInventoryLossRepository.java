package com.minerva.infrastructure.persistence.repository;

import com.minerva.domain.constants.ReasonProductLoss;
import com.minerva.infrastructure.persistence.entity.InventoryLossEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaInventoryLossRepository extends JpaRepository<InventoryLossEntity, String> {

    List<InventoryLossEntity> findByProduct_ProductNameId(String productNameId);

    List<InventoryLossEntity> findByReason(ReasonProductLoss reason);
}

package com.minerva.infrastructure.persistence.repository;

import com.minerva.infrastructure.persistence.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaSaleRepository extends JpaRepository<SaleEntity, String> {

    List<SaleEntity> findByCustomer_CustomerNameId(String customerNameId);
}

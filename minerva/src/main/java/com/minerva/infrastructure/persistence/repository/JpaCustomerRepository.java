package com.minerva.infrastructure.persistence.repository;

import com.minerva.infrastructure.persistence.entity.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface JpaCustomerRepository extends JpaRepository<CustomerEntity, String> {
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<CustomerEntity> findByPhoneNumber(String phoneNumber);
}


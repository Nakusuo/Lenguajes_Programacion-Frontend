package com.minerva.domain.repositories;

import com.minerva.domain.entities.customer.CustomerId;
import com.minerva.domain.entities.sale.Sale;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SaleRepository {
    void save(Sale sale);
    Optional<Sale> findById(UUID id);
    List<Sale> findByCustomerId(CustomerId customerId);
    List<Sale> findAll();
}

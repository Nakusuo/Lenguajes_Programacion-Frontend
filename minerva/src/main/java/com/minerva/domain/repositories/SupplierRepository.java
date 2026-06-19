package com.minerva.domain.repositories;

import com.minerva.domain.valueObject.PhoneNumber;
import com.minerva.domain.valueObject.RUC;
import com.minerva.domain.entities.supplier.Supplier;
import com.minerva.domain.valueObject.id.SupplierId;

import java.util.List;
import java.util.Optional;

public interface SupplierRepository {
    void save(Supplier supplier);

    boolean existsById(SupplierId id);
    boolean existsByRuc(RUC ruc);
    boolean existsByPhoneNumber(PhoneNumber phoneNumber);
    List<Supplier> findAll();
    Optional<Supplier> findById(SupplierId id);
    Optional<Supplier> findByRuc(RUC ruc);
    Optional<Supplier> findByPhone(PhoneNumber phoneNumber);
}

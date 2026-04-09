package com.minerva.application.port.drivers;

import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.entities.supplier.Supplier;

import java.util.List;
import java.util.Optional;

public interface SupplierUseCase {

    // --------------------- WRITE ---------------------
    Result<Void> register(String supplierName, String ruc, String phoneNumber);
    Result<Void> updatePhoneNumber(String supplierName, String phoneNumber);
    Result<Void> updateRuc(String supplierName, String ruc);

    // --------------------- READ ---------------------
    List<Supplier> findAll();
    Optional<Supplier> findById(String supplierName);
    Optional<Supplier> findByRuc(String ruc);
    Optional<Supplier> findByPhone(String phone);
}

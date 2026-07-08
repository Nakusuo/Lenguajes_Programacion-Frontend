package com.minerva.application.port.drivers;

import com.minerva.domain.entities.result.Result;
import com.minerva.domain.entities.supplier.Supplier;
import com.minerva.domain.exceptions.UnauthorizedActionException;

import java.util.List;
import java.util.Optional;

public interface SupplierUseCase {

    // --------------------- WRITE ---------------------
    Result<Void> register(String supplierName, String ruc, String phoneNumber) throws UnauthorizedActionException;
    Result<Void> updatePhoneNumber(String supplierName, String phoneNumber) throws UnauthorizedActionException;
    Result<Void> updateRuc(String supplierName, String ruc) throws UnauthorizedActionException;

    // --------------------- READ ---------------------
    List<Supplier> findAll() throws UnauthorizedActionException;
    Optional<Supplier> findById(String supplierName)throws UnauthorizedActionException;
    Optional<Supplier> findByRuc(String ruc)throws UnauthorizedActionException;
    Optional<Supplier> findByPhone(String phone)throws UnauthorizedActionException;
}

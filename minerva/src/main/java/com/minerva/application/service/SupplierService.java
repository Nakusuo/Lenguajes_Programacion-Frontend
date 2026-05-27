package com.minerva.application.service;

import com.minerva.application.port.drivers.SupplierUseCase;
import com.minerva.domain.entities.shared.PhoneNumber;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.entities.supplier.RUC;
import com.minerva.domain.entities.supplier.Supplier;
import com.minerva.domain.entities.supplier.SupplierId;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.repositories.SupplierRepository;

import java.util.List;
import java.util.Optional;

public class SupplierService implements SupplierUseCase {
    private final SupplierRepository supplierRepository;

    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository = supplierRepository;
    }

    // --------------------- WRITE ---------------------

    @Override
    public Result<Void> register(String supplierName, String ruc, String phoneNumber) {
        Supplier supplierCreated;
        try {
            supplierCreated = new Supplier(supplierName, ruc, phoneNumber);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }

        if (supplierRepository.existsById(supplierCreated.getSupplierNameId()))
            return Result.fail("Ya existe un proveedor con el mismo nombre.");

        if (supplierCreated.getRuc().isPresent() && supplierRepository.existsByRuc(supplierCreated.getRuc().get()))
            return Result.fail("Ya existe un proveedor con el mismo RUC.");

        if (supplierCreated.getPhoneNumber().isPresent() && supplierRepository.existsByPhoneNumber(supplierCreated.getPhoneNumber().get()))
            return Result.fail("Ya existe un proveedor con el mismo número de teléfono.");

        supplierRepository.save(supplierCreated);

        return Result.success(null);
    }

    @Override
    public Result<Void> updatePhoneNumber(String supplierName, String phoneNumber) {

        Optional<Supplier> supplierOpt = findByPhone(phoneNumber);
        if (supplierOpt.isEmpty()) return Result.fail("Proveedor no encontrado.");

        Supplier supplier = supplierOpt.get();

        Result<Void> updateResult = supplier.updatePhoneNumber(phoneNumber);
        if (updateResult.isFail()) return updateResult;

        if ( supplier.getPhoneNumber().isPresent() && supplierRepository.existsByPhoneNumber(supplier.getPhoneNumber().get()))
            return Result.fail("Ya existe un proveedor con el mismo número de teléfono.");

        supplierRepository.save(supplier);

        return Result.success(null);
    }

    @Override
    public Result<Void> updateRuc(String supplierName, String ruc) {

        Optional<Supplier> supplierOpt = findById(supplierName);
        if (supplierOpt.isEmpty()) return Result.fail("Proveedor no encontrado.");

        Supplier supplier = supplierOpt.get();

        Result<Void> updateResult = supplier.updateRuc(ruc);
        if (updateResult.isFail()) return updateResult;

        if (supplier.getRuc().isPresent() && supplierRepository.existsByRuc(supplier.getRuc().get()))
            return Result.fail("Ya existe un proveedor con el mismo RUC.");

        supplierRepository.save(supplier);

        return Result.success(null);
    }
    // --------------------- READ ---------------------

    @Override
    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }

    @Override
    public Optional<Supplier> findById(String supplierName) {
        try {
            return supplierRepository.findById(new SupplierId(supplierName));
        } catch (DomainException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Supplier> findByRuc(String ruc) {
        try {
            return supplierRepository.findByRuc(new RUC(ruc));
        } catch (DomainException e) {
            return Optional.empty();
        }        
    }

    @Override
    public Optional<Supplier> findByPhone(String phoneNumber) {
        try {
            return supplierRepository.findByPhone(new PhoneNumber(phoneNumber));
        } catch (DomainException e) {
            return Optional.empty();
        }
    }
}

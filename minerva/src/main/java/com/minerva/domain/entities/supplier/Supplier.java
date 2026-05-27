package com.minerva.domain.entities.supplier;

import com.minerva.domain.entities.shared.PhoneNumber;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.entities.shared.Result;

import java.time.LocalDateTime;
import java.util.Optional;

public class Supplier {
    private final SupplierId supplierNameId;
    // Puede ser null
    private RUC ruc;
    private PhoneNumber phoneNumber;
    // ------------
    private final LocalDateTime registrationDate;

    public Supplier(String supplierName, String ruc, String phoneNumber) throws DomainException {
        this.supplierNameId = new SupplierId(supplierName);
        this.ruc = (ruc == null) ? null : new RUC(ruc);
        this.phoneNumber = (phoneNumber == null) ? null : new PhoneNumber(phoneNumber);
        // VALORES POR DEFECTO
        this.registrationDate = LocalDateTime.now();
    }

    public Supplier(String supplierNameId, LocalDateTime registrationDate, String ruc, String phoneNumber) {
        try {
            this.supplierNameId = new SupplierId(supplierNameId);
            this.registrationDate = registrationDate;
            this.ruc = ruc != null ? new RUC(ruc) : null;
            this.phoneNumber = phoneNumber != null ? new PhoneNumber(phoneNumber) : null;
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al crear el proveedor: " + e.getMessage(), e);
        }        
    }

    public Result<Void> updatePhoneNumber(String phoneNumber) {
        try {
            this.phoneNumber = (phoneNumber == null) ? null : new PhoneNumber(phoneNumber);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
        return Result.success(null);
    }

    public Result<Void> updateRuc(String ruc) {
        try {
            this.ruc = (ruc == null) ? null : new RUC(ruc);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
        return Result.success(null);
    }

    public SupplierId getSupplierNameId() {
        return supplierNameId;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public Optional<RUC> getRuc() {
        if  (ruc == null) return Optional.empty();
        return Optional.of(ruc);
    }

    public Optional<PhoneNumber> getPhoneNumber() {
        if (phoneNumber == null) return Optional.empty();
        return Optional.of(phoneNumber);
    }
}


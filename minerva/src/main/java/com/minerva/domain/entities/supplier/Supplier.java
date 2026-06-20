package com.minerva.domain.entities.supplier;

import com.minerva.domain.valueObject.PhoneNumber;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.interfaces.Entity;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.valueObject.RUC;
import com.minerva.domain.valueObject.id.SupplierName;

import java.time.LocalDateTime;
import java.util.Optional;

public class Supplier extends Entity {
    private final SupplierName supplierName;
    // Puede ser null
    private RUC ruc;
    private PhoneNumber phoneNumber;
    // ------------
    private final LocalDateTime registrationDate;

    public Supplier(String supplierName, String ruc, String phoneNumber) throws DomainException {
        SupplierName tempId = new SupplierName(supplierName);
        super(tempId);
        this.supplierName = tempId;
        this.ruc = (ruc == null) ? null : new RUC(ruc);
        this.phoneNumber = (phoneNumber == null) ? null : new PhoneNumber(phoneNumber);
        // VALORES POR DEFECTO
        this.registrationDate = LocalDateTime.now();
    }

    public Supplier(String supplierName, String ruc, String phoneNumber, LocalDateTime registrationDate) {
        SupplierName tempId;
        try {
            tempId = new SupplierName(supplierName);
            
            this.supplierName = tempId;
            this.registrationDate = registrationDate;
            this.ruc = ruc != null ? new RUC(ruc) : null;
            this.phoneNumber = phoneNumber != null ? new PhoneNumber(phoneNumber) : null;
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al crear el proveedor: " + e.getMessage(), e);
        }        
        super(tempId);
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

    public SupplierName getSupplierName() {
        return supplierName;
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


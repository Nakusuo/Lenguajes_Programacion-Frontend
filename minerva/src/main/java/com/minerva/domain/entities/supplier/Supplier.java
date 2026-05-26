package com.minerva.domain.entities.supplier;

import com.minerva.domain.entities.shared.PhoneNumber;
import com.minerva.domain.exceptions.DomainException;

import java.time.LocalDateTime;
import java.util.Optional;

public class Supplier {
    private final SupplierId supplierNameId;
    private RUC ruc;
    private PhoneNumber phoneNumber;
    private final LocalDateTime registrationDate;

    public Supplier(String supplierName, String ruc, String phoneNumber) throws DomainException {
        this.supplierNameId = new SupplierId(supplierName);
        this.ruc = (ruc == null) ? null : new RUC(ruc);
        this.phoneNumber = (phoneNumber == null) ? null : new PhoneNumber(phoneNumber);
        // VALORES POR DEFECTO
        this.registrationDate = LocalDateTime.now();
    }

    public void updatePhoneNumber(String phoneNumber) throws DomainException {
        this.phoneNumber = (phoneNumber == null) ? null : new PhoneNumber(phoneNumber);
    }

    public void updateRuc(String ruc) throws DomainException {
        this.ruc = (ruc == null) ? null : new RUC(ruc);
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


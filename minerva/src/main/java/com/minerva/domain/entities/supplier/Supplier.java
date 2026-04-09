package com.minerva.domain.entities.supplier;

import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.entities.shared.PhoneNumber;

import java.time.LocalDateTime;
import java.util.Optional;

public class Supplier {
    private final SupplierId supplierNameId;
    private RUC ruc;
    private PhoneNumber phoneNumber;
    private final LocalDateTime registrationDate;

    private Supplier(SupplierId supplierNameId, RUC ruc, PhoneNumber phoneNumber) {
        this.supplierNameId = supplierNameId;
        this.ruc = ruc;
        this.phoneNumber = phoneNumber;
        // VALORES POR DEFECTO
        this.registrationDate = LocalDateTime.now();
    }

    public static Result<Supplier> create(String supplierName, String ruc, String phoneNumber) {

        Result<SupplierId> supplierIdResult = SupplierId.of(supplierName);
        if (supplierIdResult.isFail()) return Result.fail(supplierIdResult.getMessage());

        RUC rucValue = null;
        if (ruc != null) {
            Result<RUC> rucResult = RUC.of(ruc);
            if (rucResult.isFail()) return Result.fail(rucResult.getMessage());
            rucValue = rucResult.getData();
        }

        PhoneNumber phoneValue = null;
        if (phoneNumber != null) {
            Result<PhoneNumber> phoneResult = PhoneNumber.of(phoneNumber);
            if (phoneResult.isFail()) return Result.fail(phoneResult.getMessage());
            phoneValue = phoneResult.getData();
        }

        return Result.success(new Supplier(supplierIdResult.getData(), rucValue, phoneValue));
    }

    public Result<Void> updatePhoneNumber(String phoneNumber) {
        if (phoneNumber == null) {
            this.phoneNumber = null;
            return Result.success(null);
        }

        Result<PhoneNumber> phoneResult = PhoneNumber.of(phoneNumber);
        if (phoneResult.isFail()) return Result.fail(phoneResult.getMessage());

        this.phoneNumber = phoneResult.getData();
        return Result.success(null);
    }

    public Result<Void> updateRuc(String ruc) {
        if (ruc == null) {
            this.ruc = null;
            return Result.success(null);
        }

        Result<RUC> rucResult = RUC.of(ruc);
        if (rucResult.isFail()) return Result.fail(rucResult.getMessage());

        this.ruc = rucResult.getData();
        return Result.success(null);
    }

    public SupplierId getSupplierNameId() {
        return supplierNameId;
    }

    public Optional<RUC> getRuc() {
        if  (ruc == null) return Optional.empty();
        return Optional.of(ruc);
    }

    public Optional<PhoneNumber> getPhoneNumber() {
        if (phoneNumber == null) return Optional.empty();
        return Optional.of(phoneNumber);
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
}


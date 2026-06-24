package com.minerva.domain.valueObject.id;

import com.minerva.domain.entities.supplier.SupplierId;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.valueObject.Name;

public class SupplierName extends Name implements SupplierId {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 100;

    public SupplierName(String value) throws DomainException {
        super(value, MIN_LENGTH, MAX_LENGTH);
    }

    @Override
    public String asString() {
        return value;
    }

    @Override
    public String value() {
        return value;
    }
}

package com.minerva.domain.entities.sale;

import java.util.UUID;

import com.minerva.domain.exceptions.DomainException;

public class SaleId {
    public final String value;

    private SaleId(String value) {
        this.value = value;
    }

    public static SaleId generate() {
        String value = UUID.randomUUID().toString();
        return new SaleId(value);
    }

    public static SaleId fromString(String value) throws DomainException {
        try {
            if (value == null || value.isEmpty()) {
                throw new DomainException("El ID de venta no puede ser nulo o vacío.");
            }
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de venta no tiene un formato válido: " + value);
        }
        return new SaleId(value);
    }
}

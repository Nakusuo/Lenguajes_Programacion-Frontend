package com.minerva.domain.entities.sale;

import java.util.UUID;

import com.minerva.domain.exceptions.DomainException;

public class ProductReturnId {
    public final String value;

    private ProductReturnId(String value) {
        this.value = value;
    }

    public static ProductReturnId generate() {
        String value = UUID.randomUUID().toString();
        return new ProductReturnId(value);
    }

    public static ProductReturnId fromString(String value) throws DomainException {
        try {
            if (value == null || value.isEmpty()) {
                throw new DomainException("El ID de devolución de producto no puede ser nulo o vacío.");
            }
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de devolución de producto no tiene un formato válido: " + value);
        } 
        return new ProductReturnId(value);
    }
}

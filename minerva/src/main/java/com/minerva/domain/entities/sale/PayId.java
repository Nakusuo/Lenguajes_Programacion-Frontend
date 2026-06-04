package com.minerva.domain.entities.sale;

import java.util.UUID;

import com.minerva.domain.exceptions.DomainException;

class PayId {
    public final String value;

    private PayId(String value) {
        this.value = value;
    }

    public static PayId generate() {
        String value = UUID.randomUUID().toString();
        return new PayId(value);
    }

    public static PayId fromString(String value) throws DomainException {
        try {
            if (value == null || value.isEmpty()) {
                throw new DomainException("El ID de pago no puede ser nulo o vacío.");
            }
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de pago no tiene un formato válido: " + value);
        }
        return new PayId(value);
    }
}

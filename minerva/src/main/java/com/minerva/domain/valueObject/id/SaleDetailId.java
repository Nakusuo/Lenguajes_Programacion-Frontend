package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.interfaces.Id;
import com.minerva.domain.exceptions.DomainException;

public class SaleDetailId implements Id {
    public final String value;

    private SaleDetailId(String value) {
        this.value = value;
    }

    public static SaleDetailId generate() {
        String value = UUID.randomUUID().toString();
        return new SaleDetailId(value);
    }

    public static SaleDetailId fromString(String value) throws DomainException {
        try {
            if (value == null || value.isEmpty()) {
                throw new DomainException("El ID de detalle de venta no puede ser nulo o vacío.");
            }
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de detalle de venta no tiene un formato válido: " + value);
        }
        return new SaleDetailId(value);
    }

    @Override
    public String value() {
        return value;
    }
}

package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.interfaces.Id;
import com.minerva.domain.interfaces.ValueObject;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;

public class SaleId extends ValueObject<String> implements Id {
    private SaleId(String value) throws DomainException {
        super(value);
    }

    public static SaleId generate() {
        String value = UUID.randomUUID().toString();
        try {
            return new SaleId(value);
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de venta: " + e.getMessage(), e);
        }
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

    @Override
    public String value() {
        return value;
    }
}

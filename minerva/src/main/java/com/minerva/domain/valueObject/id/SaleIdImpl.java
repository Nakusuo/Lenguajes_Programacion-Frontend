package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.entities.sale.SaleId;
import com.minerva.domain.interfaces.ValueObject;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;

public class SaleIdImpl extends ValueObject<String> implements SaleId {
    private SaleIdImpl(String value) throws DomainException {
        super(value);
    }

    public static SaleIdImpl generate() {
        String value = UUID.randomUUID().toString();
        try {
            return new SaleIdImpl(value);
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de venta: " + e.getMessage(), e);
        }
    }

    public static SaleIdImpl fromString(String value) throws DomainException {
        try {
            if (value == null || value.isEmpty()) {
                throw new DomainException("El ID de venta no puede ser nulo o vacío.");
            }
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de venta no tiene un formato válido: " + value);
        }
        return new SaleIdImpl(value);
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

package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.entities.sale.SaleId;
import com.minerva.domain.interfaces.ValueObject;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;

public class SaleIdImpl extends ValueObject<UUID> implements SaleId {
    private SaleIdImpl(UUID value) throws DomainException {
        super(value);
    }

    public static SaleIdImpl generate() {
        try {
            return new SaleIdImpl(UUID.randomUUID());
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de venta: " + e.getMessage(), e);
        }
    }

    public static SaleIdImpl fromString(String value) throws DomainException {
        try {
            return new SaleIdImpl(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de venta no tiene un formato válido: " + value);
        } catch (Exception e) {
            throw new UnexpectedDomainException(e.getMessage(), e);
        }
    }

    @Override
    public String asString() {
        return value.toString();
    }

    @Override
    public UUID value() {
        return value;
    }
}

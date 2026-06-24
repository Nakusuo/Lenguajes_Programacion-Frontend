package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.entities.sale.ProductReturnId;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.interfaces.ValueObject;

public class ProductReturnIdImpl extends ValueObject<UUID> implements ProductReturnId {

    private ProductReturnIdImpl(UUID value) throws DomainException {
        super(value);
    }

    public static ProductReturnIdImpl generate() {
        try {
            return new ProductReturnIdImpl(UUID.randomUUID());
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de devolución de producto: " + e.getMessage(), e);
        }
    }

    public static ProductReturnIdImpl fromString(String value) throws DomainException {
        try {
            return new ProductReturnIdImpl(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de devolución de producto no tiene un formato válido: " + value);
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

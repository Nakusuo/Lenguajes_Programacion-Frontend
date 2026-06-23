package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.entities.sale.ProductReturnId;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.interfaces.ValueObject;

public class ProductReturnIdImpl extends ValueObject<String> implements ProductReturnId {

    private ProductReturnIdImpl(String value) throws DomainException {
        super(value);
    }

    public static ProductReturnIdImpl generate() {
        String value = UUID.randomUUID().toString();
        try {
            return new ProductReturnIdImpl(value);
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de devolución de producto: " + e.getMessage(), e);
        }
    }

    public static ProductReturnIdImpl fromString(String value) throws DomainException {
        try {
            if (value == null || value.isEmpty()) {
                throw new DomainException("El ID de devolución de producto no puede ser nulo o vacío.");
            }
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de devolución de producto no tiene un formato válido: " + value);
        } 
        return new ProductReturnIdImpl(value);
    }

    @Override
    public String value() {
        return value;
    }
}

package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.entities.sale.SaleDetailId;
import com.minerva.domain.interfaces.ValueObject;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;

public class SaleDetailIdImpl extends ValueObject<String> implements SaleDetailId {
    
    private SaleDetailIdImpl(String value) throws DomainException {
        super(value);
    }

    public static SaleDetailIdImpl generate() {
        String value = UUID.randomUUID().toString();
        try {
            return new SaleDetailIdImpl(value);
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de detalle de venta: " + e.getMessage(), e);
        }
    }

    public static SaleDetailIdImpl fromString(String value) throws DomainException {
        try {
            if (value == null || value.isEmpty()) {
                throw new DomainException("El ID de detalle de venta no puede ser nulo o vacío.");
            }
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de detalle de venta no tiene un formato válido: " + value);
        }
        return new SaleDetailIdImpl(value);
    }

    @Override
    public String value() {
        return value;
    }
}

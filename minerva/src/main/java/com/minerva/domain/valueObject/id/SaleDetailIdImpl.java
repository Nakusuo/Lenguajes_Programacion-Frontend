package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.entities.sale.SaleDetailId;
import com.minerva.domain.interfaces.ValueObject;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;

public class SaleDetailIdImpl extends ValueObject<UUID> implements SaleDetailId {
    
    private SaleDetailIdImpl(UUID value) throws DomainException {
        super(value);
    }

    public static SaleDetailIdImpl generate() {
        try {
            return new SaleDetailIdImpl(UUID.randomUUID());
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de detalle de venta: " + e.getMessage(), e);
        }
    }

    public static SaleDetailIdImpl fromString(String value) throws DomainException {
        try {
            return new SaleDetailIdImpl(UUID.fromString(value));
        } catch (Exception e) {
            throw new DomainException("El ID de detalle de venta no tiene un formato válido: " + value);
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

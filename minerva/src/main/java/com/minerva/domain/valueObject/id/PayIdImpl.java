package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.entities.sale.PayId;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.ValueObject;

public class PayIdImpl extends ValueObject<UUID> implements PayId {

    private PayIdImpl(UUID value) throws DomainException {
        super(value);
    }

    public static PayIdImpl generate() {
        try {
            return new PayIdImpl(UUID.randomUUID());
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de pago: " + e.getMessage(), e);
        }
    }

    public static PayIdImpl fromString(String value) throws DomainException {
        try {
            return new PayIdImpl(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de pago no tiene un formato válido: " + value);
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

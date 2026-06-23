package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.entities.sale.PayId;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.ValueObject;

public class PayIdImpl extends ValueObject<String> implements PayId {

    private PayIdImpl(String value) throws DomainException {
        super(value);
    }

    public static PayIdImpl generate() {
        String value = UUID.randomUUID().toString();
        try {
            return new PayIdImpl(value);
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de pago: " + e.getMessage(), e);
        }
    }

    public static PayIdImpl fromString(String value) throws DomainException {
        try {
            if (value == null || value.isEmpty()) {
                throw new DomainException("El ID de pago no puede ser nulo o vacío.");
            }
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de pago no tiene un formato válido: " + value);
        }
        return new PayIdImpl(value);
    }

    @Override
    public String value() {
        return value;
    }
    
}

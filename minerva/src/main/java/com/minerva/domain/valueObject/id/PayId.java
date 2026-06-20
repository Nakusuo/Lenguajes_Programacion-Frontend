package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.interfaces.Id;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.ValueObject;

public class PayId extends ValueObject<String> implements Id {

    private PayId(String value) throws DomainException {
        super(value);
    }

    public static PayId generate() {
        String value = UUID.randomUUID().toString();
        try {
            return new PayId(value);
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de pago: " + e.getMessage(), e);
        }
    }

    public static PayId fromString(String value) throws DomainException {
        try {
            if (value == null || value.isEmpty()) {
                throw new DomainException("El ID de pago no puede ser nulo o vacío.");
            }
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de pago no tiene un formato válido: " + value);
        }
        return new PayId(value);
    }

    @Override
    public String value() {
        return value;
    }
    
}

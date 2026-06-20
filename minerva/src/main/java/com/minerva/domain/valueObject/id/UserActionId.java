package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.interfaces.Id;
import com.minerva.domain.interfaces.ValueObject;

public class UserActionId extends ValueObject<String> implements Id {
    
    private UserActionId(String value) throws DomainException {
        super(value);
    }

    public static UserActionId generate() {
        String value = UUID.randomUUID().toString();
        try {
            return new UserActionId(value);
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de acción de usuario: " + e.getMessage(), e);
        }
    }

    public static UserActionId fromString(String value) throws DomainException {
        try {
            if (value == null || value.isEmpty()) {
                throw new DomainException("El ID de acción de usuario no puede ser nulo o vacío.");
            }
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de acción de usuario no tiene un formato válido: " + value);
        }
        return new UserActionId(value);
    }

    @Override
    public String value() {
        return value;
    }
}

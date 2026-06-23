package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.entities.userAction.UserActionId;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.interfaces.ValueObject;

public class UserActionIdImpl extends ValueObject<String> implements UserActionId {
    
    private UserActionIdImpl(String value) throws DomainException {
        super(value);
    }

    public static UserActionIdImpl generate() {
        String value = UUID.randomUUID().toString();
        try {
            return new UserActionIdImpl(value);
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de acción de usuario: " + e.getMessage(), e);
        }
    }

    public static UserActionIdImpl fromString(String value) throws DomainException {
        try {
            if (value == null || value.isEmpty()) {
                throw new DomainException("El ID de acción de usuario no puede ser nulo o vacío.");
            }
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de acción de usuario no tiene un formato válido: " + value);
        }
        return new UserActionIdImpl(value);
    }

    @Override
    public String value() {
        return value;
    }
}

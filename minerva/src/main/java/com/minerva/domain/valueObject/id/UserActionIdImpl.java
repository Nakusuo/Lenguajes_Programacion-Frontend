package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.entities.userAction.UserActionId;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.interfaces.ValueObject;

public class UserActionIdImpl extends ValueObject<UUID> implements UserActionId {
    
    private UserActionIdImpl(UUID value) throws DomainException {
        super(value);
    }

    public static UserActionIdImpl generate() {
        try {
            return new UserActionIdImpl(UUID.randomUUID());
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de acción de usuario: " + e.getMessage(), e);
        }
    }

    public static UserActionIdImpl fromString(String value) throws DomainException {
        try {
            return new UserActionIdImpl(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de acción de usuario no tiene un formato válido: " + value);
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

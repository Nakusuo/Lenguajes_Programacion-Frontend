package com.minerva.domain.entities.userAction;

import java.util.UUID;

import com.minerva.domain.exceptions.DomainException;

public class UserActionId {
    public final String value;

    private UserActionId(String value) {
        this.value = value;
    }

    public static UserActionId generate() {
        String value = UUID.randomUUID().toString();
        return new UserActionId(value);
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
    
}

package com.minerva.domain.valueObject.id;

import com.minerva.domain.entities.user.UserId;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.ValueObject;

public final class UserName extends ValueObject<String> implements UserId {

    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 30;

    public UserName(String value) throws DomainException {
        super(value);

        if (value.isBlank()) throw new DomainException("El USERNAME no puede estar vacío.");
        if (value.length() < MIN_LENGTH) throw new DomainException("El USERNAME debe tener al menos " + MIN_LENGTH + " caracteres.");
        if (value.length() > MAX_LENGTH) throw new DomainException("El USERNAME no puede exceder los " + MAX_LENGTH + " caracteres.");
        if (!value.matches("^[a-zA-Z0-9]+$")) throw new DomainException("El USERNAME solo puede contener letras (sin tilde) y números");
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String asString() {
        return value;
    }
}

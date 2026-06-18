package com.minerva.domain.entities.user;

import java.util.Objects;

import com.minerva.domain.exceptions.DomainException;

public final class UserName {

    private static final Integer MIN_LENGTH = 3;
    private static final Integer MAX_LENGTH = 30;

    public final String value;


    public UserName(String value) throws DomainException {
        if (value == null) throw new DomainException("El USERNAME no puede ser nulo.");
        if (value.isBlank()) throw new DomainException("El USERNAME no puede estar vacío.");
        if (value.length() < MIN_LENGTH) throw new DomainException("El USERNAME debe tener al menos " + MIN_LENGTH + " caracteres.");
        if (value.length() > MAX_LENGTH) throw new DomainException("El USERNAME no puede exceder los " + MAX_LENGTH + " caracteres.");
        if (!value.matches("^[a-zA-Z0-9]+$")) throw new DomainException("El USERNAME solo puede contener letras (sin tilde) y números");

        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserName userName))
            return false;
        return value.equals(userName.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}

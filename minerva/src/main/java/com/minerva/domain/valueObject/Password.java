package com.minerva.domain.valueObject;

import java.util.Objects;

import com.minerva.domain.exceptions.DomainException;

public final class Password {

    private static final Integer MIN_LENGTH = 8;
    private static final Integer MAX_LENGTH = 100;

    public final String value;


    public Password(String value) throws DomainException {
        if (value == null) throw new DomainException("El PASSWORD no puede ser nulo.");
        if (value.isBlank()) throw new DomainException("El PASSWORD no puede estar vacío.");
        if (value.length() < MIN_LENGTH) throw new DomainException("El PASSWORD debe tener al menos " + MIN_LENGTH + " caracteres.");
        if (value.length() > MAX_LENGTH)throw new DomainException("El PASSWORD no puede exceder los " + MAX_LENGTH + " caracteres.");

        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Password password))
            return false;
        return value.equals(password.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

}

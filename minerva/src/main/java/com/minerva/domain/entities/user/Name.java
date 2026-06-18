package com.minerva.domain.entities.user;

import com.minerva.domain.exceptions.DomainException;

import java.util.Objects;

public class Name {
    private static final Integer MIN_LENGTH = 3;
    private static final Integer MAX_LENGTH = 50;

    public final String value;

    public Name(String value) throws DomainException {

        if (value == null) throw new DomainException("El NOMBRE no puede ser nulo.");
        if (value.isBlank()) throw new DomainException("El NOMBRE no puede estar vacío.");
        if (value.length() < MIN_LENGTH) throw new DomainException("El NOMBRE debe tener al menos " + MIN_LENGTH + " caracteres.");
        if (value.length() > MAX_LENGTH) throw new DomainException("El NOMBRE no puede exceder los " + MAX_LENGTH + " caracteres.");
        if (!value.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) throw new DomainException("El NOMBRE solo debe contener letras.");

        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Name that = (Name) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

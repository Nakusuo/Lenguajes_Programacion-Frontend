package com.minerva.domain.valueObject;

import com.minerva.domain.exceptions.DomainException;

import java.util.Objects;

// MEJORAS: Los regex
public class PhoneNumber {
    private static final int LENGTH = 9;
    public final String value;

    public PhoneNumber(String value) throws DomainException {

        if (value == null) throw new DomainException("Ingrese un número de teléfono.");
        if (value.length() != LENGTH) throw new DomainException("El número de teléfono debe tener " + LENGTH + " dígitos.");
        if (!value.matches("^\\d+$")) throw new DomainException("El número de teléfono solo puede contener números.");

        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

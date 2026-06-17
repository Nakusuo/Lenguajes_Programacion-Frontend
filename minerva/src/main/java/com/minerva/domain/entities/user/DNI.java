package com.minerva.domain.entities.user;

import com.minerva.domain.exceptions.DomainException;

import java.util.Objects;

public class DNI {

    private static final int LENGTH = 8;

    public final String value;

    public DNI(String value) throws DomainException {

        if (value == null)
            throw new DomainException("Ingrese el DNI.");

        if (value.isBlank())
            throw new DomainException("El DNI no puede estar vacío.");

        if (!value.matches("^\\d+$"))
            throw new DomainException("El DNI solo puede contener números.");

        if (value.length() != LENGTH)
            throw new DomainException("El DNI debe tener exactamente " + LENGTH + " dígitos.");

        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DNI dni = (DNI) o;
        return Objects.equals(value, dni.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

}
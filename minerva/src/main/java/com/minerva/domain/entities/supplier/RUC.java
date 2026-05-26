package com.minerva.domain.entities.supplier;

import com.minerva.domain.exceptions.DomainException;

import java.util.Objects;

public class RUC {
    private static final int LENGTH = 11;
    public final String value;

    public RUC(String value) throws DomainException {

        if (value == null) throw new DomainException("El RUC no puede ser nulo.");
        if (value.length() != LENGTH) throw new DomainException("El RUC debe tener exactamente " + LENGTH + " caracteres.");
        if (!value.matches("^\\d+$")) throw new DomainException("El RUC debe contener solo números.");

        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RUC ruc = (RUC) o;
        return Objects.equals(value, ruc.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

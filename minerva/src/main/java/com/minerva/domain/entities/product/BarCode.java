package com.minerva.domain.entities.product;

import com.minerva.domain.exceptions.DomainException;

import java.util.Objects;

public class BarCode {

    public final String value;

    public BarCode(String value) throws DomainException {

        if (value == null) throw new DomainException("Ingrese el código de barras.");
        if (value.isBlank()) throw new DomainException("El código de barras no puede estar vacío.");
        if (value.length() != 13) throw new DomainException("El código de barras debe tener 13 dígitos.");
        if (!value.matches("^\\d+$")) throw new DomainException("El código de barras solo puede contener números.");


        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BarCode barCode = (BarCode) o;
        return Objects.equals(value, barCode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

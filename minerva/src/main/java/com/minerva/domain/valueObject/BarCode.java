package com.minerva.domain.valueObject;

import com.minerva.domain.exceptions.InvalidDomainArgumentException;

public class BarCode extends ValueObject<String> {

    public BarCode(String value) throws InvalidDomainArgumentException {
        super(value);

        if (value.isBlank()) throw new InvalidDomainArgumentException("El código de barras no puede estar vacío.");
        if (value.length() != 13) throw new InvalidDomainArgumentException("El código de barras debe tener 13 dígitos.");
        if (!value.matches("^\\d+$")) throw new InvalidDomainArgumentException("El código de barras solo puede contener números.");
    }
}

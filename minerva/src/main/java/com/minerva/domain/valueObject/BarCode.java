package com.minerva.domain.valueObject;

import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.ValueObject;

public class BarCode extends ValueObject<String> {

    public BarCode(String value) throws DomainException {
        super(value);

        if (value.isBlank()) throw new DomainException("El código de barras no puede estar vacío.");
        if (value.length() != 13) throw new DomainException("El código de barras debe tener 13 dígitos.");
        if (!value.matches("^\\d+$")) throw new DomainException("El código de barras solo puede contener números.");
    }
}

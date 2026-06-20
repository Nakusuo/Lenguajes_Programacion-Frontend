package com.minerva.domain.valueObject.id;

import com.minerva.domain.interfaces.Id;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.ValueObject;

public class DNI extends ValueObject<String> implements Id {

    private static final int LENGTH = 8;

    public DNI(String value) throws DomainException {
        super(value);

        if (value.isBlank()) throw new DomainException("El DNI no puede estar vacío.");
        if (!value.matches("^\\d+$")) throw new DomainException("El DNI solo puede contener números.");
        if (value.length() != LENGTH) throw new DomainException("El DNI debe tener exactamente " + LENGTH + " dígitos.");
    }

    @Override
    public String value() {
        return value;
    }
}
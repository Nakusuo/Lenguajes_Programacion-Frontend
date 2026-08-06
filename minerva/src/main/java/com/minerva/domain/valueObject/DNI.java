package com.minerva.domain.valueObject;


import com.minerva.domain.exceptions.InvalidDomainArgumentException;

public class DNI extends ValueObject<String> {

    private static final int LENGTH = 8;

    public DNI(String value) throws InvalidDomainArgumentException {
        super(value);

        if (value.isBlank()) throw new InvalidDomainArgumentException("El DNI no puede estar vacío.");
        if (!value.matches("^\\d+$")) throw new InvalidDomainArgumentException("El DNI solo puede contener números.");
        if (value.length() != LENGTH) throw new InvalidDomainArgumentException("El DNI debe tener exactamente " + LENGTH + " dígitos.");
    }
}
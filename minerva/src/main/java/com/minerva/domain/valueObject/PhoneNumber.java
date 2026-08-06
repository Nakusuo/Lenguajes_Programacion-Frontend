package com.minerva.domain.valueObject;

import com.minerva.domain.exceptions.InvalidDomainArgumentException;

// MEJORAS: Los regex
public class PhoneNumber extends ValueObject<String> {
    private static final int LENGTH = 9;

    public PhoneNumber(String value) throws InvalidDomainArgumentException {
        super(value);

        if (value.length() != LENGTH) throw new InvalidDomainArgumentException("El número de teléfono debe tener " + LENGTH + " dígitos.");
        if (!value.matches("^\\d+$")) throw new InvalidDomainArgumentException("El número de teléfono solo puede contener números.");
    }

}

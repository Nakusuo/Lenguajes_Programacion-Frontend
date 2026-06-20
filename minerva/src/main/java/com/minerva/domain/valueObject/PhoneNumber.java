package com.minerva.domain.valueObject;

import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.ValueObject;

// MEJORAS: Los regex
public class PhoneNumber extends ValueObject<String> {
    private static final int LENGTH = 9;

    public PhoneNumber(String value) throws DomainException {
        super(value);

        if (value.length() != LENGTH) throw new DomainException("El número de teléfono debe tener " + LENGTH + " dígitos.");
        if (!value.matches("^\\d+$")) throw new DomainException("El número de teléfono solo puede contener números.");
    }

}

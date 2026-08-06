package com.minerva.domain.valueObject;

import com.minerva.domain.exceptions.InvalidDomainArgumentException;

public class RUC extends ValueObject<String> {
    private static final int LENGTH = 11;

    public RUC(String value) throws InvalidDomainArgumentException {
        super(value);

        if (value.length() != LENGTH) throw new InvalidDomainArgumentException("El RUC debe tener exactamente " + LENGTH + " caracteres.");
        if (!value.matches("^\\d+$")) throw new InvalidDomainArgumentException("El RUC debe contener solo números.");
    }

}

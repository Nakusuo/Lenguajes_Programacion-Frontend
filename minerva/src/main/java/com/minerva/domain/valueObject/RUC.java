package com.minerva.domain.valueObject;

import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.ValueObject;

public class RUC extends ValueObject<String> {
    private static final int LENGTH = 11;

    public RUC(String value) throws DomainException {
        super(value);

        if (value.length() != LENGTH) throw new DomainException("El RUC debe tener exactamente " + LENGTH + " caracteres.");
        if (!value.matches("^\\d+$")) throw new DomainException("El RUC debe contener solo números.");
    }

}

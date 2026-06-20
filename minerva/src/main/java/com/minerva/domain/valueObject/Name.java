package com.minerva.domain.valueObject;

import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.ValueObject;

public class Name extends ValueObject<String> {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;

    public Name(String value) throws DomainException {
        super(value);
        validate(value, MIN_LENGTH, MAX_LENGTH);
    }

    public Name(String value, int minLength, int maxLength) throws DomainException {
        super(value);
        validate(value, minLength, maxLength);
    }

    private void validate(String value, int minLength, int maxLength) throws DomainException {
        if (value.isBlank()) throw new DomainException("El NOMBRE no puede estar vacío.");
        if (value.length() < minLength) throw new DomainException("El NOMBRE debe tener al menos " + minLength + " caracteres.");
        if (value.length() > maxLength) throw new DomainException("El NOMBRE no puede exceder los " + maxLength + " caracteres.");
        if (!value.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) throw new DomainException("El NOMBRE solo debe contener letras.");
    }
}

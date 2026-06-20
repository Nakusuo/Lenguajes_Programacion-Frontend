package com.minerva.domain.valueObject;

import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.ValueObject;

import java.util.Objects;

public class Name extends ValueObject<String> {
    private static final Integer MIN_LENGTH = 3;
    private static final Integer MAX_LENGTH = 50;

    public Name(String value) throws DomainException {
        super(value);

        if (value.isBlank()) throw new DomainException("El NOMBRE no puede estar vacío.");
        if (value.length() < MIN_LENGTH) throw new DomainException("El NOMBRE debe tener al menos " + MIN_LENGTH + " caracteres.");
        if (value.length() > MAX_LENGTH) throw new DomainException("El NOMBRE no puede exceder los " + MAX_LENGTH + " caracteres.");
        if (!value.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) throw new DomainException("El NOMBRE solo debe contener letras.");
    }

}

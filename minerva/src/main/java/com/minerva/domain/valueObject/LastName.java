package com.minerva.domain.valueObject;

import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.ValueObject;

public class LastName extends ValueObject<String> {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 50;

    public LastName(String value) throws DomainException {
        super(value);

        if (value.isBlank()) throw new DomainException("El APELLIDO no puede estar vacío.");
        if (value.length() < MIN_LENGTH) throw new DomainException("El APELLIDO debe tener al menos " + MIN_LENGTH + " caracteres.");
        if (value.length() > MAX_LENGTH) throw new DomainException("El APELLIDO no puede exceder los " + MAX_LENGTH + " caracteres.");
        if (!value.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ ]+$")) throw new DomainException("El APELLIDO solo debe contener letras.");
    }
    
}

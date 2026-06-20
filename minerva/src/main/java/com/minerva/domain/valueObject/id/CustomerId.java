package com.minerva.domain.valueObject.id;

import com.minerva.domain.interfaces.Id;
import com.minerva.domain.interfaces.ValueObject;
import com.minerva.domain.exceptions.DomainException;

public class CustomerId extends ValueObject<String> implements Id {
    private static final Integer MIN_LENGTH = 3;
    private static final Integer MAX_LENGTH = 50;

    public CustomerId(String value) throws DomainException {
        super(value);

        if (value.isBlank()) throw new DomainException("El NOMBRE DEL CLIENTE no puede estar vacío.");
        if (value.length() < MIN_LENGTH) throw new DomainException("El NOMBRE DEL CLIENTE debe tener al menos " + MIN_LENGTH + " caracteres.");
        if (value.length() > MAX_LENGTH) throw new DomainException("El NOMBRE DEL CLIENTE no puede exceder los " + MAX_LENGTH + " caracteres.");
        if (!value.matches("^[A-Za-zñÑ0-9 ]+$")) throw new DomainException("El NOMBRE DEL CLIENTE solo debe contener letras (sin tildes) y números.");

    }

    @Override
    public String value() {
        return value;
    }
}

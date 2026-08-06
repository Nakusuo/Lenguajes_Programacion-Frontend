package com.minerva.domain.valueObject;

import com.minerva.domain.exceptions.InvalidDomainArgumentException;

public class ProductName extends ValueObject<String> {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 100;

    public ProductName(String value) throws InvalidDomainArgumentException {
        super(value);

        if (value.isBlank()) throw new InvalidDomainArgumentException("El nombre del producto no puede estar vacío.");
        if (value.length() < MIN_LENGTH) throw new InvalidDomainArgumentException("El nombre del producto debe tener al menos " + MIN_LENGTH + " caracteres.");
        if (value.length() > MAX_LENGTH) throw new InvalidDomainArgumentException("El nombre del producto no puede tener más de " + MAX_LENGTH + " caracteres.");
        if (!value.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 ]+$")) throw new InvalidDomainArgumentException("El nombre del producto solo puede contener letras y números.");
    }

}

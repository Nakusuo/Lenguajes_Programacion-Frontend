package com.minerva.domain.valueObject.id;

import com.minerva.domain.interfaces.Id;
import com.minerva.domain.exceptions.DomainException;

import java.util.Objects;

public class ProductId implements Id {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 100;

    public final String value;

    public ProductId(String value) throws DomainException {

        if (value == null) throw new DomainException("Ingrese el nombre del producto.");
        if (value.isBlank()) throw new DomainException("El nombre del producto no puede estar vacío.");
        if (value.length() < MIN_LENGTH) throw new DomainException("El nombre del producto debe tener al menos " + MIN_LENGTH + " caracteres.");
        if (value.length() > MAX_LENGTH) throw new DomainException("El nombre del producto no puede tener más de " + MAX_LENGTH + " caracteres.");
        if (!value.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 ]+$")) throw new DomainException("El nombre del producto solo puede contener letras y números.");

        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductId productId = (ProductId) o;
        return Objects.equals(value, productId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String value() {
        return value;
    }
}

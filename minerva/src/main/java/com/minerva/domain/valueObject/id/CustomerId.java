package com.minerva.domain.valueObject.id;

import com.minerva.domain.interfaces.Id;
import com.minerva.domain.exceptions.DomainException;

import java.util.Objects;

public class CustomerId implements Id {
    private static final Integer MIN_LENGTH = 3;
    private static final Integer MAX_LENGTH = 50;

    public final String value;

    public CustomerId(String value) throws DomainException {

        if (value == null) throw new DomainException("El NOMBRE DEL CLIENTE no puede ser nulo.");
        if (value.isBlank()) throw new DomainException("El NOMBRE DEL CLIENTE no puede estar vacío.");
        if (value.length() < MIN_LENGTH) throw new DomainException("El NOMBRE DEL CLIENTE debe tener al menos " + MIN_LENGTH + " caracteres.");
        if (value.length() > MAX_LENGTH) throw new DomainException("El NOMBRE DEL CLIENTE no puede exceder los " + MAX_LENGTH + " caracteres.");
        if (!value.matches("^[A-Za-zñÑ0-9 ]+$")) throw new DomainException("El NOMBRE DEL CLIENTE solo debe contener letras (sin tildes) y números.");

        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CustomerId that = (CustomerId) o;
        return Objects.equals(value, that.value);
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

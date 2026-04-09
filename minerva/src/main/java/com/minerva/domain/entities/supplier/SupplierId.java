package com.minerva.domain.entities.supplier;

import com.minerva.domain.entities.shared.Result;

import java.util.Objects;

public class SupplierId {
    private static final Integer MIN_LENGTH = 3;
    private static final Integer MAX_LENGTH = 100;

    private final String value;

    private SupplierId(String value) {
        this.value = value;
    }

    public static Result<SupplierId> of(String value) {
        if (value == null) return Result.fail("El NOMBRE DEL PROVEEDOR no puede ser nulo.");
        if (value.isBlank()) return Result.fail("El NOMBRE DEL PROVEEDOR no puede estar vacio.");
        if (value.length() < MIN_LENGTH) return Result.fail("El NOMBRE DEL PROVEEDOR debe tener al menos " + MIN_LENGTH + " caracteres.");
        if (value.length() > MAX_LENGTH) return Result.fail("El NOMBRE DEL PROVEEDOR no puede exceder los " + MAX_LENGTH + " caracteres.");
        if (!value.matches("^[A-Za-zñÑ0-9 ]+$")) return Result.fail("El NOMBRE DEL PROVEEDOR solo debe contener letras (sin tildes) y números.");

        return Result.success(new SupplierId(value));
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SupplierId that = (SupplierId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

package com.minerva.domain.entities.shared;

import java.util.Objects;

// MEJORAS: Los regex
public class PhoneNumber {
    private static final int LENGTH = 9;
    private final String value;

    private PhoneNumber(String value) {
        this.value = value;
    }

    public static Result<PhoneNumber> of(String value) {
        if (value == null) return Result.fail("Ingrese un número de teléfono.");
        if (value.length() != LENGTH) return Result.fail("El número de teléfono debe tener " + LENGTH + " dígitos.");
        if (!value.matches("^\\d+$")) return Result.fail("El número de teléfono solo puede contener números.");

        return Result.success(new PhoneNumber(value));
    }


    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PhoneNumber that = (PhoneNumber) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

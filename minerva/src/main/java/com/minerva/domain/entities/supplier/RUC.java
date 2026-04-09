package com.minerva.domain.entities.supplier;

import com.minerva.domain.entities.shared.Result;

import java.util.Objects;

public class RUC {
    private static final int LENGTH = 11;
    private final String value;

    private RUC(String value) {
        this.value = value;
    }

    public static Result<RUC> of(String value) {
        if (value == null) return Result.fail("El RUC no puede ser nulo.");
        if (value.length() != LENGTH) return Result.fail("El RUC debe tener exactamente " + LENGTH + " caracteres.");
        if (!value.matches("^\\d+$")) return Result.fail("El RUC debe contener solo números.");

        return Result.success(new RUC(value));
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RUC ruc = (RUC) o;
        return Objects.equals(value, ruc.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

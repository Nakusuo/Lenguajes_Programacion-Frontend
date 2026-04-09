package com.minerva.domain.entities.product;

import com.minerva.domain.entities.shared.Result;

import java.util.Objects;

public class BarCode {

    private final String value;

    private BarCode(String value) {
        this.value = value;
    }

    public static Result<BarCode> of(String value) {
        if (value == null) return Result.fail("Ingrese el código de barras.");
        if (value.isBlank()) return Result.fail("El código de barras no puede estar vacío.");
        if (value.length() != 13) return Result.fail("El código de barras debe tener 13 dígitos.");
        if (!value.matches("^\\d+$")) return Result.fail("El código de barras solo puede contener números.");

        return Result.success(new BarCode(value));
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BarCode barCode = (BarCode) o;
        return Objects.equals(value, barCode.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

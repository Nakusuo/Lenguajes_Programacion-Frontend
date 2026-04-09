package com.minerva.domain.entities.product;

import com.minerva.domain.entities.shared.Result;

import java.util.Objects;

public class ProductId  {
    private static final int MIN_LENGTH = 3;
    private static final int MAX_LENGTH = 100;

    private final String value;

    private ProductId(String value) {
        this.value = value;
    }

    public static Result<ProductId> of(String value) {
        if (value == null) return Result.fail("Ingrese el nombre del producto.");
        if (value.isBlank()) return Result.fail("El nombre del producto no puede estar vacío.");
        if (value.length() < MIN_LENGTH) return Result.fail("El nombre del producto debe tener al menos " + MIN_LENGTH + " caracteres.");
        if (value.length() > MAX_LENGTH) return Result.fail("El nombre del producto no puede tener más de " + MAX_LENGTH + " caracteres.");
        if (!value.matches("^[A-Za-zÁÉÍÓÚáéíóúÑñ0-9 ]+$")) return Result.fail("El nombre del producto solo puede contener letras y números.");

        return Result.success(new ProductId(value));
    }

    public String value() {
        return value;
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
}

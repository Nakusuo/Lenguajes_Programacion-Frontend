package com.minerva.domain.entities.product;

import com.minerva.domain.entities.shared.Result;

import java.math.BigDecimal;
import java.util.Objects;

public class ProductQuantity {
    // DECIMAL(10,3)
    private static final BigDecimal MIN_AMOUNT = BigDecimal.ZERO;
    private static final int MAX_DECIMALS = 3;


    private final BigDecimal value;

    private ProductQuantity(BigDecimal value) {
        this.value = value;
    }

    public static Result<ProductQuantity> of(BigDecimal value) {
        if (value == null) return Result.fail("Ingrese la cantidad del producto.");
        if (value.scale() > MAX_DECIMALS) return Result.fail("El monto solo puede tener " + MAX_DECIMALS + " decimales.");
        if (value.compareTo(MIN_AMOUNT) < 0) return Result.fail("La cantidad no puede ser menor que " + MIN_AMOUNT + ".");

        return Result.success(new ProductQuantity(value));
    }

    // Nota: Mejorar el nombre
    public static ProductQuantity zero() {
        return new ProductQuantity(BigDecimal.ZERO);
    }

    public boolean isGreaterThanZero() {
        return value.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean isLessThanZero() {
        return value.compareTo(BigDecimal.ZERO) < 0;
    }

    public boolean isZero() {
        return value.compareTo(BigDecimal.ZERO) == 0;
    }

    public boolean isZeroOrLess() {
        return value.compareTo(BigDecimal.ZERO) <= 0;
    }

// --------------------- COMPARACIONES ---------------------BORRAR SI NO TIENE USO

    public boolean isGreaterThan(ProductQuantity other) {
        return this.value.compareTo(other.value) > 0;
    }

    public boolean isLessThan(ProductQuantity other) {
        return this.value.compareTo(other.value) < 0;
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProductQuantity that = (ProductQuantity) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

package com.minerva.domain.entities.shared;

import java.math.BigDecimal;
import java.util.Objects;

public final class Money {
    public static final BigDecimal MIN_AMOUNT = BigDecimal.ZERO;
    public static final int MAX_DECIMALS = 2;

    private final BigDecimal value;

    private Money(BigDecimal amount) {
        this.value = amount;
    }

    public static Result<Money> of(BigDecimal value) {
        if (value == null) return Result.fail("Ingrese el monto.");
        if (value.scale() > MAX_DECIMALS) return Result.fail("El monto solo puede tener " + MAX_DECIMALS + " decimales.");
        if (value.compareTo(MIN_AMOUNT) < 0) return Result.fail("El monto no puede ser menor que " + MIN_AMOUNT + ".");

        return Result.success(new Money(value));
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

    public boolean isZeroOrGreater() {
        return value.compareTo(BigDecimal.ZERO) >= 0;
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}


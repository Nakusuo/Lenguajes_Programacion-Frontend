package com.minerva.domain.valueObject;

import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.MinimumAmountException;
import com.minerva.domain.exceptions.UnexpectedDomainException;

import java.math.BigDecimal;
import java.util.Objects;

public final class Money {
    public static final BigDecimal MIN_AMOUNT = BigDecimal.ZERO;
    public static final int MAX_DECIMALS = 2;

    public final BigDecimal value;

    public Money(BigDecimal value) throws DomainException {

        if (value == null) throw new DomainException("Ingrese el monto.");
        if (value.scale() > MAX_DECIMALS) throw new DomainException("El monto solo puede tener " + MAX_DECIMALS + " decimales.");
        if (value.compareTo(MIN_AMOUNT) < 0) throw new MinimumAmountException(MIN_AMOUNT.toString());

        this.value = value;
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

    public boolean isLessThan(Money other) {
        return this.value.compareTo(other.value) < 0;
    }

    public boolean isGreaterThan(Money other) {
        return this.value.compareTo(other.value) > 0;
    }

    public Money add(Money other) {
        try {
            return new Money(this.value.add(other.value));
        } catch (DomainException e) {
            // Si esto truena, recenle al de arriba
            throw new UnexpectedDomainException("Error al sumar montos: " + e.getMessage(), e);
        }
    }

    public Money subtract(Money other) throws MinimumAmountException {
        try {
            return new Money(this.value.subtract(other.value));
        } catch (MinimumAmountException e) {
            throw e;
        } catch (DomainException e) {
            // Si esto truena, récenle al de arriba
            throw new UnexpectedDomainException("Error al restar montos: " + e.getMessage(), e);
        }
    }

    public static Money zero() {
        try {
            return new Money(BigDecimal.ZERO);
        } catch (DomainException e) {
            // Si esto truena, récenle al de arriba
            throw new UnexpectedDomainException("Error al crear el monto cero.", e);
        }
    }

    public static Money tenCents() {
        try {
            return new Money(new BigDecimal("0.10"));
        } catch (DomainException e) {
            // Si esto truena, récenle al de arriba
            throw new UnexpectedDomainException("Error al crear el monto mínimo.", e);
        }
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


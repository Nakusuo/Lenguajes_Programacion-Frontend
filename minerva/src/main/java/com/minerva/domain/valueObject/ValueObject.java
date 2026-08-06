package com.minerva.domain.valueObject;

import java.util.Objects;

import com.minerva.domain.exceptions.NullValueException;

public abstract class ValueObject<V> {
    public final V value;

    public ValueObject(V value) throws NullValueException {
        if (value == null) throw new NullValueException("El valor no puede ser nulo.");
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ValueObject<?> that = (ValueObject<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

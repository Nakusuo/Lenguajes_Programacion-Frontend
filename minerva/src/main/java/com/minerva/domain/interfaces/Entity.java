package com.minerva.domain.interfaces;

import com.minerva.domain.exceptions.UnexpectedDomainException;

import java.util.Objects;

public abstract class Entity {
    private final Id<?> id;

    public Entity(Id<?> id) {
        if (id == null) throw new UnexpectedDomainException("El ID no puede ser nulo");
        this.id = id;
    }

    public Id<?> getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(getId(), entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}

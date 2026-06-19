package com.minerva.domain.interfaces;

import com.minerva.domain.exceptions.UnexpectedDomainException;

public abstract class Entity {
    private final Id id;

    public Entity(Id id) {
        if (id == null) throw new UnexpectedDomainException("El ID no puede ser nulo");
        this.id = id;
    }

    public Id getId() {
        return id;
    }
}

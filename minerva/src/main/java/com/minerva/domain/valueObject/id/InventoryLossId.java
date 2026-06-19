package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.interfaces.Id;
import com.minerva.domain.exceptions.DomainException;

public class InventoryLossId implements Id {
    public final String value;

    private InventoryLossId(String value) {
        this.value = value;
    }

    public static InventoryLossId generate() {
        String value = java.util.UUID.randomUUID().toString();
        return new InventoryLossId(value);
    }

    public static InventoryLossId fromString(String value) throws DomainException {
        try {
            if (value == null || value.isEmpty()) {
                throw new DomainException("El ID de pérdida de inventario no puede ser nulo o vacío.");
            }
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de pérdida de inventario no tiene un formato válido: " + value);
        }
        return new InventoryLossId(value);
    }

    @Override
    public String value() {
        return value;
    }
    
}

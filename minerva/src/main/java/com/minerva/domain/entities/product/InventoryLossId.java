package com.minerva.domain.entities.product;

import java.util.UUID;

import com.minerva.domain.exceptions.DomainException;

public class InventoryLossId {
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
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de pérdida de inventario no tiene un formato válido: " + value);
        }
        return new InventoryLossId(value);
    }
    
}

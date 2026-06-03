package com.minerva.domain.entities.stockEntry;

import java.util.UUID;

import com.minerva.domain.exceptions.DomainException;

public class StockEntryId {
    public final String value;

    private StockEntryId(String value) {
        this.value = value;
    }

    public static StockEntryId generate() {
        String value = UUID.randomUUID().toString();
        return new StockEntryId(value);
    }

    public static StockEntryId fromString(String value) throws DomainException {
        try {
            if (value == null || value.isEmpty()) {
                throw new DomainException("El ID de entrada de stock no puede ser nulo o vacío.");
            }
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de entrada de stock no tiene un formato válido: " + value);
        }
        return new StockEntryId(value);
    }
}

package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.entities.stockEntry.StockEntryId;
import com.minerva.domain.interfaces.ValueObject;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;

public class StockEntryIdImpl extends ValueObject<String> implements StockEntryId {
    private StockEntryIdImpl(String value) throws DomainException {
        super(value);
    }

    public static StockEntryIdImpl generate() {
        String value = UUID.randomUUID().toString();
        try {
            return new StockEntryIdImpl(value);
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de entrada de stock: " + e.getMessage(), e);
        }
    }

    public static StockEntryIdImpl fromString(String value) throws DomainException {
        try {
            if (value == null || value.isEmpty()) {
                throw new DomainException("El ID de entrada de stock no puede ser nulo o vacío.");
            }
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de entrada de stock no tiene un formato válido: " + value);
        }
        return new StockEntryIdImpl(value);
    }

    @Override
    public String value() {
        return value;
    }
    
}

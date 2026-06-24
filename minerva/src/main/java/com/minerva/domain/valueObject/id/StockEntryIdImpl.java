package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.entities.stockEntry.StockEntryId;
import com.minerva.domain.interfaces.ValueObject;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;

public class StockEntryIdImpl extends ValueObject<UUID> implements StockEntryId {
    private StockEntryIdImpl(UUID value) throws DomainException {
        super(value);
    }

    public static StockEntryIdImpl generate() {
        try {
            return new StockEntryIdImpl(UUID.randomUUID());
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de entrada de stock: " + e.getMessage(), e);
        }
    }

    public static StockEntryIdImpl fromString(String value) throws DomainException {
        try {
            return new StockEntryIdImpl(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de entrada de stock no tiene un formato válido: " + value);
        } catch (Exception e) {
            throw new UnexpectedDomainException(e.getMessage(), e);
        }
    }

    @Override
    public String asString() {
        return value.toString();
    }

    @Override
    public UUID value() {
        return value;
    }
    
}

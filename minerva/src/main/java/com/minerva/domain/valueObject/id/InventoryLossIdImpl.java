package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.entities.product.InventoryLossId;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.ValueObject;

public class InventoryLossIdImpl extends ValueObject<UUID> implements InventoryLossId {

    private InventoryLossIdImpl(UUID value) throws DomainException {
        super(value);
    }

    public static InventoryLossIdImpl generate() {
        try {
            return new InventoryLossIdImpl(UUID.randomUUID());
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de pérdida de inventario: " + e.getMessage(), e);
        }
    }

    public static InventoryLossIdImpl fromString(String value) throws DomainException {
        try {
            return new InventoryLossIdImpl(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de pérdida de inventario no tiene un formato válido: " + value);
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

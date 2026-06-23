package com.minerva.domain.valueObject.id;

import java.util.UUID;

import com.minerva.domain.entities.product.InventoryLossId;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.ValueObject;

public class InventoryLossIdImpl extends ValueObject<String> implements InventoryLossId {

    private InventoryLossIdImpl(String value) throws DomainException {
        super(value);
    }

    public static InventoryLossIdImpl generate() {
        String value = UUID.randomUUID().toString();
        try {
            return new InventoryLossIdImpl(value);
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de pérdida de inventario: " + e.getMessage(), e);
        }
    }

    public static InventoryLossIdImpl fromString(String value) throws DomainException {
        try {
            if (value == null || value.isEmpty()) {
                throw new DomainException("El ID de pérdida de inventario no puede ser nulo o vacío.");
            }
            UUID.fromString(value);
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de pérdida de inventario no tiene un formato válido: " + value);
        }
        return new InventoryLossIdImpl(value);
    }

    @Override
    public String value() {
        return value;
    }
    
}

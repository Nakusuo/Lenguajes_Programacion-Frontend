package com.minerva.domain.valueObject.id;

import com.minerva.domain.entities.product.ProductId;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.valueObject.ValueObject;

import java.util.UUID;

public class ProductIdImpl extends ValueObject<UUID>  implements ProductId {

    private ProductIdImpl(UUID value) throws DomainException {
        super(value);
    }

    public static ProductIdImpl generate() {
        try {
            return new ProductIdImpl(UUID.randomUUID());
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al generar el ID de product: " + e.getMessage(), e);
        }
    }

    public static ProductIdImpl fromString(String value) throws DomainException {
        try {
            return new ProductIdImpl(UUID.fromString(value));
        } catch (IllegalArgumentException e) {
            throw new DomainException("El ID de product no tiene un formato válido: " + value);
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

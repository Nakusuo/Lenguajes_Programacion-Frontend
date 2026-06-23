package com.minerva.domain.entities.sale;

import com.minerva.domain.constants.ReasonProductReturn;
import com.minerva.domain.valueObject.ProductQuantity;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.Entity;
import com.minerva.domain.valueObject.id.ProductReturnIdImpl;

import java.time.LocalDateTime;

class ProductReturn extends Entity<ProductReturnId> {
    private final ProductQuantity quantity;
    private final ReasonProductReturn reason;
    private final LocalDateTime registrationDate;

    public ProductReturn(ProductQuantity quantity, ReasonProductReturn reason) throws DomainException {
        if (quantity != null && quantity.isZeroOrLess()) throw new DomainException("La cantidad a devolver debe ser mayor a cero.");
        if (reason == null) throw new DomainException("La razón de la devolución no puede estar vacío.");

        super(ProductReturnIdImpl.generate());

        this.quantity = quantity;
        this.reason = reason;
        // DATOS INICIALES
        this.registrationDate = LocalDateTime.now();
    }

    public ProductQuantity getQuantity() {
        return quantity;
    }

    public ReasonProductReturn getReason() {
        return reason;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
}

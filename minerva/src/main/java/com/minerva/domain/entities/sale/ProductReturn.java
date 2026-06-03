package com.minerva.domain.entities.sale;

import com.minerva.domain.constants.ReasonProductReturn;
import com.minerva.domain.entities.product.ProductQuantity;
import com.minerva.domain.exceptions.DomainException;

import java.time.LocalDateTime;

class ProductReturn {
    private final ProductReturnId productReturnId;
    private final ProductQuantity quantity;
    private final ReasonProductReturn reason;
    private final LocalDateTime registrationDate;

    public ProductReturn(ProductQuantity quantity, ReasonProductReturn reason) throws DomainException {
        if (quantity != null && quantity.isZeroOrLess()) throw new DomainException("La cantidad a devolver debe ser mayor a cero.");
        if (reason == null) throw new DomainException("La razón de la devolución no puede estar vacío.");

        this.quantity = quantity;
        this.reason = reason;
        // DATOS INICIALES
        this.productReturnId = ProductReturnId.generate();
        this.registrationDate = LocalDateTime.now();
    }

    public ProductReturnId getId() {
        return productReturnId;
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

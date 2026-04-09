package com.minerva.domain.entities.sale;

import com.minerva.domain.constants.ReasonProductReturn;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.entities.product.ProductQuantity;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProductReturn {
    private final UUID productReturnId;
    private final ProductQuantity quantity;
    private final ReasonProductReturn reason;
    private final LocalDateTime registrationDate;

    private ProductReturn(ProductQuantity quantity, ReasonProductReturn reason) {
        this.quantity = quantity;
        this.reason = reason;
        // DATOS INICIALES
        this.productReturnId = UUID.randomUUID();
        this.registrationDate = LocalDateTime.now();
    }

    static Result<ProductReturn> create(ProductQuantity quantity, ReasonProductReturn reason) {
        if (quantity != null && quantity.isZeroOrLess()) return Result.fail("La cantidad a devolver debe ser mayor a cero.");
        if (reason == null) return Result.fail("La razón de la devolución no puede estar vacío.");

        return Result.success(new ProductReturn(quantity, reason));
    }

    public UUID getId() {
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

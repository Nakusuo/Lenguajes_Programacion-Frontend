package com.minerva.domain.entities.product;

import com.minerva.domain.constants.ReasonProductLoss;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.interfaces.Entity;
import com.minerva.domain.valueObject.ProductQuantity;
import com.minerva.domain.valueObject.id.InventoryLossIdImpl;
import com.minerva.domain.valueObject.id.ProductName;

import java.time.LocalDateTime;

public class InventoryLoss extends Entity {

    private final ProductName productName;
    private final ProductQuantity quantity;
    private ReasonProductLoss reason;
    private String observation;
    private final LocalDateTime registrationDate;

    public InventoryLoss(
            ProductName productName,
            ProductQuantity quantity,
            ReasonProductLoss reason,
            String observation
    ) throws DomainException {

        if (productName == null) throw new DomainException("El nombre del producto no puede estar vacío.");
        if (quantity != null && quantity.isZeroOrLess()) throw new DomainException("La cantidad debe ser mayor a cero.");
        if (reason == null) throw new DomainException("Debe especificar la razón de la pérdida.");

        super(InventoryLossIdImpl.generate());

        this.productName = productName;
        this.quantity = quantity;
        this.reason = reason;
        this.observation = observation;
        // VALORES POR DEFECTO
        this.registrationDate = LocalDateTime.now();
    }

    public ProductName getProductName() {
        return productName;
    }

    public ProductQuantity getQuantity() {
        return quantity;
    }

    public String getObservation() {
        return observation;
    }

    public ReasonProductLoss getReason() {
        return reason;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

}

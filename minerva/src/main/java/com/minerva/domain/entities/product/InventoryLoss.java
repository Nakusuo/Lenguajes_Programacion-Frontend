package com.minerva.domain.entities.product;

import com.minerva.domain.constants.ReasonProductLoss;
import com.minerva.domain.entities.shared.Result;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class InventoryLoss {

    private final UUID inventoryLossId;
    private final ProductId productNameId;
    private final ProductQuantity quantity;
    private ReasonProductLoss reason;
    private String observation;
    private final LocalDateTime registrationDate;

    private InventoryLoss(ProductId productNameId, ProductQuantity quantity, ReasonProductLoss reason, String observation) {
        this.productNameId = productNameId;
        this.quantity = quantity;
        this.reason = reason;
        this.observation = observation;
        // VALORES POR DEFECTO
        this.inventoryLossId = UUID.randomUUID();
        this.registrationDate = LocalDateTime.now();
    }

    // Poner parametros a observacion
    static Result<InventoryLoss> create(ProductId productNameId, ProductQuantity quantity, ReasonProductLoss reason, String observation) {
        if (productNameId == null) return Result.fail("El nombre del producto no puede estar vacío.");
        if (quantity != null && quantity.isZeroOrLess()) return Result.fail("La cantidad debe ser mayor a cero.");
        if (reason == null) return Result.fail("Debe especificar la razón de la pérdida.");

        InventoryLoss loss = new InventoryLoss(productNameId, quantity, reason, observation);

        return Result.success(loss);
    }

    public UUID getInventoryLossId() {
        return inventoryLossId;
    }

    public ProductId getProductNameId() {
        return productNameId;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        InventoryLoss that = (InventoryLoss) o;
        return Objects.equals(inventoryLossId, that.inventoryLossId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(inventoryLossId);
    }
}

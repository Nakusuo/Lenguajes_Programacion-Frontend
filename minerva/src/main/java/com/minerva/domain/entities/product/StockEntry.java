package com.minerva.domain.entities.product;

import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.entities.shared.Money;
import com.minerva.domain.entities.supplier.SupplierId;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class StockEntry {

    private final UUID stockEntryId;
    private final ProductId productNameId;
    private final SupplierId supplierNameId;
    private final Money priceUnit;
    private final ProductQuantity quantity;
    private final LocalDateTime expirationDate;
    private final LocalDateTime registrationDate;

    private StockEntry(UUID stockEntryId, ProductId productNameId, SupplierId supplierNameId, Money priceUnit, ProductQuantity quantity, LocalDateTime expirationDate, LocalDateTime registrationDate) {
        this.stockEntryId = stockEntryId;
        this.productNameId = productNameId;
        this.supplierNameId = supplierNameId;
        this.priceUnit = priceUnit;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.registrationDate = registrationDate;
    }

    static Result<StockEntry> create(ProductId productNameId, SupplierId supplierNameId, Money priceUnit, ProductQuantity quantity, LocalDateTime expirationDate) {

        if (productNameId == null) return Result.fail("El nombre del producto no puede estar vacío.");
        if (supplierNameId == null) return Result.fail("El nombre del proveedor no puede estar vacío.");
        if (priceUnit != null && priceUnit.isZeroOrLess()) return Result.fail("El precio del producto debe ser mayor a 0.");
        if (quantity != null && quantity.isZeroOrLess()) return Result.fail("La cantidad del producto debe ser mayor a 0.");

        if (expirationDate != null && (expirationDate.isBefore(LocalDateTime.now()) || expirationDate.isEqual(LocalDateTime.now())))
            return Result.fail("La fecha de expiración debe ser posterior a la fecha actual.");

        UUID stockEntryId = UUID.randomUUID();
        LocalDateTime registrationDate = LocalDateTime.now();

        StockEntry stockEntry = new StockEntry(stockEntryId, productNameId, supplierNameId, priceUnit, quantity, expirationDate, registrationDate);

        return Result.success(stockEntry);
    }

    public UUID getId() {
        return stockEntryId;
    }

    public ProductId getProductNameId() {
        return productNameId;
    }

    public SupplierId getSupplierNameId() {
        return supplierNameId;
    }

    public Money getPriceUnit() {
        return priceUnit;
    }

    public ProductQuantity getQuantity() {
        return quantity;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        StockEntry that = (StockEntry) o;
        return Objects.equals(stockEntryId, that.stockEntryId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(stockEntryId);
    }
}

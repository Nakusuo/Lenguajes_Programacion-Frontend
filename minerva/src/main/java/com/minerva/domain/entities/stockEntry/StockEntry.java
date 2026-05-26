package com.minerva.domain.entities.stockEntry;

import com.minerva.domain.entities.product.ProductId;
import com.minerva.domain.entities.product.ProductQuantity;
import com.minerva.domain.entities.shared.Money;
import com.minerva.domain.entities.supplier.SupplierId;
import com.minerva.domain.exceptions.DomainException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class StockEntry {

    private final UUID stockEntryId;
    private final ProductId productNameId;
    private final SupplierId supplierNameId;
    private final Money unitPrice;
    private final ProductQuantity quantity;
    private final LocalDateTime expirationDate;
    private final LocalDateTime registrationDate;

    public StockEntry(
            String productNameId,
            String supplierNameId,
            BigDecimal unitPrice,
            BigDecimal quantity,
            LocalDateTime expirationDate
    ) throws DomainException {

        this.productNameId = new ProductId(productNameId);
        this.supplierNameId = new SupplierId(supplierNameId);
        this.unitPrice = new Money(unitPrice);
        this.quantity = new ProductQuantity(quantity);

        if (this.unitPrice.isZeroOrLess()) throw new DomainException("El precio del producto debe ser mayor a 0.");
        if (this.quantity.isZeroOrLess()) throw new DomainException("La cantidad del producto debe ser mayor a 0.");
        if (expirationDate != null &&(expirationDate.isBefore(LocalDateTime.now()) || expirationDate.isEqual(LocalDateTime.now()))) {
            throw new DomainException("La fecha de expiración debe ser posterior a la fecha actual.");
        }

        this.expirationDate = expirationDate;

        this.stockEntryId = UUID.randomUUID();
        this.registrationDate = LocalDateTime.now();
    }

    public UUID getStockEntryId() {
        return stockEntryId;
    }

    public SupplierId getSupplierNameId() {
        return supplierNameId;
    }

    public ProductId getProductNameId() {
        return productNameId;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public ProductQuantity getQuantity() {
        return quantity;
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

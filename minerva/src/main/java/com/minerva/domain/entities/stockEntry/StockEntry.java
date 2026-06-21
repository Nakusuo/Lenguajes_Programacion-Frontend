package com.minerva.domain.entities.stockEntry;

import com.minerva.domain.valueObject.id.ProductName;
import com.minerva.domain.valueObject.ProductQuantity;
import com.minerva.domain.valueObject.Money;
import com.minerva.domain.valueObject.id.SupplierName;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.interfaces.Entity;
import com.minerva.domain.valueObject.id.StockEntryId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public class StockEntry extends Entity {

    private final StockEntryId stockEntryId;
    private final ProductName productName;
    private final SupplierName supplierName;
    private final Money unitPrice;
    private final ProductQuantity quantity;
    // PUedes ser null
    private final LocalDateTime expirationDate;
    // --------------------
    private final LocalDateTime registrationDate;

    public StockEntry(
            String productName,
            String supplierName,
            BigDecimal unitPrice,
            BigDecimal quantity,
            LocalDateTime expirationDate
    ) throws DomainException {        

        this.productName = new ProductName(productName);
        this.supplierName = new SupplierName(supplierName);
        this.unitPrice = new Money(unitPrice);
        this.quantity = new ProductQuantity(quantity);

        StockEntryId tempId = StockEntryId.generate();
        super(tempId);

        if (this.unitPrice.isZeroOrLess()) throw new DomainException("El precio del producto debe ser mayor a 0.");
        if (this.quantity.isZeroOrLess()) throw new DomainException("La cantidad del producto debe ser mayor a 0.");
        if (expirationDate != null &&(expirationDate.isBefore(LocalDateTime.now()) || expirationDate.isEqual(LocalDateTime.now()))) {
            throw new DomainException("La fecha de expiración debe ser posterior a la fecha actual.");
        }

        this.expirationDate = expirationDate;

        this.stockEntryId = tempId;
        this.registrationDate = LocalDateTime.now();
    }

    public StockEntry(
            String stockEntryId,
            String productName,
            String supplierName,
            BigDecimal unitPrice,
            BigDecimal quantity,
            LocalDateTime expirationDate,
            LocalDateTime registrationDate
    ) {
        StockEntryId tempId;
        try {
            tempId = StockEntryId.fromString(stockEntryId);
            this.stockEntryId = tempId;
            this.productName = new ProductName(productName);
            this.supplierName = new SupplierName(supplierName);
            this.unitPrice = new Money(unitPrice);
            this.quantity = new ProductQuantity(quantity);
            this.expirationDate = expirationDate;
            this.registrationDate = registrationDate;
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al crear la entrada de stock: " + e.getMessage(), e);
        }
        super(tempId);
    }

    public StockEntryId getStockEntryId() {
        return stockEntryId;
    }

    public SupplierName getSupplierName() {
        return supplierName;
    }

    public ProductName getProductName() {
        return productName;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }

    public Optional<LocalDateTime> getExpirationDate() {
        if (expirationDate == null) return Optional.empty();
        return Optional.of(expirationDate);
    }

    public ProductQuantity getQuantity() {
        return quantity;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

}

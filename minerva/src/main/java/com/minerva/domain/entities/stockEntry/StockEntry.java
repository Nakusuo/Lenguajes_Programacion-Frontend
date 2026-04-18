package com.minerva.domain.entities.stockEntry;

import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.entities.product.ProductId;
import com.minerva.domain.entities.product.ProductQuantity;
import com.minerva.domain.entities.shared.Money;
import com.minerva.domain.entities.supplier.SupplierId;

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

    private StockEntry(
            UUID stockEntryId,
            ProductId productId,
            SupplierId supplierId,
            Money unitPrice,
            ProductQuantity quantity,
            LocalDateTime expirationDate,
            LocalDateTime registrationDate)
    {
        this.stockEntryId = stockEntryId;
        this.productNameId = productId;
        this.supplierNameId = supplierId;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.expirationDate = expirationDate;
        this.registrationDate = registrationDate;
    }

    public static StockEntry restore(
        UUID stockEntryId,
        String productId,
        String supplierId,
        BigDecimal unitPrice,
        BigDecimal quantity,
        LocalDateTime expirationDate,
        LocalDateTime registrationDate) 
    {

    return new StockEntry(
            stockEntryId,
            ProductId.of(productId).getData(),
            SupplierId.of(supplierId).getData(),
            Money.of(unitPrice).getData(),
            ProductQuantity.of(quantity).getData(),
            expirationDate,
            registrationDate
    );
}

    public static Result<StockEntry> create(
            String productNameId,
            String supplierNameId,
            BigDecimal unitPrice,
            BigDecimal quantity,
            LocalDateTime expirationDate)
    {
        Result<ProductId> productIdResult = ProductId.of(productNameId);
        if (productIdResult.isFail()) return Result.fail(productIdResult.getMessage());

        Result<SupplierId> supplierIdResult = SupplierId.of(supplierNameId);
        if (supplierIdResult.isFail()) return Result.fail(supplierIdResult.getMessage());

        Result<Money> unitPriceResult = Money.of(unitPrice);
        if (unitPriceResult.isFail()) return Result.fail(unitPriceResult.getMessage());

        Result<ProductQuantity> quantityResult = ProductQuantity.of(quantity);
        if (quantityResult.isFail()) return Result.fail(quantityResult.getMessage());

        if (unitPriceResult.getData().isZeroOrLess()) return Result.fail("El precio del producto debe ser mayor a 0.");
        if (quantityResult.getData().isZeroOrLess()) return Result.fail("La cantidad del producto debe ser mayor a 0.");

        if (expirationDate != null && (expirationDate.isBefore(LocalDateTime.now()) || expirationDate.isEqual(LocalDateTime.now())))
            return Result.fail("La fecha de expiración debe ser posterior a la fecha actual.");

        UUID stockEntryId = UUID.randomUUID();
        LocalDateTime registrationDate = LocalDateTime.now();

        StockEntry stockEntry = new StockEntry(stockEntryId, productIdResult.getData(), supplierIdResult.getData(), unitPriceResult.getData(), quantityResult.getData(), expirationDate, registrationDate);

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

    public Money getUnitPrice() {
        return unitPrice;
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

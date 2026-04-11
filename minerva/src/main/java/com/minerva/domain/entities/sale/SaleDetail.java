package com.minerva.domain.entities.sale;

import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.entities.product.ProductQuantity;
import com.minerva.domain.entities.product.ProductId;
import com.minerva.domain.entities.shared.Money;

import java.math.BigDecimal;
import java.util.UUID;

public class SaleDetail {
    private final UUID id;
    private final ProductId productNameId;
    private final ProductQuantity quantity;
    private final Money unitPrice;

    private SaleDetail(ProductId productNameId, ProductQuantity quantity, Money unitPrice) {
        this.id = UUID.randomUUID();
        this.productNameId = productNameId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    static Result<SaleDetail> create(ProductId productId, ProductQuantity quantity, Money unitPrice) {
        if (productId == null) return Result.fail("El PRODUCTO es requerido.");
        if (quantity != null && quantity.isZeroOrLess()) return Result.fail("La CANTIDAD DE PRODUCTO debe ser mayor a 0.");
        if (unitPrice != null && unitPrice.isZeroOrLess()) return Result.fail("El PRECIO UNITARIO debe ser mayor a 0.");

        return Result.success(new SaleDetail(productId, quantity, unitPrice));
    }

    public BigDecimal calculateTotal() {
        return unitPrice.value().multiply(quantity.value());
    }

    public UUID getId() {
        return id;
    }

    public ProductId getProductNameId() {
        return productNameId;
    }

    public ProductQuantity getQuantity() {
        return quantity;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }
}

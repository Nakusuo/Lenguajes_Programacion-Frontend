package com.minerva.domain.entities.sale;

import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.entities.product.ProductQuantity;
import com.minerva.domain.entities.product.ProductId;
import com.minerva.domain.entities.shared.Money;

import java.math.BigDecimal;
import java.util.UUID;

class SaleDetail {
    private final UUID id;
    private final ProductId productNameId;
    private final ProductQuantity quantity;
    private final Money unitPrice;

    public SaleDetail(ProductId productNameId, ProductQuantity quantity, Money unitPrice) throws DomainException {

        if (productNameId == null) throw new DomainException("El PRODUCTO es requerido.");
        if (quantity != null && quantity.isZeroOrLess()) throw new DomainException("La CANTIDAD DE PRODUCTO debe ser mayor a 0.");
        if (unitPrice != null && unitPrice.isZeroOrLess()) throw new DomainException("El PRECIO UNITARIO debe ser mayor a 0.");

        this.id = UUID.randomUUID();
        this.productNameId = productNameId;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public SaleDetail(String id, String productNameId, BigDecimal quantity, BigDecimal unitPrice) {
        try {
            this.id = UUID.fromString(id);
            this.productNameId = new ProductId(productNameId);
            this.quantity = new ProductQuantity(quantity);
            this.unitPrice = new Money(unitPrice);
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al crear el detalle de venta: " + e.getMessage(), e);
        }
    }

    // REVISAR ESTO QUE YA ME GANO EL SUEÑO, si este mensaje sigue aqui es porque no lo revisé.
    public Money calculateSubTotal() {
        try {
            return new Money(unitPrice.value.multiply(quantity.value));
        } catch (DomainException e) {
            // Si esto truena, recenle al de arriba
            throw new UnexpectedDomainException("Error al calcular el subtotal del detalle de venta: " + e.getMessage(), e);
        }
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

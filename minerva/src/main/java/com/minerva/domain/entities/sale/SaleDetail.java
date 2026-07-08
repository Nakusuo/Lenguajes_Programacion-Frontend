package com.minerva.domain.entities.sale;

import com.minerva.domain.entities.product.ProductId;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.interfaces.Entity;
import com.minerva.domain.valueObject.ProductQuantity;
import com.minerva.domain.valueObject.id.ProductName;
import com.minerva.domain.valueObject.Money;
import com.minerva.domain.valueObject.id.SaleDetailIdImpl;

import java.math.BigDecimal;

class SaleDetail extends Entity<SaleDetailId> {
    private final ProductId productName;
    private final ProductQuantity quantity;
    private final Money unitPrice;

    public SaleDetail(ProductName productName, ProductQuantity quantity, Money unitPrice) throws DomainException {

        if (productName == null) throw new DomainException("El PRODUCTO es requerido.");
        if (quantity != null && quantity.isZeroOrLess()) throw new DomainException("La CANTIDAD DE PRODUCTO debe ser mayor a 0.");
        if (unitPrice != null && unitPrice.isZeroOrLess()) throw new DomainException("El PRECIO UNITARIO debe ser mayor a 0.");

        super(SaleDetailIdImpl.generate());

        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public SaleDetail(String id, String productName, BigDecimal quantity, BigDecimal unitPrice) {
        SaleDetailIdImpl tempId;
        try {
            tempId = SaleDetailIdImpl.fromString(id);
            
            this.productName = new ProductName(productName);
            this.quantity = new ProductQuantity(quantity);
            this.unitPrice = new Money(unitPrice);
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al crear el detalle de venta: " + e.getMessage(), e);
        }
        
        super(tempId);
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

    public ProductId getProductId() {
        return productName;
    }

    public ProductQuantity getQuantity() {
        return quantity;
    }

    public Money getUnitPrice() {
        return unitPrice;
    }
}

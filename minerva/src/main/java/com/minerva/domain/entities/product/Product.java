package com.minerva.domain.entities.product;

import com.minerva.domain.constants.Category;
import com.minerva.domain.constants.GainStrategy;
import com.minerva.domain.constants.SaleType;
import com.minerva.domain.entities.shared.Money;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.MinimumAmountException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.services.PriceCalculator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.minerva.domain.services.Math.isDecimal;
import static com.minerva.domain.services.Math.isZeroOrLess;

public class Product {
    private final ProductId productNameId;
    private ProductQuantity stock;
    private GainStrategy gainStrategy;
    private Money gainAmount;
    //Puede ser null
    private ProductQuantity reorderLevel;
    private final BarCode barCode;
    //----------------------------------------------

    private SaleType saleType;
    private Money price;
    private final Category category;
    private final LocalDateTime registrationDate;

    // STOCK ENTRY debe estar en una capa anticorrupcion
    public Product(
            String productName,
            GainStrategy gainStrategy,
            BigDecimal gainAmount,
            BigDecimal reorderLevel,
            String barCode,
            SaleType saleType,
            BigDecimal initialStock,
            Category category,
            BigDecimal purchasePrice
    ) throws DomainException {
        this.productNameId = new ProductId(productName);
        this.stock = new ProductQuantity(initialStock);
        this.gainAmount = new Money(gainAmount);
        this.gainStrategy = gainStrategy;
        this.saleType = saleType;
        this.category = category;

        if (gainStrategy == null) throw new DomainException("Seleccione una estrategia de ganancia.");
        if (saleType == null) throw new DomainException("Seleccione el tipo de venta.");
        if (category == null) throw new DomainException("Seleccione una categoría.");
        if (this.gainAmount.isZeroOrLess()) throw new DomainException("El monto de ganancia debe ser mayor a cero.");


        if (reorderLevel == null ) {
            this.reorderLevel = null;
        } else {
            if (SaleType.UNIDAD.equals(saleType) && isDecimal(reorderLevel))
                throw new DomainException("El nivel de reposición no puede ser decimal para productos vendidos por unidad.");

            this.reorderLevel = new ProductQuantity(reorderLevel);
        }

        if (barCode == null) {
            if (SaleType.UNIDAD.equals(saleType))
                throw new DomainException("Ingrese el código de barras para productos vendidos por unidad.");
            this.barCode = null;
        } else {
            this.barCode = new BarCode(barCode);
        }

        Result<Money> priceResult = PriceCalculator.calculate(new Money(purchasePrice), gainStrategy, this.gainAmount);
        if (priceResult.isFail()) throw new DomainException(priceResult.getMessage());

        this.price = priceResult.getData();
        this.registrationDate = LocalDateTime.now();
    }

    public Product(
            String productName,
            GainStrategy gainStrategy,
            BigDecimal gainAmount,
            BigDecimal reorderLevel,
            String barCode,
            SaleType saleType,
            BigDecimal stock,
            Category category,
            BigDecimal price,
            LocalDateTime registrationDate
    ) {
        try {
            this.productNameId = new ProductId(productName);
            this.stock = new ProductQuantity(stock);
            this.gainAmount = new Money(gainAmount);
            this.gainStrategy = gainStrategy;
            this.saleType = saleType;
            this.category = category;
            this.reorderLevel = reorderLevel == null ? null : new ProductQuantity(reorderLevel);
            this.barCode = barCode == null ? null : new BarCode(barCode);
            this.price = new Money(price);
            this.registrationDate = registrationDate;

            } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al crear el producto: " + e.getMessage(), e);
        } 
    }

    // --------------------------------

    public Result<Void> processDeliveryFromSupplier(BigDecimal quantity) {
        return increaseStock(quantity);
    }

    public Result<Void> processSale(BigDecimal quantity) {
        return decreaseStock(quantity);
    }

    //----------------------------------

    private Result<Void> increaseStock(BigDecimal quantityToAdd) {
        try {
            ProductQuantity newStockValue = this.stock.add(new ProductQuantity(quantityToAdd));
            return updateStock(newStockValue);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
    }

    private Result<Void> decreaseStock(BigDecimal quantityToSubtract) {
        if (this.stock.isZero()) return Result.fail("No hay stock disponible para este producto.");

        try {
            ProductQuantity newStockValue = this.stock.subtract(new ProductQuantity(quantityToSubtract));

            updateStock(newStockValue);
            return Result.success(null);
        } catch (MinimumAmountException e) {
            if (isZeroOrLess(this.stock.value.subtract(quantityToSubtract)))
                return Result.fail("No hay suficiente stock para completar esta operación. Stock disponible: " + this.stock.value);

            return Result.fail(e.getMessage());
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
    }

    private Result<Void> updateStock(ProductQuantity newStockValue) {
        if (newStockValue == null)
            return Result.fail("El nuevo valor de stock no puede ser nulo.");

        if (this.saleType == SaleType.UNIDAD && newStockValue.isDecimal())
            return Result.fail("Este producto se maneja por unidades. Ingrese una cantidad entera.");
  
        this.stock = newStockValue;
        return Result.success(null);  
    }

    // -----------------------------------------------------
    public Result<Void> validateBulkAssociation(Product bulkProduct, ProductQuantity quantity) {
        if (bulkProduct == null) return Result.fail("El producto a granel no puede ser nulo.");
        if (quantity == null) return Result.fail("La cantidad no puede estar vacío");

        if (this.equals(bulkProduct)) return Result.fail("No es posible asociar un producto consigo mismo.");
        if (this.getSaleType() != SaleType.UNIDAD ) return Result.fail("El producto -- " + this.getNameId() + " -- se vende por unidad y no permite asociar otro producto.");

        if (bulkProduct.getSaleType() != SaleType.GRANEL) return Result.fail("El producto -- " + bulkProduct.getNameId() + " -- debe venderse a granel para poder ser asociado.");
        if (quantity.isZeroOrLess()) return Result.fail("La cantidad debe ser mayor a cero");

        return Result.success(null);
    }
    // ---------------------------------------------

    public ProductId getNameId() {
        return productNameId;
    }

    public Optional<BarCode> getBarCode() {
        if (barCode == null) return Optional.empty();
        return Optional.of(barCode);
    }

    public Money getGainAmount() {
        return gainAmount;
    }

    public ProductQuantity getStock() {
        return stock;
    }

    public Optional<ProductQuantity> getReorderLevel() {
        if (reorderLevel == null) return Optional.empty();
        return Optional.of(reorderLevel);
    }

    public GainStrategy getGainStrategy() {
        return gainStrategy;
    }

    public SaleType getSaleType() {
        return saleType;
    }

    public Category getCategory() {
        return category;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public Money getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(productNameId, product.productNameId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(productNameId);
    }
}

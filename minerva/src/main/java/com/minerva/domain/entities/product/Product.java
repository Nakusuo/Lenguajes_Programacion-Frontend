package com.minerva.domain.entities.product;

import com.minerva.domain.constants.Category;
import com.minerva.domain.constants.GainStrategy;
import com.minerva.domain.constants.SaleType;
import com.minerva.domain.entities.result.Result;
import com.minerva.domain.entities.Entity;
import com.minerva.domain.valueObject.BarCode;
import com.minerva.domain.valueObject.Money;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.MinimumAmountException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.valueObject.ProductQuantity;
import com.minerva.domain.valueObject.ProductName;
import com.minerva.domain.valueObject.id.ProductIdImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.minerva.domain.services.Math.isDecimal;
import static com.minerva.domain.services.Math.isZeroOrLess;

public class Product extends Entity<ProductId> {
    private final ProductName productName;
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
        super(ProductIdImpl.generate());
        this.productName = new ProductName(productName);
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

        Result<Money> priceResult = calculatePrice(new Money(purchasePrice), gainStrategy, this.gainAmount);
        if (priceResult.isFail()) throw new DomainException(priceResult.getMessage());

        this.price = priceResult.getData();
        this.registrationDate = LocalDateTime.now();
    }

    public Product(
            UUID productId,
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
        ProductId tempId;
        try {
            tempId = new ProductIdImpl(productId);
            this.productName = new ProductName(productName);
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
        super(tempId);
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

    private Result<Money> calculatePrice(Money purchasePrice, GainStrategy gainStrategy, Money gainAmount) {
        if (purchasePrice == null)
            return Result.fail("Se necesita un precio de compra para calcular el precio.");

        if (gainStrategy == null)
            return Result.fail("Se necesita una estrategia de ganancia para calcular el precio.");

        if (gainAmount == null)
            return Result.fail("Se necesita un monto de ganancia para calcular el precio.");

        BigDecimal finalPrice = switch (gainStrategy) {
            case INCREMENTAL -> purchasePrice.value.add(gainAmount.value);

            case PORCENTAJE -> purchasePrice.value.multiply(gainFactor(gainAmount));
        };

        try {
            return Result.success(new Money(finalPrice));
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Result<Money> calculateCost() {
        BigDecimal purchasePrice = switch (gainStrategy) {
            case INCREMENTAL -> price.value.subtract(gainAmount.value);

            case PORCENTAJE -> price.value.divide(
                    gainFactor(gainAmount),
                    Money.MAX_DECIMALS,
                    RoundingMode.HALF_UP
            );
        };

        try {
            return Result.success(new Money(purchasePrice));
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
    }

    private BigDecimal gainFactor(Money gainAmount) {
        return BigDecimal.ONE.add(
                gainAmount.value.divide(
                        BigDecimal.valueOf(100),
                        Money.MAX_DECIMALS,
                        RoundingMode.HALF_UP
                )
        );
    }
    // ---------------------------------------------

    public ProductName getNameId() {
        return productName;
    }

    public Optional<BarCode> getBarCode() {
        return Optional.ofNullable(barCode);
    }

    public Money getGainAmount() {
        return gainAmount;
    }

    public ProductQuantity getStock() {
        return stock;
    }

    public Optional<ProductQuantity> getReorderLevel() {
        return Optional.ofNullable(reorderLevel);
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

}

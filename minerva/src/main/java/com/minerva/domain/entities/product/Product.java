package com.minerva.domain.entities.product;

import com.minerva.domain.constants.Category;
import com.minerva.domain.constants.GainStrategy;
import com.minerva.domain.constants.SaleType;
import com.minerva.domain.entities.shared.Money;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.entities.stockEntry.StockEntry;
import com.minerva.domain.services.PriceCalculator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.minerva.domain.services.Math.isDecimal;

@SuppressWarnings("LombokGetterMayBeUsed")
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

    private Product(ProductId productNameId, ProductQuantity stock, GainStrategy gainStrategy, Money gainAmount, ProductQuantity reorderLevel, BarCode barCode, SaleType saleType, Money price,Category category, LocalDateTime registrationDate) {
        this.productNameId = productNameId;
        this.stock = stock;
        this.gainStrategy = gainStrategy;
        this.gainAmount = gainAmount;
        this.reorderLevel = reorderLevel;
        this.barCode = barCode;
        this.saleType = saleType;
        this.price = price;
        this.category = category;
        this.registrationDate = registrationDate;
    }

    public static Product restore (String id, String productNameId, BigDecimal stock, GainStrategy gainStrategy, BigDecimal gainAmount, BigDecimal reorderLevel, String barCode, SaleType saleType, Category category, LocalDateTime registrationDate, StockEntry stockEntry) {

    }

    // Stock entry debe estar en una capa anticorrupción
    public static Result<Product> create(String productName, GainStrategy gainStrategy, BigDecimal gainAmount, BigDecimal reorderLevel, String barCode, SaleType saleType, Category category, StockEntry stockEntry) {

        if (gainStrategy == null) return Result.fail("Seleccione una estrategia de ganancia.");
        if (saleType == null) return Result.fail("Seleccione el tipo de venta.");
        if (category == null) return Result.fail("Seleccione una categoría.");


        Result<ProductId> productIdResult = ProductId.of(productName);
        if (productIdResult.isFail()) return Result.fail(productIdResult.getMessage());

        //----------------------------------
        Result<Money> gainAmountResult = Money.of(gainAmount);
        if (gainAmountResult.isFail()) return Result.fail(gainAmountResult.getMessage());

        if (gainAmountResult.getData().isZeroOrLess()) return Result.fail("El monto de ganancia debe ser mayor a cero.");

        //----------------------------------

        ProductQuantity reorderLevelValue = null;

        if (reorderLevel != null) {
            Result<ProductQuantity> reorderLevelResult = ProductQuantity.of(reorderLevel);
            if (reorderLevelResult.isFail()) return Result.fail(reorderLevelResult.getMessage());

            if (SaleType.UNIDAD.equals(saleType) && isDecimal(reorderLevelResult.getData().value())){
                return Result.fail("El nivel de reposición no puede ser decimal para productos vendidos por unidad.");
            }

            reorderLevelValue = reorderLevelResult.getData();
        }

        //----------------------------------

        BarCode barCodeValue = null;

        if (SaleType.UNIDAD.equals(saleType)) {

            if (barCode == null) return Result.fail("Ingrese el código de barras para productos vendidos por unidad.");

            Result<BarCode> barCodeResult = BarCode.of(barCode);
            if (barCodeResult.isFail()) return Result.fail(barCodeResult.getMessage());

            barCodeValue = barCodeResult.getData();
        }
        //----------------------------------
        Result<Money> priceResult = PriceCalculator.calculate(stockEntry, gainStrategy, gainAmountResult.getData());
        if (priceResult.isFail()) return Result.fail(priceResult.getMessage());

        //----------------------------------

        LocalDateTime registrationDate = LocalDateTime.now();
        ProductQuantity initialStock = stockEntry.getQuantity();
        

        Product productCreated = new Product(
                productIdResult.getData(),
                initialStock,
                gainStrategy,
                gainAmountResult.getData(),
                reorderLevelValue,
                barCodeValue,
                saleType,
                priceResult.getData(),
                category,
                registrationDate
        );

        return Result.success(productCreated);
    }

    //----------------------------------

    private Result<Void> increaseStock(BigDecimal quantityToAdd) {
        if (quantityToAdd == null) {
            return Result.fail("La cantidad a sumar no puede ser nula.");
        }

        BigDecimal newStockValue = this.getStock().value().add(quantityToAdd);

        return updateStock(newStockValue);
    }

    private Result<Void> decreaseStock(BigDecimal quantityToSubtract) {
        if (quantityToSubtract == null) {
            return Result.fail("La cantidad a restar no puede ser nula.");
        }

        BigDecimal newStockValue = this.getStock().value().subtract(quantityToSubtract);

        if (newStockValue.compareTo(BigDecimal.ZERO) < 0) {
            return Result.fail("No hay stock suficiente para realizar esta operación.");
        }

        return updateStock(newStockValue);
    }

    private Result<Void> updateStock(BigDecimal newStockValue) {
        Result<ProductQuantity> newStockResult = ProductQuantity.of(newStockValue);
        if (newStockResult.isFail()) return Result.fail(newStockResult.getMessage());

        ProductQuantity newStock = newStockResult.getData();

        if (this.saleType == SaleType.UNIDAD && isDecimal(newStock.value()))
            return Result.fail("Este producto se maneja por unidades. Ingrese una cantidad entera.");

        this.stock = newStock;
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

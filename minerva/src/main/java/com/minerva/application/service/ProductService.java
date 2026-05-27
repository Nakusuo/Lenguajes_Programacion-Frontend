package com.minerva.application.service;

import com.minerva.application.port.drivers.ProductUseCase;
import com.minerva.domain.constants.Category;
import com.minerva.domain.constants.GainStrategy;
import com.minerva.domain.constants.SaleType;
import com.minerva.domain.entities.product.*;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.repositories.ProductRepository;
import com.minerva.domain.repositories.SupplierRepository;
import com.minerva.domain.entities.stockEntry.StockEntry;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ProductService implements ProductUseCase {
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    public ProductService(ProductRepository productRepository, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Result<Void> registerProduct(String productName,
                                        GainStrategy gainStrategy,
                                        BigDecimal gainAmount,
                                        BigDecimal reorderLevel,
                                        String barCode,
                                        SaleType saleType,
                                        Category category,
                                        String purchasedFromSupplierId,
                                        BigDecimal purchaseUnitPrice,
                                        BigDecimal purchaseQuantity,
                                        LocalDateTime purchaseExpirationDate
                                        ) {

        Product productCreated;
        try {
            productCreated = new Product(productName, gainStrategy, gainAmount, reorderLevel, barCode, saleType, purchaseQuantity, category, purchaseUnitPrice);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }

        if (productRepository.existsById(productCreated.getNameId()))
            return Result.fail("Ya existe un producto con el mismo nombre.");


        if (productCreated.getBarCode().isPresent() && productRepository.existByBarCode(productCreated.getBarCode().get()))
            return Result.fail("Ya existe un producto con el mismo código de barras.");

        // Esto debe tratarsse como una operación atómica, por lo que si falla el registro de la entrada de stock, se debería eliminar el producto registrado para mantener la consistencia. Esto se puede
        productRepository.save(productCreated);
        Result<Void> stockEntryResult = registerStockEntry(productName, purchasedFromSupplierId, purchaseUnitPrice, purchaseQuantity, purchaseExpirationDate);

        if (stockEntryResult.isFail()) return Result.fail(stockEntryResult.getMessage());
        // -------------------

        return Result.success(null);
    }

    @Override
    public Result<Void> registerStockEntry(String productId, String supplierNameId, BigDecimal unitPrice, BigDecimal quantity, LocalDateTime expirationDate) {
        StockEntry stockEntryCreated;
        try {
            stockEntryCreated = new StockEntry(productId, supplierNameId, unitPrice, quantity, expirationDate);
            
            if  (!productRepository.existsById(stockEntryCreated.getProductNameId()))
                return Result.fail("El producto no esta registrado.");
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }

        productRepository.save(stockEntryCreated);

        return Result.success(null);
    }

    @Override
    public Result<Void> registerUnitToBulk(String unitProductId, String bulkProductId, BigDecimal quantity) {
        Optional<Product> unitProductOptional = findProductById(unitProductId);
        if (unitProductOptional.isEmpty()) return Result.fail("No se encontró el producto vendido por unidad.");

        Optional<Product> bulkProductOptional = findProductById(bulkProductId);
        if (bulkProductOptional.isEmpty()) return Result.fail("No se encontró el producto vendido a granel.");

        Product unitProduct = unitProductOptional.get();
        Product bulkProduct = bulkProductOptional.get();

        ProductQuantity productQuantity;
        try {
            productQuantity = new ProductQuantity(quantity);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }

        Result<Void> validationResult = unitProduct.validateBulkAssociation(bulkProduct, productQuantity);
        if (validationResult.isFail()) return validationResult;

        productRepository.saveUnitToBulk(unitProduct.getNameId(), bulkProduct.getNameId(), productQuantity);

        return Result.success(null);
    }

    // --------------------- READ ---------------------
    @Override
    public Optional<Product> findProductById(String productId) {     
        ProductId productIdObj;
        try {
            productIdObj = new ProductId(productId);
        } catch (DomainException e) {
            return Optional.empty();
        }  
        return productRepository.findById(productIdObj);
    }

    @Override
    public Optional<Product> findProductByBarCode(String barCode) {
        BarCode barCodeObj;
        try {
            barCodeObj = new BarCode(barCode);
        } catch (DomainException e) {
            return Optional.empty();
        }
   
        return productRepository.findByBarCode(barCodeObj);
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAllProducts();
    }

}

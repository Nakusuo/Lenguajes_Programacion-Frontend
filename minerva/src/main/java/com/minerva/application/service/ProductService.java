package com.minerva.application.service;

import com.minerva.application.port.drivers.ProductUseCase;
import com.minerva.domain.constants.Category;
import com.minerva.domain.constants.GainStrategy;
import com.minerva.domain.constants.SaleType;
import com.minerva.domain.entities.product.*;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.repositories.ProductRepository;
import com.minerva.domain.repositories.SupplierRepository;

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
                                        Category category) {

        Result<Product> productResult = Product.create(productName, gainStrategy, gainAmount, reorderLevel, barCode, saleType, category);
        if (productResult.isFail()) return Result.fail(productResult.getMessage());

        Product productCreated = productResult.getData();

        if (productRepository.existsById(productCreated.getNameId()))
            return Result.fail("Ya existe un producto con el mismo nombre.");


        if (productCreated.getBarCode().isPresent() && productRepository.existByBarCode(productCreated.getBarCode().get()))
            return Result.fail("Ya existe un producto con el mismo código de barras.");

        productRepository.save(productCreated);

        return Result.success(null);
    }

    @Override
    public Result<Void> registerStockEntry(String productId, String supplierNameId, BigDecimal priceUnit, BigDecimal quantity, LocalDateTime expirationDate) {

        Optional<Product> optional = findProductById(productId);
        if (optional.isEmpty()) return Result.fail("El producto no esta registrado.");


        Product product = optional.get();

        Result<StockEntry> stockEntryResult = product.generateStockEntry(supplierNameId, priceUnit, quantity, expirationDate);
        if (stockEntryResult.isFail()) return Result.fail(stockEntryResult.getMessage());

        productRepository.save(stockEntryResult.getData());

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

        Result<ProductQuantity> quantityResult = ProductQuantity.of(quantity);
        if (quantityResult.isFail()) return Result.fail(quantityResult.getMessage());

        ProductQuantity productQuantity = quantityResult.getData();

        Result<Void> validationResult = unitProduct.validateBulkAssociation(bulkProduct, productQuantity);
        if (validationResult.isFail()) return validationResult;

        productRepository.saveUnitToBulk(unitProduct.getNameId(), bulkProduct.getNameId(), productQuantity);

        return Result.success(null);
    }

    @Override
    public Optional<Product> findProductById(String productId) {
        Result<ProductId> productIdResult = ProductId.of(productId);
        if (productIdResult.isFail()) return Optional.empty();
        
        return productRepository.findById(productIdResult.getData());
    }

    @Override
    public Optional<Product> findProductByBarCode(String barCode) {
        Result<BarCode> barCodeResult = BarCode.of(barCode);
        if (barCodeResult.isFail()) return Optional.empty();

        return productRepository.findByBarCode(barCodeResult.getData());
    }

    @Override
    public List<Product> findAllProducts() {
        return productRepository.findAllProducts();
    }

}

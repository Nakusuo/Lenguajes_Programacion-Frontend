package com.minerva.application.service;

import com.minerva.application.port.drivers.ProductUseCase;
import com.minerva.domain.constants.Category;
import com.minerva.domain.constants.GainStrategy;
import com.minerva.domain.constants.Permission;
import com.minerva.domain.constants.Role;
import com.minerva.domain.constants.SaleType;
import com.minerva.domain.entities.product.*;
import com.minerva.domain.entities.result.Result;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnauthorizedActionException;
import com.minerva.domain.repositories.ProductRepository;
import com.minerva.domain.repositories.SupplierRepository;
import com.minerva.domain.repositories.UserRepository;
import com.minerva.domain.entities.stockEntry.StockEntry;
import com.minerva.domain.valueObject.BarCode;
import com.minerva.domain.valueObject.ProductQuantity;
import com.minerva.domain.valueObject.id.AllId;
import com.minerva.domain.valueObject.id.ProductName;
import com.minerva.domain.valueObject.id.UserName;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ProductService extends Service implements ProductUseCase {
    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    public ProductService(Role userRole, UserName userName, UserRepository userRepository, ProductRepository productRepository, SupplierRepository supplierRepository) {
        super(userRole, userName, userRepository);
        this.productRepository = productRepository;
        this.supplierRepository = supplierRepository;
    }

    // --------------------- WRITE ---------------------
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
                                        ) throws UnauthorizedActionException {
        
        if (getUserRole().lacksPermission(Permission.PRODUCT_REGISTER)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para registrar productos.");

        Product productCreated;
        StockEntry stockEntryCreated;
        try {
            productCreated = new Product(productName, gainStrategy, gainAmount, reorderLevel, barCode, saleType, purchaseQuantity, category, purchaseUnitPrice);
            stockEntryCreated = new StockEntry(productName, purchasedFromSupplierId, purchaseUnitPrice, purchaseQuantity, purchaseExpirationDate);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }

        if (productRepository.existsById(productCreated.getNameId()))
            return Result.fail("Ya existe un producto con el mismo nombre.");


        if (productCreated.getBarCode().isPresent() && productRepository.existByBarCode(productCreated.getBarCode().get()))
            return Result.fail("Ya existe un producto con el mismo código de barras.");


        if (!supplierRepository.existsById(stockEntryCreated.getSupplierName()))
            return Result.fail("El proveedor no esta registrado.");

        // Esto debe tratarsse como una operación atómica, por lo que si falla el registro de la entrada de stock, se debería eliminar el producto registrado para mantener la consistencia. Esto se puede
        productRepository.registerProduct(productCreated, stockEntryCreated);
        // -------------------

        registerUserAction(Permission.PRODUCT_REGISTER, productCreated.getId());
        return Result.success(null);
    }

    // OJAZOOOOOOO se puede mejorar el control de expeciones a la ora de crear los id
    @Override
    public Result<Void> registerStockEntry(String productName, String supplierName, BigDecimal unitPrice, BigDecimal quantity, LocalDateTime expirationDate) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.PRODUCT_REGISTER_STOCK_ENTRY)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para registrar entradas de stock.");
        
        StockEntry stockEntryCreated;
        try {
            stockEntryCreated = new StockEntry(productName, supplierName, unitPrice, quantity, expirationDate);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }


        Optional<Product> product;

        try {
            product = productRepository.findById(new ProductName(productName));
        } catch (DomainException e) {
            return Result.fail("El producto no esta registrado.");
        }

        if (product.isEmpty())
            return Result.fail("El producto no esta registrado.");

        Product productToUpdate = product.get();

        productToUpdate.processDeliveryFromSupplier(stockEntryCreated.getQuantity().value);

        if (!supplierRepository.existsById(stockEntryCreated.getSupplierName()))
            return Result.fail("El proveedor no esta registrado.");

        productRepository.saveStockEntry(stockEntryCreated, productToUpdate);
    
        registerUserAction(Permission.PRODUCT_REGISTER_STOCK_ENTRY, stockEntryCreated.getId());
        return Result.success(null);
    }

    @Override
    public Result<Void> registerUnitToBulk(String unitProductId, String bulkProductId, BigDecimal quantity) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.PRODUCT_ASSOCIATE_UNIT_TO_BULK)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para asociar productos vendidos por unidad a productos vendidos a granel.");

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

        // ESTO NO TIENE SENTIDO XD, porque se supone que es una llave compuesta, pero aqui solo registro un id de product
        registerUserAction(Permission.PRODUCT_ASSOCIATE_UNIT_TO_BULK, unitProduct.getId());
        return Result.success(null);
    }

    // --------------------- READ ---------------------
    @Override
    public Optional<Product> findProductById(String productId) throws UnauthorizedActionException {     
        if (getUserRole().lacksPermission(Permission.PRODUCT_FIND_BY_ID)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para buscar productos por ID.");
        try {
            return productRepository.findById(new ProductName(productId)).map(product -> {
                registerUserAction(Permission.PRODUCT_FIND_BY_ID, product.getId());
                return product;
            });
        } catch (DomainException e) {
            return Optional.empty();
        }  
    }

    @Override
    public Optional<Product> findProductByBarCode(String barCode) throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.PRODUCT_FIND_BY_BAR_CODE)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para buscar productos por código de barras.");

        try {
            return productRepository.findByBarCode(new BarCode(barCode)).map(product -> {
                registerUserAction(Permission.PRODUCT_FIND_BY_BAR_CODE, product.getId());
                return product;
            });
        } catch (DomainException e) {
            return Optional.empty();
        }        
    }

    @Override
    public List<Product> findAllProducts() throws UnauthorizedActionException {
        if (getUserRole().lacksPermission(Permission.PRODUCT_FIND_ALL)) 
            throw new UnauthorizedActionException("El usuario no tiene permiso para buscar todos los productos.");
        
        List<Product> products = productRepository.findAllProducts();
        registerUserAction(Permission.PRODUCT_FIND_ALL, new AllId());
        return products;      
    }

}

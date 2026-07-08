package com.minerva.application.port.drivers;

import com.minerva.domain.constants.Category;
import com.minerva.domain.constants.GainStrategy;
import com.minerva.domain.constants.SaleType;
import com.minerva.domain.entities.product.Product;
import com.minerva.domain.entities.result.Result;
import com.minerva.domain.exceptions.UnauthorizedActionException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductUseCase {
    // --------------------- WRITE ---------------------
    Result<Void> registerProduct(String productName,
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
                                 ) throws UnauthorizedActionException;

    Result<Void> registerStockEntry(String productId, String supplierNameId, BigDecimal unitPrice, BigDecimal quantity, LocalDateTime expirationDate) throws UnauthorizedActionException;
    Result<Void> registerUnitToBulk(String unitProductId, String bulkProductId, BigDecimal quantity) throws UnauthorizedActionException;

    // --------------------- READ ---------------------
    Optional<Product> findProductById(String productId) throws UnauthorizedActionException;
    Optional<Product> findProductByBarCode(String barCode) throws UnauthorizedActionException;
    List<Product> findAllProducts() throws UnauthorizedActionException;
}

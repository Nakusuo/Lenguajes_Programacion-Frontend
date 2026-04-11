package com.minerva.application.port.drivers;

import com.minerva.domain.constants.Category;
import com.minerva.domain.constants.GainStrategy;
import com.minerva.domain.constants.SaleType;
import com.minerva.domain.entities.product.Product;
import com.minerva.domain.entities.shared.Result;

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
                                 Category category);

    Result<Void> registerStockEntry(String productId, String supplierNameId, BigDecimal unitPrice, BigDecimal quantity, LocalDateTime expirationDate);
    Result<Void> registerUnitToBulk(String unitProductId, String bulkProductId, BigDecimal quantity);

    // --------------------- READ ---------------------
    Optional<Product> findProductById(String productId);
    Optional<Product> findProductByBarCode(String barCode);
    List<Product> findAllProducts();
}

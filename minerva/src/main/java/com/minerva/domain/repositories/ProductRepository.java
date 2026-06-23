package com.minerva.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.minerva.domain.entities.product.*;
import com.minerva.domain.entities.stockEntry.StockEntry;
import com.minerva.domain.valueObject.BarCode;
import com.minerva.domain.valueObject.ProductQuantity;
import com.minerva.domain.valueObject.id.ProductName;

public interface ProductRepository {
    void registerProduct(Product product, StockEntry stockEntry);
    void save(Product product);
    void saveStockEntry(StockEntry stockEntry, Product product);
    void saveUnitToBulk(ProductName unitProductName, ProductName bulkProductName, ProductQuantity quantity);

    boolean existsById(ProductId id);
    boolean existByBarCode(BarCode barCode);
    Optional<Product> findById(ProductId id);
    Optional<Product> findByBarCode(BarCode barCode);
    List<Product> findAllProducts();
    List<StockEntry> findAllEntriesByProductId(ProductId id);
}

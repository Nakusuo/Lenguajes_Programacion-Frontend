package com.minerva.domain.repositories;

import java.util.List;
import java.util.Optional;

import com.minerva.domain.entities.product.*;
import com.minerva.domain.entities.stockEntry.StockEntry;

public interface ProductRepository {
    void save(Product product);
    void save(StockEntry stockEntry);
    void saveUnitToBulk(ProductId unitProductId, ProductId bulkProductId, ProductQuantity quantity);

    boolean existsById(ProductId id);
    boolean existByBarCode(BarCode barCode);
    Optional<Product> findById(ProductId id);
    Optional<Product> findByBarCode(BarCode barCode);
    Optional<StockEntry> findLatestEntryBeforeToday(ProductId id);
    List<Product> findAllProducts();
    List<StockEntry> findAllEntriesByProductId(ProductId id);
}

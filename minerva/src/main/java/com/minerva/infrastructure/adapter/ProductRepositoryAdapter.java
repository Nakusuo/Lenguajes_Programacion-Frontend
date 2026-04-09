package com.minerva.infrastructure.adapter;

import com.minerva.domain.entities.product.*;
import com.minerva.domain.repositories.ProductRepository;
import com.minerva.infrastructure.persistence.entity.ProductEntity;
import com.minerva.infrastructure.persistence.entity.StockEntryEntity;
import com.minerva.infrastructure.persistence.entity.SupplierEntity;
import com.minerva.infrastructure.persistence.entity.UnitToBulkEntity;
import com.minerva.infrastructure.persistence.repository.JpaProductRepository;
import com.minerva.infrastructure.persistence.repository.JpaStockEntryRepository;
import com.minerva.infrastructure.persistence.repository.JpaUnitToBulkRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ProductRepositoryAdapter implements ProductRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final JpaProductRepository jpaProductRepository;
    private final JpaStockEntryRepository jpaStockEntryRepository;
    private final JpaUnitToBulkRepository jpaUnitToBulkRepository;

    public ProductRepositoryAdapter(JpaProductRepository jpaProductRepository, JpaStockEntryRepository jpaStockEntryRepository, JpaUnitToBulkRepository jpaUnitToBulkRepository) {
        this.jpaProductRepository = jpaProductRepository;
        this.jpaStockEntryRepository = jpaStockEntryRepository;
        this.jpaUnitToBulkRepository = jpaUnitToBulkRepository;
    }


    @Override
    public void save(Product product) {
        jpaProductRepository.save(new ProductEntity(
                product.getNameId().value(),
                product.getGainStrategy(),
                product.getGainAmount().value(),
                product.getStock().value(),
                product.getReorderLevel().get().value(),
                product.getBarCode().get().value(),
                product.getSaleType(),
                product.getCategory(),
                product.getRegistrationDate()                
        ));
    }

    @Override
    public void save(StockEntry stockEntry) {
        ProductEntity productEntity = entityManager.getReference(ProductEntity.class, stockEntry.getProductNameId().value());
        SupplierEntity supplierEntity = entityManager.getReference(SupplierEntity.class, stockEntry.getSupplierNameId().value());

        jpaStockEntryRepository.save(new StockEntryEntity(
                stockEntry.getId(),
                productEntity,
                supplierEntity,
                stockEntry.getPriceUnit().value(),
                stockEntry.getQuantity().value(),
                stockEntry.getExpirationDate(),
                stockEntry.getRegistrationDate()
        ));
    }

    @Override
    public void saveUnitToBulk(ProductId unitProductId, ProductId bulkProductId, ProductQuantity quantity) {
        ProductEntity unitProductEntity = entityManager.getReference(ProductEntity.class, unitProductId.value());
        ProductEntity bulkProductEntity = entityManager.getReference(ProductEntity.class, bulkProductId.value());

        jpaUnitToBulkRepository.save(new UnitToBulkEntity(
                new UnitToBulkEntity.UnitToBulkId(unitProductId.value(), bulkProductId.value()),
                unitProductEntity,
                bulkProductEntity,
                quantity.value(),
                LocalDateTime.now()
        ));
    }

    @Override
    public boolean existsById(ProductId id) {
        return jpaProductRepository.existsById(id.value());
    }

    @Override
    public boolean existByBarCode(BarCode barCode) {
        return jpaProductRepository.existsByBarCode(barCode.value());
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        
    }

    @Override
    public Optional<Product> findByBarCode(BarCode barCode) {
        return Optional.empty();
    }

    @Override
    public Optional<StockEntry> findLatestEntryBeforeToday(ProductId id) {
        return Optional.empty();
    }

    @Override
    public List<Product> findAllProducts() {
        
    }

    @Override
    public List<StockEntry> findAllEntriesByProductId(ProductId id) {
        return List.of();
    }
}

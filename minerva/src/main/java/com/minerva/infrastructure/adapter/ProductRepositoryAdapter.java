package com.minerva.infrastructure.adapter;

import com.minerva.domain.entities.product.*;
import com.minerva.domain.entities.stockEntry.StockEntry;
import com.minerva.domain.repositories.ProductRepository;
import com.minerva.domain.valueObject.BarCode;
import com.minerva.domain.valueObject.ProductQuantity;
import com.minerva.domain.valueObject.id.ProductId;
import com.minerva.infrastructure.persistence.entity.ProductEntity;
import com.minerva.infrastructure.persistence.entity.StockEntryEntity;
import com.minerva.infrastructure.persistence.entity.SupplierEntity;
import com.minerva.infrastructure.persistence.entity.UnitToBulkEntity;
import com.minerva.infrastructure.persistence.repository.JpaProductRepository;
import com.minerva.infrastructure.persistence.repository.JpaStockEntryRepository;
import com.minerva.infrastructure.persistence.repository.JpaUnitToBulkRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Transactional
    @Override
    public void registerProduct(Product product, StockEntry stockEntry) {
        jpaProductRepository.save(toEntity(product));
        jpaStockEntryRepository.save(toEntity(stockEntry));
    }

    @Override
    public void save(Product product) {
        jpaProductRepository.save(toEntity(product));
    }

    @Transactional
    @Override
    public void saveStockEntry(StockEntry stockEntry, Product product) {
        jpaStockEntryRepository.save(toEntity(stockEntry));
        jpaProductRepository.save(toEntity(product));
    }

    @Override
    public void saveUnitToBulk(ProductId unitProductId, ProductId bulkProductId, ProductQuantity quantity) {
        ProductEntity unitProductEntity = entityManager.getReference(ProductEntity.class, unitProductId.value);
        ProductEntity bulkProductEntity = entityManager.getReference(ProductEntity.class, bulkProductId.value);

        jpaUnitToBulkRepository.save(new UnitToBulkEntity(
                new UnitToBulkEntity.UnitToBulkId(unitProductId.value, bulkProductId.value),
                unitProductEntity,
                bulkProductEntity,
                quantity.value,
                LocalDateTime.now()
        ));
    }

    @Override
    public boolean existsById(ProductId id) {
        return jpaProductRepository.existsById(id.value);
    }

    @Override
    public boolean existByBarCode(BarCode barCode) {
        return jpaProductRepository.existsByBarCode(barCode.value);
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return jpaProductRepository.findById(id.value)
                .map(this::toDomain);
    }

    @Override
    public Optional<Product> findByBarCode(BarCode barCode) {
        return jpaProductRepository.findByBarCode(barCode.value)
                .map(this::toDomain);
    }

    // OJAZOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO
    // ESTO FALTA
    @Override
    public Optional<StockEntry> findLatestEntryBeforeToday(ProductId id) {
        return Optional.empty();
    }

    @Override
    public List<Product> findAllProducts() {
        return jpaProductRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public List<StockEntry> findAllEntriesByProductId(ProductId id) {
        return jpaStockEntryRepository.findByProductEntity_ProductNameId(id.value)
                .stream()
                .map(this::toDomain)
                .toList();
    }

    private Product toDomain(ProductEntity entity) {
        return new Product(
                entity.getProductNameId(),
                entity.getGainStrategy(),
                entity.getGainAmount(),
                entity.getReorderLevel(),
                entity.getBarCode(),
                entity.getSaleType(),
                entity.getStock(),
                entity.getCategory(),
                entity.getPrice(),
                entity.getRegistrationDate()
        );
    }

    private ProductEntity toEntity(Product product) {
        return new ProductEntity(
                product.getNameId().value,
                product.getGainStrategy(),
                product.getGainAmount().value,
                product.getPrice().value,
                product.getStock().value,
                product.getReorderLevel().get().value,
                product.getBarCode().get().value,
                product.getSaleType(),
                product.getCategory(),
                product.getRegistrationDate()
        );
    }

    private StockEntryEntity toEntity(StockEntry stockEntry) {
        ProductEntity productEntity =
                entityManager.getReference(ProductEntity.class, stockEntry.getProductNameId().value);

        SupplierEntity supplierEntity =
                entityManager.getReference(SupplierEntity.class, stockEntry.getSupplierNameId().value);

        return new StockEntryEntity(
                stockEntry.getStockEntryId().toString(),
                productEntity,
                supplierEntity,
                stockEntry.getUnitPrice().value,
                stockEntry.getQuantity().value,
                stockEntry.getExpirationDate().orElse(null),
                stockEntry.getRegistrationDate()
        );
    }

    private StockEntry toDomain(StockEntryEntity entity) {
        return new StockEntry(
                entity.getStockEntryId(),
                entity.getProductEntity().getProductNameId(),
                entity.getSupplierEntity().getSupplierNameId(),
                entity.getUnitPrice(),
                entity.getQuantity(),
                entity.getExpirationDate(),
                entity.getRegistrationDate()
        );
    }
}

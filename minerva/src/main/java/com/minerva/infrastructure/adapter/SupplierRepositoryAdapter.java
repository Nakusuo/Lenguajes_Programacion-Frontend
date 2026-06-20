package com.minerva.infrastructure.adapter;

import java.util.List;
import java.util.Optional;

import com.minerva.domain.valueObject.PhoneNumber;
import com.minerva.domain.valueObject.RUC;
import com.minerva.domain.entities.supplier.Supplier;
import com.minerva.domain.valueObject.id.SupplierName;
import com.minerva.domain.repositories.SupplierRepository;
import com.minerva.infrastructure.persistence.entity.SupplierEntity;
import com.minerva.infrastructure.persistence.repository.JpaSupplierRepository;

public class SupplierRepositoryAdapter implements SupplierRepository{

    private final JpaSupplierRepository jpaSupplierRepository;

    public SupplierRepositoryAdapter(JpaSupplierRepository jpaSupplierRepository) {
        this.jpaSupplierRepository = jpaSupplierRepository;
    }

    @Override
    public void save(Supplier supplier) {
        jpaSupplierRepository.save(toEntity(supplier));
    }

    @Override
    public boolean existsById(SupplierName id) {
        return jpaSupplierRepository.existsById(id.value);
    }

    @Override
    public boolean existsByRuc(RUC ruc) {
        return jpaSupplierRepository.existsByRuc(ruc.value);
    }

    @Override
    public boolean existsByPhoneNumber(PhoneNumber phoneNumber) {
        return jpaSupplierRepository.existsByPhoneNumber(phoneNumber.value);
    }

    @Override
    public List<Supplier> findAll() {
        return jpaSupplierRepository.findAll()
                .stream()
                .map(this::toDomain)
                .toList();
    }

    @Override
    public Optional<Supplier> findById(SupplierName id) {
        return jpaSupplierRepository.findById(id.value)
                .map(this::toDomain);
    }
    

    @Override
    public Optional<Supplier> findByRuc(RUC ruc) {
        return jpaSupplierRepository.findByRuc(ruc.value)
                .map(this::toDomain);
    }

    @Override
    public Optional<Supplier> findByPhone(PhoneNumber phoneNumber) {
        return jpaSupplierRepository.findByPhoneNumber(phoneNumber.value)
                .map(this::toDomain);
    }

    private SupplierEntity toEntity(Supplier supplier) {
        return new SupplierEntity(
                supplier.getSupplierName().value,
                supplier.getRuc().map(r -> r.value).orElse(null),
                supplier.getPhoneNumber().map(p -> p.value).orElse(null),
                supplier.getRegistrationDate()
        );
    }

    private Supplier toDomain(SupplierEntity entity) {
        return new Supplier(
                entity.getSupplierNameId(),
                entity.getRuc(),
                entity.getPhoneNumber(),
                entity.getRegistrationDate()
        );
    }
}

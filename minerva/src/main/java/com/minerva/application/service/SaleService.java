package com.minerva.application.service;

import com.minerva.domain.entities.customer.CustomerId;
import com.minerva.domain.entities.sale.Sale;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.repositories.SaleRepository;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.entities.sale.Sale.SaleItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SaleService {
    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public Result<Void> registerSale(String customerId, List<SaleItem> items) {
        try {
            Sale saleCreated = Sale.create(customerId, items);
            saleRepository.save(saleCreated);
            return Result.success(null);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Optional<Sale> findSaleById(UUID id) {
        return saleRepository.findById(id);
    }

    public List<Sale> findSalesByCustomerId(String customerId) {
        try {
            return saleRepository.findByCustomerId(new CustomerId(customerId));
        } catch (DomainException e) {
            return List.of();
        }
    }

    public List<Sale> findAllSales() {
        return saleRepository.findAll();
    }
    
}

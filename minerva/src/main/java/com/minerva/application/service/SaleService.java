package com.minerva.application.service;

import com.minerva.domain.entities.customer.CustomerId;
import com.minerva.domain.entities.sale.Sale;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.repositories.SaleRepository;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.entities.sale.Sale.SaleItem;
import com.minerva.domain.repositories.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SaleService {
    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;

    public SaleService(SaleRepository saleRepository, CustomerRepository customerRepository) {
        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
    }

    public Result<Void> registerSale(String customerId, List<Sale.PayData> pays, List<SaleItem> items) {
        try {
            Sale saleCreated = Sale.create(customerId, items);
            if (customerRepository.findById(new CustomerId(customerId)).isEmpty())
                return Result.fail("Cliente no encontrado.");

            Result<Void> addPaysResult = saleCreated.addPays(pays);
            if (addPaysResult.isFail()) return addPaysResult;

            saleRepository.save(saleCreated);
            return Result.success(null);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Result<Void> addPaymentToSale(UUID saleId, List<Sale.PayData> pays) {
        Optional<Sale> saleOpt = saleRepository.findById(saleId);
        if (saleOpt.isEmpty()) return Result.fail("Venta no encontrada.");

        Sale sale = saleOpt.get();

        Result<Void> addPaymentResult = sale.addPays(pays);
        if (addPaymentResult.isFail()) return addPaymentResult;

        saleRepository.save(sale);
        return Result.success(null);
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

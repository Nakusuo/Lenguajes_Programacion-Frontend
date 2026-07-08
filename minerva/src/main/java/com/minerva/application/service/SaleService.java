package com.minerva.application.service;

import com.minerva.domain.entities.product.Product;
import com.minerva.domain.entities.product.ProductId;
import com.minerva.domain.repositories.ProductRepository;
import com.minerva.domain.valueObject.ProductQuantity;
import com.minerva.domain.valueObject.id.CustomerName;
import com.minerva.domain.entities.sale.Sale;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.repositories.SaleRepository;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.entities.sale.Sale.SaleItem;
import com.minerva.domain.valueObject.id.SaleIdImpl;
import com.minerva.domain.repositories.CustomerRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


public class SaleService {
    private final SaleRepository saleRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    public SaleService(SaleRepository saleRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    // --------------------- WRITE ---------------------


    public Result<Void> registerSale(String customerId, List<Sale.PayData> pays, List<SaleItem> items) {
        try {
            Sale saleCreated = new Sale(customerId, items);
            if (customerRepository.findById(new CustomerName(customerId)).isEmpty())
                return Result.fail("Cliente no encontrado.");

            Result<Void> addPaysResult = saleCreated.addPays(pays);
            if (addPaysResult.isFail()) return addPaysResult;

            Map<ProductId, ProductQuantity> productQuantities = saleCreated.getProductQuantities();
            Set<Product> products = productRepository.findAllByIds(productQuantities.keySet());

            if (products.size() != productQuantities.size()) return Result.fail("Uno o más productos no encontrados.");

            for (Product product : products) {
                product.processSale(productQuantities.get(product.getId()).value);
            }

            saleRepository.save(saleCreated, products);
            return Result.success(null);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Result<Void> addPaymentToSale(String saleIdStr, List<Sale.PayData> pays) {
        SaleIdImpl saleIdImpl;
        try {
            saleIdImpl = SaleIdImpl.fromString(saleIdStr);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
        Optional<Sale> saleOpt = saleRepository.findById(saleIdImpl);
        if (saleOpt.isEmpty()) return Result.fail("Venta no encontrada.");

        Sale sale = saleOpt.get();

        Result<Void> addPaymentResult = sale.addPays(pays);
        if (addPaymentResult.isFail()) return addPaymentResult;

        saleRepository.updatePayments(sale);
        return Result.success(null);
    }

    // --------------------- READ ---------------------


    public Optional<Sale> findSaleById(String saleId) {
        try {
            return saleRepository.findById(SaleIdImpl.fromString(saleId));
        } catch (DomainException e) {
            return Optional.empty();
        }
    }

    public List<Sale> findSalesByCustomerId(String customerId) {
        try {
            return saleRepository.findByCustomerId(new CustomerName(customerId));
        } catch (DomainException e) {
            return List.of();
        }
    }

    public List<Sale> findAllSales() {
        return saleRepository.findAll();
    }
    
}

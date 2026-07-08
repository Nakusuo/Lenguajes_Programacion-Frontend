package com.minerva.domain.repositories;

import com.minerva.domain.entities.customer.CustomerId;
import com.minerva.domain.entities.product.Product;
import com.minerva.domain.entities.sale.PayId;
import com.minerva.domain.entities.sale.SaleDetailId;
import com.minerva.domain.entities.sale.SaleId;
import com.minerva.domain.entities.sale.Sale;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface SaleRepository {
    void save(Sale sale, Set<Product> products);
    Optional<Sale> findById(SaleId id);
    List<Sale> findByCustomerId(CustomerId customerId);
    List<Sale> findAll();
    List<Sale.SaleDetailDTO> findSaleDetailsById(SaleDetailId id);
    List<Sale.PayDTO> findPaysById(PayId id);
    void updatePayments(Sale sale);
}

package com.minerva.domain.repositories;

import com.minerva.domain.entities.sale.PayId;
import com.minerva.domain.valueObject.id.CustomerName;
import com.minerva.domain.entities.sale.Sale;
import com.minerva.domain.valueObject.id.SaleDetailId;
import com.minerva.domain.valueObject.id.SaleId;

import java.util.List;
import java.util.Optional;

public interface SaleRepository {
    void save(Sale sale);
    Optional<Sale> findById(SaleId id);
    List<Sale> findByCustomerId(CustomerName customerName);
    List<Sale> findAll();
    List<Sale.SaleDetailDTO> findSaleDetailsById(SaleDetailId id);
    List<Sale.PayDTO> findPaysById(PayId id);
}

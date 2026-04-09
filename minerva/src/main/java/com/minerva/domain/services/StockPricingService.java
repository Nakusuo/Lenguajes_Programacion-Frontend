package com.minerva.domain.services;

import com.minerva.domain.entities.product.ProductId;
import com.minerva.domain.entities.product.StockEntry;
import com.minerva.domain.entities.shared.Money;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.repositories.ProductRepository;

import java.util.Optional;

public class StockPricingService {

    private final ProductRepository productRepository;

    public StockPricingService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Result<Money> calculatePriceFromLastEntry(ProductId productId) {
        Optional<StockEntry> entryOpt = productRepository.findLatestEntryBeforeToday(productId);

        if (entryOpt.isEmpty()) {
            return Result.fail("No existe una entrada de stock del día anterior.");
        }

        return Result.success(entryOpt.get().getPriceUnit());
    }
}
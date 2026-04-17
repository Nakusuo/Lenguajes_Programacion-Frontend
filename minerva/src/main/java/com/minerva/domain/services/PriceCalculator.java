package com.minerva.domain.services;

import com.minerva.domain.constants.GainStrategy;
import com.minerva.domain.entities.product.Product;
import com.minerva.domain.entities.product.StockEntry;
import com.minerva.domain.entities.shared.Money;
import com.minerva.domain.entities.shared.Result;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class PriceCalculator {

    public static Result<Money> calculate(StockEntry stockEntry, GainStrategy gainStrategy, Money gainAmount) {
        if (stockEntry == null) return Result.fail("Se necesita una entrada de stock para calcular el precio.");
        if (gainStrategy == null) return Result.fail("Se necesita una estrategia de ganancia para calcular el precio.");
        if (gainAmount == null) return Result.fail("Se necesita un monto de ganancia para calcular el precio.");

        BigDecimal basePrice = stockEntry.getUnitPrice().value();
        BigDecimal finalPrice =

        switch (gainStrategy) {
            case INCREMENTAL -> basePrice.add(gainAmount.value());
            case PORCENTAJE -> basePrice.multiply(
                    BigDecimal.ONE.add(
                            gainAmount.value().divide(BigDecimal.valueOf(100), Money.MAX_DECIMALS, RoundingMode.HALF_UP)
                    )
            );
        };

        return Result.success(Money.of(finalPrice).getData());
    }

    public static Result<Money> calculate(StockEntry stockEntry, Product product) {
        return calculate(stockEntry, product.getGainStrategy(), product.getGainAmount());
    }
}
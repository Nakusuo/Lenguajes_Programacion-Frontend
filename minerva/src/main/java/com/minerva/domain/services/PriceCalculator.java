package com.minerva.domain.services;

import com.minerva.domain.constants.GainStrategy;
import com.minerva.domain.valueObject.Money;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.exceptions.DomainException;

import java.math.BigDecimal;
import java.math.RoundingMode;

// OJO, REVISAR LO DE ABAJO QUE HIZO LA IA PARA VER SI TIENE SENTIDO ESA DECISION DE ARQUTIECTURA
// OJO, esta clase no es un servicio de dominio, es una clase de utilidad, por lo que no debería estar en el paquete de servicios de dominio. Debería estar en un paquete de utilidades o algo similar.
// Recordatorio: Comenzar a usar los metodos de la clase Money para hacer las operaciones con dinero, en lugar de usar BigDecimal directamente. Esto ayudará a mantener la consistencia y a evitar errores relacionados con el manejo de dinero.
public class PriceCalculator {

    public static Result<Money> calculate(Money purchasePrice, GainStrategy gainStrategy, Money gainAmount) {
        if (purchasePrice == null) return Result.fail("Se necesita un precio de compra para calcular el precio.");
        if (gainStrategy == null) return Result.fail("Se necesita una estrategia de ganancia para calcular el precio.");
        if (gainAmount == null) return Result.fail("Se necesita un monto de ganancia para calcular el precio.");

        BigDecimal finalPrice =

        switch (gainStrategy) {
            case INCREMENTAL -> purchasePrice.value.add(gainAmount.value);
            case PORCENTAJE -> purchasePrice.value.multiply(
                    BigDecimal.ONE.add(
                            gainAmount.value.divide(BigDecimal.valueOf(100), Money.MAX_DECIMALS, RoundingMode.HALF_UP)
                    )
            );
        };

        try {
            return Result.success(new Money(finalPrice));
        } catch (DomainException domainException) {
            return Result.fail(domainException.getMessage());
        }
    }
}
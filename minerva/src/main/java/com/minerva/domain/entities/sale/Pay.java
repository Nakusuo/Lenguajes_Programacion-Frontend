package com.minerva.domain.entities.sale;

import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.constants.PaymentMethod;
import com.minerva.domain.entities.shared.Money;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Pay {
    private final UUID payId;
    private final Money amount;
    private final PaymentMethod paymentMethod;
    private final LocalDateTime registrationDate;

    private static final BigDecimal MIN_AMOUNT = new BigDecimal("0.10");

    private Pay(Money amount, PaymentMethod paymentMethod) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        // Valores por defecto
        this.payId = UUID.randomUUID();
        this.registrationDate = LocalDateTime.now();
    }

    static Result<Pay> create(Money amount, PaymentMethod paymentMethod) {
        if (paymentMethod == null) return Result.fail("El método de pago no puede estar vacío.");
        if (amount != null && amount.value().compareTo(MIN_AMOUNT) < 0) return Result.fail("El MONTO debe ser mayor o igual a S/" + MIN_AMOUNT);

        return Result.success(new Pay(amount, paymentMethod));
    }

    public Money getAmount() {
        return amount;
    }

    public UUID getId() {
        return payId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
}

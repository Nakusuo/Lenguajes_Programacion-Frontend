package com.minerva.domain.entities.sale;

import com.minerva.domain.constants.PaymentMethod;
import com.minerva.domain.entities.shared.Money;
import com.minerva.domain.exceptions.DomainException;

import java.time.LocalDateTime;
import java.util.UUID;

public class Pay {
    private final UUID payId;
    private final Money amount;
    private final PaymentMethod paymentMethod;
    private final LocalDateTime registrationDate;

    private static final Money MIN_AMOUNT = Money.tenCents();

    public Pay(Money amount, PaymentMethod paymentMethod) throws DomainException {
        if (paymentMethod == null) throw new DomainException("El método de pago no puede estar vacío.");
        if (amount != null && amount.isLessThan(MIN_AMOUNT)) throw new DomainException("El MONTO debe ser mayor o igual a S/" + MIN_AMOUNT);

        this.amount = amount;
        this.paymentMethod = paymentMethod;
        // Valores por defecto
        this.payId = UUID.randomUUID();
        this.registrationDate = LocalDateTime.now();
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

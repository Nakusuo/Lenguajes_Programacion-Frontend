package com.minerva.domain.entities.sale;

import com.minerva.domain.constants.PaymentMethod;
import com.minerva.domain.valueObject.Money;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.interfaces.Entity;
import com.minerva.domain.valueObject.id.PayId;

import java.math.BigDecimal;
import java.time.LocalDateTime;

class Pay extends Entity {
    private final PayId payId;
    private final Money amount;
    private final PaymentMethod paymentMethod;
    private final LocalDateTime registrationDate;

    private static final Money MIN_AMOUNT = Money.tenCents();

    Pay(Money amount, PaymentMethod paymentMethod) throws DomainException {
        if (paymentMethod == null) throw new DomainException("El método de pago no puede estar vacío.");
        if (amount != null && amount.isLessThan(MIN_AMOUNT)) throw new DomainException("El MONTO debe ser mayor o igual a S/" + MIN_AMOUNT);

        PayId tempId = PayId.generate();

        super(tempId);

        this.amount = amount;
        this.paymentMethod = paymentMethod;
        // Valores por defecto
        this.payId = tempId;
        this.registrationDate = LocalDateTime.now();
    }

    Pay(String payId, BigDecimal amount, PaymentMethod paymentMethod, LocalDateTime registrationDate) {
        PayId tempId;
        try {
            tempId = PayId.fromString(payId);
            this.payId = tempId;
            this.amount = new Money(amount);
            this.paymentMethod = paymentMethod;
            this.registrationDate = registrationDate;
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al crear el pago: " + e.getMessage(), e);
        }
        super(tempId);
    }

    public PayId getPayId() {
        return payId;
    }    
        
    public Money getAmount() {
        return amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
}

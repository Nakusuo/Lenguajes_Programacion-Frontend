package com.minerva.domain.entities.sale;

import com.minerva.domain.entities.product.ProductId;
import com.minerva.domain.entities.product.ProductQuantity;
import com.minerva.domain.entities.shared.Money;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.constants.PaymentMethod;
import com.minerva.domain.entities.customer.CustomerId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Sale {
    private final UUID saleId;
    private final CustomerId customerId;
    private final LocalDateTime registrationDate;

    private final List<Pay>  pays =  new LinkedList<>();
    private final List<SaleDetail> saleDetails = new LinkedList<SaleDetail>();

    public Sale(String customerNameId, String productId, BigDecimal unitPrice, BigDecimal quantity) throws DomainException {
        this.customerId = new CustomerId(customerNameId);
   
        // Valores por defecto
        this.saleId = UUID.randomUUID();
        this.registrationDate =  LocalDateTime.now();

        this.addDetail(productId, unitPrice, quantity);
    }

    public Result<Void> addDetail(String productIdStr, BigDecimal unitPrice, BigDecimal quantityBigDecimal) {
        try {
            ProductId productId = new ProductId(productIdStr);
            Money price = new Money(unitPrice);
            ProductQuantity quantity = new ProductQuantity(quantityBigDecimal);

            Optional<SaleDetail> existingDetailOpt = saleDetails.stream()
                    .filter(detail -> detail.getProductNameId().equals(productId))
                    .findFirst();

            if (existingDetailOpt.isPresent()) {
                SaleDetail existingDetail = existingDetailOpt.get();

                quantity = quantity.add(existingDetail.getQuantity());

                saleDetails.remove(existingDetail);
            }

            SaleDetail newDetail = new SaleDetail(productId, quantity, price);

            saleDetails.add(newDetail);

            return Result.success(null);

        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
    }

    public Result<Void> addPayment(BigDecimal amount, PaymentMethod paymentMethod) {
        if (isDueCanceled()) return Result.fail("La VENTA ya esta CANCELADA");

        Pay payCreated;
        try {
            payCreated = new Pay(new Money(amount), paymentMethod);
        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
        
        if (payCreated.getAmount().isGreaterThan(calculateAmountDue()))
            return Result.fail("El PAGO sobrepasa la DEUDA de la VENTA.");

        pays.add(payCreated);
        return Result.success(null);
    }

    public Money calculateTotal() {
        return saleDetails.stream()
                .map(SaleDetail::calculateSubTotal)
                .reduce(Money.zero(), Money::add);
    }

    public Money calculateTotalPaid() {
        return pays.stream()
                .map(Pay::getAmount)
                .reduce(Money.zero(), Money::add);
    }

    public Money calculateAmountDue() {
        try {
            return calculateTotal().subtract(calculateTotalPaid());
        } catch (DomainException e) {
            throw new RuntimeException("Error al calcular el monto adeudado: " + e.getMessage(), e);
        }
    }

    public boolean isDueCanceled() {
        return calculateAmountDue().isZero();
    }

    public UUID getId() {
        return saleId;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public CustomerId getCustomerId() {
        return customerId;
    }

    public List<Pay> getPays() {
        return List.copyOf(pays);
    }

    public List<SaleDetail> getSaleDetails() {
        return List.copyOf(saleDetails);
    }
}

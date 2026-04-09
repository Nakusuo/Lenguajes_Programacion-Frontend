package com.minerva.domain.entities.sale;

import com.minerva.domain.entities.product.ProductId;
import com.minerva.domain.entities.product.ProductQuantity;
import com.minerva.domain.entities.shared.Money;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.constants.PaymentMethod;
import com.minerva.domain.entities.customer.CustomerId;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class Sale {
    private final UUID saleId;
    private final CustomerId customerId;
    private final LocalDateTime registrationDate;

    private final List<Pay>  pays =  new LinkedList<>();
    private final List<SaleDetail> saleDetails = new LinkedList<SaleDetail>();

    private Sale(CustomerId customerId) {
        this.customerId = customerId;
        this.saleId = UUID.randomUUID();
        this.registrationDate =  LocalDateTime.now();
    }

    public static Result<Sale> create(String customerNameId, String productId, BigDecimal priceUnit, BigDecimal quantity) {

        Result<CustomerId> customerIdResult = CustomerId.of(customerNameId);
        if (customerIdResult.isFail()) return Result.fail(customerIdResult.getMessage());

        Sale saleCreated = new Sale(customerIdResult.getData());

        Result<Void> result = saleCreated.addDetail(productId, priceUnit, quantity);
        if (result.isFail()) return Result.fail(result.getMessage());

        return Result.success(saleCreated);
    }

    public Result<Void> addDetail(String productId, BigDecimal priceUnit, BigDecimal quantity) {
        Result<ProductId> productIdResult = ProductId.of(productId);
        if (productIdResult.isFail()) return Result.fail(productIdResult.getMessage());

        Result<Money> priceUnitResult = Money.of(priceUnit);
        if (priceUnitResult.isFail()) return Result.fail(priceUnitResult.getMessage());

        Result<ProductQuantity> quantityResult = ProductQuantity.of(quantity);
        if (quantityResult.isFail()) return Result.fail(quantityResult.getMessage());

        Result<SaleDetail> detailResult = SaleDetail.create(productIdResult.getData(), quantityResult.getData(), priceUnitResult.getData());
        if (detailResult.isFail()) return Result.fail(detailResult.getMessage());

        saleDetails.add(detailResult.getData());
        return Result.success(null);
    }

    public Result<Void> addPayment(BigDecimal amount, PaymentMethod paymentMethod) {
        if (isDueCanceled()) return Result.fail("La VENTA ya esta CANCELADA");

        Result<Money> amountResult = Money.of(amount);
        if (amountResult.isFail()) return Result.fail(amountResult.getMessage());

        Result<Pay> payResult = Pay.create(amountResult.getData(), paymentMethod);
        if (payResult.isFail()) return Result.fail(payResult.getMessage());

        Pay payCreated = payResult.getData();

        if (payCreated.getAmount().compareTo(calculateAmountDue()) > 0)
            return Result.fail("El PAGO sobrepasa la DEUDA de la VENTA.");

        pays.add(payCreated);
        return Result.success(null);
    }

    public BigDecimal calculateTotal() {
        return saleDetails.stream()
                .map(SaleDetail::calculateTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalPaid() {
        return pays.stream()
                .map(Pay::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateAmountDue() {
        return calculateTotal().subtract(calculateTotalPaid());
    }

    public boolean isDueCanceled() {
        return calculateAmountDue().compareTo(BigDecimal.ZERO) == 0;
    }

    public UUID getId() {
        return saleId;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public String getCustomerId() {
        return customerId.value();
    }
}

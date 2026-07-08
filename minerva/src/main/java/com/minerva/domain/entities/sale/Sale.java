package com.minerva.domain.entities.sale;

import com.minerva.domain.valueObject.id.ProductName;
import com.minerva.domain.valueObject.ProductQuantity;
import com.minerva.domain.valueObject.Money;
import com.minerva.domain.entities.product.ProductId;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.exceptions.DomainException;
import com.minerva.domain.exceptions.UnexpectedDomainException;
import com.minerva.domain.interfaces.Entity;
import com.minerva.domain.constants.PaymentMethod;
import com.minerva.domain.valueObject.id.CustomerName;
import com.minerva.domain.valueObject.id.SaleIdImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Sale extends Entity<SaleId> {
    private final CustomerName customerName;
    private final LocalDateTime registrationDate;

    private final List<Pay>  pays =  new LinkedList<>();
    private final Map<ProductId, SaleDetail> saleDetails = new HashMap<>();

    public Sale(String customerNameId, List<SaleItem> items) throws DomainException {    
        super(SaleIdImpl.generate());
        this.customerName = new CustomerName(customerNameId);
        if (items == null || items.isEmpty()) {
            throw new DomainException("La venta debe tener al menos un item");
        }

        for (SaleItem item : items) {
            Result<Void> addDetailResult = this.addDetail(
                item.productId,
                item.unitPrice,
                item.quantity
            );

            if (addDetailResult.isFail()) {
                throw new DomainException(addDetailResult.getMessage());
            }
        }

        // Valores por defecto
        this.registrationDate = LocalDateTime.now();
    }

    public Sale(String saleId, String customerNameId, LocalDateTime registrationDate, List<SaleDetailDTO> saleDetails, List<PayDTO> pays) {
        SaleIdImpl tempId;
        try {
            tempId = SaleIdImpl.fromString(saleId);
            this.customerName = new CustomerName(customerNameId);
            this.registrationDate = registrationDate;             
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al crear la venta: " + e.getMessage(), e);
        }
        super(tempId);

        try {
            if (saleDetails != null && !saleDetails.isEmpty()) {
                for (SaleDetailDTO detailDTO : saleDetails) {
                    SaleDetail saleDetail = new SaleDetail(
                        detailDTO.saleDetailId,
                        detailDTO.productId,
                        detailDTO.quantity,
                        detailDTO.unitPrice
                    );
                    this.saleDetails.put(saleDetail.getProductId(), saleDetail);
                }
            } else {
                throw new DomainException("La venta debe tener al menos un detalle");
            }
        } catch (DomainException e) {
            throw new UnexpectedDomainException("Error al crear la venta: " + e.getMessage(), e);
        }

        if (pays != null) {
            for (PayDTO payDTO : pays) {
                this.pays.add(new Pay(
                    payDTO.payId,
                    payDTO.amount,
                    payDTO.paymentMethod,
                    payDTO.registrationDate
                ));
            }
        }
    }

    public record SaleItem(String productId, BigDecimal quantity, BigDecimal unitPrice) {}

    public record SaleDetailDTO(String saleDetailId, String productId, BigDecimal quantity, BigDecimal unitPrice) {}

    public record PayDTO(String payId, BigDecimal amount, PaymentMethod paymentMethod, LocalDateTime registrationDate) {}

    public Result<Void> addDetail(String productNameStr, BigDecimal unitPrice, BigDecimal quantityBigDecimal) {
        try {
            ProductName productName = new ProductName(productNameStr);
            Money price = new Money(unitPrice);
            ProductQuantity quantity = new ProductQuantity(quantityBigDecimal);

            if (saleDetails.containsKey(productName)) {
                SaleDetail existingDetail = saleDetails.get(productName);

                quantity = quantity.add(existingDetail.getQuantity());

                saleDetails.remove(existingDetail.getProductId());
            };

            SaleDetail newDetail = new SaleDetail(productName, quantity, price);

            saleDetails.put(productName, newDetail);

            return Result.success(null);

        } catch (DomainException e) {
            return Result.fail(e.getMessage());
        }
    }

    private Result<Void> addPayment(BigDecimal amount, PaymentMethod paymentMethod) {
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

    public Result<Void> addPays(List<PayData> payData) {
        if (payData == null || payData.isEmpty())
            return Result.fail("La lista de pagos no puede estar vacía.");

        if (payData.size() != PaymentMethod.values().length)
            return Result.fail("Número de pagos no coincide con el número de métodos de pago.");

        for (PayData data : payData) {
            Result<Void> addPaymentResult = addPayment(data.amount, data.paymentMethod);
            if (addPaymentResult.isFail()) return addPaymentResult;
        }
        return Result.success(null);
    }
    
    public record PayData(BigDecimal amount, PaymentMethod paymentMethod) {}

    public Money calculateTotal() {
        return saleDetails.values().stream()
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
            throw new UnexpectedDomainException("Error al calcular el monto adeudado: " + e.getMessage(), e);
        }
    }

    public boolean isDueCanceled() {
        return calculateAmountDue().isZero();
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public CustomerName getCustomerId() {
        return customerName;
    }

    public List<PayDTO> getPays() {
        List<PayDTO> paysDTO = new ArrayList<>(pays.size());
        for (Pay pay : pays) {
            paysDTO.add(new PayDTO(
                pay.getId().toString(),
                pay.getAmount().value, 
                pay.getPaymentMethod(), 
                pay.getRegistrationDate()));
        }
        return paysDTO;
    }

    public List<SaleDetailDTO> getSaleDetails() {
        return saleDetails.values().stream()
                .map(detail -> new SaleDetailDTO(
                        detail.getId().toString(),
                        detail.getProductId().asString(),
                        detail.getQuantity().value,
                        detail.getUnitPrice().value))
                .toList();
    }

    public Map<ProductId, ProductQuantity> getProductQuantities() {
        HashMap<ProductId, ProductQuantity> productIds = new HashMap<>();

        for (SaleDetail detail : saleDetails.values()) {
            productIds.put(detail.getProductId(), detail.getQuantity());
        }

        return productIds;
    }

}

package com.minerva.infrastructure.rest.controller;

import com.minerva.application.service.SaleService;
import com.minerva.domain.entities.sale.Sale;
import com.minerva.domain.entities.shared.Result;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    // --------------------- WRITE ---------------------

    @PostMapping
    public ResponseEntity<?> registerSale(@Valid @RequestBody RegisterSaleRequest request) {

        Result<Void> result = saleService.registerSale(
                request.customerId(),
                request.payments(),
                request.items()
        );

        if (result.isFail()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{saleId}/payments")
    public ResponseEntity<?> addPaymentToSale(
            @PathVariable String saleId,
            @Valid @RequestBody AddPaymentRequest request) {

        Result<Void> result = saleService.addPaymentToSale(
                saleId,
                request.payments()
        );

        if (result.isFail()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    // --------------------- READ ---------------------

    @GetMapping("/{saleId}")
    public ResponseEntity<?> findById(@PathVariable String saleId) {

        return saleService.findSaleById(saleId)
                .map(sale -> ResponseEntity.ok(mapToResponse(sale)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SaleResponse>> findByCustomer(@PathVariable String customerId) {

        List<SaleResponse> sales = saleService.findSalesByCustomerId(customerId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(sales);
    }

    @GetMapping
    public ResponseEntity<List<SaleResponse>> findAll() {

        List<SaleResponse> sales = saleService.findAllSales()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(sales);
    }

    // --------------------- MAPPER ---------------------

    private SaleResponse mapToResponse(Sale sale) {
        return new SaleResponse(
                sale.getId().toString(),
                sale.getCustomerId().value,
                sale.getRegistrationDate().toString(),
                sale.getSaleDetails(),
                sale.getPays()
        );
    }

    // --------------------- DTOs ---------------------

    public record RegisterSaleRequest(
            @NotBlank String customerId,
            @NotNull List<Sale.SaleItem> items,
            @NotNull List<Sale.PayData> payments
    ) {}

    public record AddPaymentRequest(
            @NotNull List<Sale.PayData> payments
    ) {}

    public record SaleResponse(
            String saleId,
            String customerId,
            String registrationDate,
            List<Sale.SaleDetailDTO> details,
            List<Sale.PayDTO> payments
    ) {}
}
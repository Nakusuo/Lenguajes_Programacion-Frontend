package com.minerva.infrastructure.rest.controller;

import com.minerva.application.service.SupplierService;
import com.minerva.domain.entities.shared.Result;
import com.minerva.domain.entities.supplier.Supplier;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    // --------------------- WRITE ---------------------

    @PostMapping
    public ResponseEntity<?> registerSupplier(
            @Valid @RequestBody RegisterSupplierRequest request) {

        Result<Void> result = supplierService.register(
                request.supplierName(),
                request.ruc(),
                request.phoneNumber()
        );

        if (result.isFail()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{supplierName}/phone-number")
    public ResponseEntity<?> updatePhoneNumber(
            @PathVariable String supplierName,
            @Valid @RequestBody UpdatePhoneRequest request) {

        Result<Void> result = supplierService.updatePhoneNumber(
                supplierName,
                request.phoneNumber()
        );

        if (result.isFail()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{supplierName}/ruc")
    public ResponseEntity<?> updateRuc(
            @PathVariable String supplierName,
            @Valid @RequestBody UpdateRucRequest request) {

        Result<Void> result = supplierService.updateRuc(
                supplierName,
                request.ruc()
        );

        if (result.isFail()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    // --------------------- READ ---------------------

    @GetMapping
    public ResponseEntity<List<SupplierResponse>> getAll() {

        List<SupplierResponse> suppliers = supplierService.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{supplierName}")
    public ResponseEntity<?> findById(@PathVariable String supplierName) {

        return supplierService.findById(supplierName)
                .map(supplier -> ResponseEntity.ok(mapToResponse(supplier)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/ruc/{ruc}")
    public ResponseEntity<?> findByRuc(@PathVariable String ruc) {

        return supplierService.findByRuc(ruc)
                .map(supplier -> ResponseEntity.ok(mapToResponse(supplier)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<?> findByPhone(@PathVariable String phoneNumber) {

        return supplierService.findByPhone(phoneNumber)
                .map(supplier -> ResponseEntity.ok(mapToResponse(supplier)))
                .orElse(ResponseEntity.notFound().build());
    }

    // --------------------- MAPPER ---------------------

    private SupplierResponse mapToResponse(Supplier supplier) {
        return new SupplierResponse(
                supplier.getSupplierName().value,
                supplier.getRuc().map(r -> r.value).orElse(null),
                supplier.getPhoneNumber().map(p -> p.value).orElse(null)
        );
    }

    // --------------------- DTOs ---------------------

    public record RegisterSupplierRequest(
            @NotBlank String supplierName,
            String ruc,
            String phoneNumber
    ) {}

    public record UpdatePhoneRequest(
            @NotBlank String phoneNumber
    ) {}

    public record UpdateRucRequest(
            @NotBlank String ruc
    ) {}

    public record SupplierResponse(
            String supplierName,
            String ruc,
            String phoneNumber
    ) {}
}
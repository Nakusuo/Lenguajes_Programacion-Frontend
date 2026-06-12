package com.minerva.infrastructure.rest.controller;

import com.minerva.application.service.CustomerService;
import com.minerva.domain.entities.shared.Result;
import jakarta.validation.Valid;

import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    // --------------------- WRITE ---------------------
    @PostMapping
    public ResponseEntity<?> registerCustomer(
            @Valid @RequestBody RegisterCustomerRequest request) {

        Result<Void> result = customerService.registerCustomer(
                request.customerName(),
                request.phoneNumber()
        );

        if (result.isFail()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{customerId}/phone-number")
    public ResponseEntity<?> updatePhoneNumber(
            @PathVariable String customerId,
            @Valid @RequestBody UpdatePhoneRequest request) {

        Result<Void> result = customerService.updatePhoneNumber(
                customerId,
                request.newPhoneNumber()
        );

        if (result.isFail()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }

        return ResponseEntity.ok().build();
    }

    // --------------------- READ ---------------------

    @GetMapping("/{customerId}")
    public ResponseEntity<?> findById(@PathVariable String customerId) {

        return customerService.findCustomerById(customerId)
                .map(customer -> ResponseEntity.ok(
                        new CustomerResponse(
                            customer.getCustomerNameId().value,
                            customer.getPhoneNumber()
                                    .map(phone -> phone.value)
                                    .orElse(null),
                            customer.getRegistrationDate().toString()
                        )
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {

        List<CustomerResponse> customers =
                customerService.getAllCustomers()
                        .stream()
                        .map(customer -> new CustomerResponse(
                                customer.getCustomerNameId().value,
                                customer.getPhoneNumber()
                                        .map(phone -> phone.value)
                                        .orElse(null),
                                customer.getRegistrationDate().toString()
                        ))
                        .collect(Collectors.toList());

        return ResponseEntity.ok(customers);
    }

    @GetMapping("/search")
    public ResponseEntity<?> findByPhoneNumber(@RequestParam String phoneNumber) {

        return customerService.findCustomerByPhoneNumber(phoneNumber)
                .map(customer -> ResponseEntity.ok(
                        new CustomerResponse(
                                customer.getCustomerNameId().value,
                                customer.getPhoneNumber()
                                        .map(phone -> phone.value)
                                        .orElse(null),
                                customer.getRegistrationDate().toString()
                        )
                ))
                .orElse(ResponseEntity.notFound().build());
    }

    // --------------------- DTOs ---------------------

    public record RegisterCustomerRequest(
        @NotBlank(message = "El nombre es obligatorio")
        String customerName,

        String phoneNumber

    ) {}

    public record UpdatePhoneRequest(
        @NotBlank(message = "El número de teléfono es obligatorio")
        String newPhoneNumber
    ) {}

    public record CustomerResponse(
        String customerName,
        String phoneNumber,
        String registrationDate
    ) {
    }
}
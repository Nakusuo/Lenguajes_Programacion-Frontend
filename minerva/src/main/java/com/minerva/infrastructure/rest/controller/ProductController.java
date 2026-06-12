package com.minerva.infrastructure.rest.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.minerva.domain.constants.Category;
import com.minerva.domain.entities.shared.Result;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.minerva.application.service.ProductService;
import com.minerva.domain.constants.GainStrategy;
import com.minerva.domain.constants.SaleType;
import com.minerva.domain.entities.product.Product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // --------------------- WRITE ---------------------

    @PostMapping
    public ResponseEntity<?> registerProduct(@Valid @RequestBody RegisterProductRequest request) {

        Result<Void> result = productService.registerProduct(
                request.productName(),
                request.gainStrategy(),
                request.gainAmount(),
                request.reorderLevel(),
                request.barCode(),
                request.saleType(),
                request.category(),
                request.purchasedFromSupplierId(),
                request.purchaseUnitPrice(),
                request.purchaseQuantity(),
                request.purchaseExpirationDate()
        );

        if (result.isFail()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{productId}/stock-entries")
    public ResponseEntity<?> registerStockEntry(
            @PathVariable String productId,
            @Valid @RequestBody RegisterStockEntryRequest request) {

        Result<Void> result = productService.registerStockEntry(
                productId,
                request.supplierNameId(),
                request.unitPrice(),
                request.quantity(),
                request.expirationDate()
        );

        if (result.isFail()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/unit-to-bulk")
    public ResponseEntity<?> registerUnitToBulk(
            @Valid @RequestBody RegisterUnitToBulkRequest request) {

        Result<Void> result = productService.registerUnitToBulk(
                request.unitProductId(),
                request.bulkProductId(),
                request.quantity()
        );

        if (result.isFail()) {
            return ResponseEntity.badRequest().body(result.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // --------------------- READ ---------------------

    @GetMapping("/{productId}")
    public ResponseEntity<?> findById(@PathVariable String productId) {

        return productService.findProductById(productId)
                .map(product -> ResponseEntity.ok(mapToResponse(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/barcode/{barCode}")
    public ResponseEntity<?> findByBarCode(@PathVariable String barCode) {

        return productService.findProductByBarCode(barCode)
                .map(product -> ResponseEntity.ok(mapToResponse(product)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {

        List<ProductResponse> products = productService.findAllProducts()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }

    // --------------------- MAPPER ---------------------

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
                product.getNameId().value,
                product.getBarCode().map(b -> b.value).orElse(null),
                product.getSaleType().name(),
                product.getCategory().name()
        );
    }

    // --------------------- DTOs ---------------------

    public record RegisterProductRequest(
            @NotBlank String productName,
            @NotNull GainStrategy gainStrategy,
            @NotNull BigDecimal gainAmount,
            @NotNull BigDecimal reorderLevel,
            String barCode,
            @NotNull SaleType saleType,
            @NotNull Category category,
            @NotBlank String purchasedFromSupplierId,
            @NotNull BigDecimal purchaseUnitPrice,
            @NotNull BigDecimal purchaseQuantity,
            @NotNull LocalDateTime purchaseExpirationDate
    ) {}

    public record RegisterStockEntryRequest(
            @NotBlank String supplierNameId,
            @NotNull BigDecimal unitPrice,
            @NotNull BigDecimal quantity,
            @NotNull LocalDateTime expirationDate
    ) {}

    public record RegisterUnitToBulkRequest(
            @NotBlank String unitProductId,
            @NotBlank String bulkProductId,
            @NotNull BigDecimal quantity
    ) {}

    public record ProductResponse(
            String productId,
            String barCode,
            String saleType,
            String category
    ) {}
}

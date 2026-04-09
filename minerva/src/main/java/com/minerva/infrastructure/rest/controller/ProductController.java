package com.minerva.infrastructure.rest.controller;

import com.minerva.application.port.drivers.ProductUseCase;
import com.minerva.domain.entities.product.Product;
import com.minerva.domain.entities.shared.Result;
import com.minerva.infrastructure.rest.dto.product.MessageDto;
import com.minerva.infrastructure.rest.dto.product.ReadProductDto;
import com.minerva.infrastructure.rest.dto.product.RegisterProductDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {
    private final ProductUseCase productUseCase;

    public ProductController(ProductUseCase productUseCase) {
        this.productUseCase = productUseCase;
    }

    @PostMapping
    public ResponseEntity<MessageDto> register(@Valid @RequestBody RegisterProductDto dto) {
        Result<Void> result = productUseCase.registerProduct(
                dto.getProductName(),
                dto.getGainStrategy(),
                dto.getGainAmount(),
                dto.getReorderLevel(),
                dto.getBarCode(),
                dto.getSaleType(),
                dto.getCategory()
        );

        if (result.isFail()) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new MessageDto(result.getMessage()));
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new MessageDto("Producto registrado correctamente"));
    }

    // -------------------- GET BY ID --------------------

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") String productId) {

        Optional<Product> optionalProduct = productUseCase.findProductById(productId);

        if (optionalProduct.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(new MessageDto("Producto no encontrado"));
        }

        return ResponseEntity.ok(to(optionalProduct.get()));
    }


    // -------------------- GET ALL --------------------

    @GetMapping
    public ResponseEntity<List<ReadProductDto>> getAllProducts() {

        List<Product> products = productUseCase.findAllProducts();

        List<ReadProductDto> dtos = products.stream()
                .map(ProductController::to)
                .toList();

        return ResponseEntity.ok(dtos);
    }


    // -------------------- MAPPER --------------------

    private static ReadProductDto to(Product product) {
        return new ReadProductDto(
                product.getNameId(),                                   // productName
                product.getGainStrategy().name(),                      // gainStrategy
                product.getGainAmount(),                               // gainAmount
                product.getReorderLevel().orElse(null),                // reorderLevel
                product.getBarCode().orElse(""),                     // barCode
                product.getSaleType().name(),                          // saleType
                product.getCategory().name(),                          // category
                product.getPrice().orElse(null),                            // price
                product.getRegistrationDate().toString(),              // registrationDate
                product.getStock()                                     // stock
        );
    }




}

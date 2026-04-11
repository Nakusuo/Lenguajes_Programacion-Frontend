package com.minerva.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stockEntry")
public class StockEntryEntity {

    @Id
    @Column(name = "stockEntryId")
    private UUID stockEntryId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "productNameId", nullable = false)
    private ProductEntity productEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "supplierNameId", nullable = false)
    private SupplierEntity supplierEntity;

    @Column(name = "unitPrice", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;

    @Column(name = "quantity", precision = 10, scale = 3, nullable = false)
    private BigDecimal quantity;

    @Column(name = "ExpirationDate", nullable = false)
    private LocalDateTime expirationDate;

    @Column(
            name = "registrationDate",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime registrationDate;
}

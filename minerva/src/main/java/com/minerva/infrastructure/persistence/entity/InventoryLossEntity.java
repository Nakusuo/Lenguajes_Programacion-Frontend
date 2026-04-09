package com.minerva.infrastructure.persistence.entity;

import com.minerva.domain.constants.ReasonProductLoss;
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
@Table(name = "inventoryLoss")
public class InventoryLossEntity {

    @Id
    @Column(name = "inventoryLossId")
    private UUID inventoryLossId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "productNameId", nullable = false)
    private ProductEntity productEntity;

    @Column(name = "quantity", precision = 10, scale = 3, nullable = false)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private ReasonProductLoss reason;

    @Column(name = "observation", length = 255)
    private String observation;

    @Column(
            name = "registrationDate",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime registrationDate;
}

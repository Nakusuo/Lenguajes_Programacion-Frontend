package com.minerva.infrastructure.persistence.entity;

import com.minerva.domain.constants.PaymentMethod;
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
@Table(name = "pay")
public class PayEntity {
    // columnDefinition = "CHAR(36)"
    @Id
    @Column(name = "payId")
    private UUID payId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "saleId", nullable = false)
    private SaleEntity saleEntity;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "paymentMethod", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(
            name = "registrationDate",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime registrationDate;
}


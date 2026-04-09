package com.minerva.infrastructure.persistence.entity;

import com.minerva.domain.constants.ReasonProductReturn;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "productReturn")
public class ProductReturnEntity {

    @Id
    @Column(name = "productReturnId", columnDefinition = "CHAR(36)")
    private String productReturnId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "saleDetailId", nullable = false)
    private SaleDetailEntity saleDetailEntity;

    @Column(name = "quantity", precision = 10, scale = 3, nullable = false)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false)
    private ReasonProductReturn reason;

    @Column(
            name = "registrationDate",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime registrationDate;
}

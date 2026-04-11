package com.minerva.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "saleDetail")
public class SaleDetailEntity {

    @Id
    @Column(name = "saleDetailId")
    private UUID saleDetailId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "saleId", nullable = false)
    private SaleEntity saleEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "productNameId", nullable = false)
    private ProductEntity productEntity;

    @Column(name = "quantity", precision = 10, scale = 3, nullable = false)
    private BigDecimal quantity;

    @Column(name = "unitPrice", precision = 10, scale = 2, nullable = false)
    private BigDecimal unitPrice;
}

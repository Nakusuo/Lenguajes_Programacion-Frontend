package com.minerva.infrastructure.persistence.entity;

import com.minerva.domain.constants.Category;
import com.minerva.domain.constants.GainStrategy;
import com.minerva.domain.constants.SaleType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity {

    @Id
    @Column(name = "productNameId", length = 100)
    private String productNameId;

    @Enumerated(EnumType.STRING)
    @Column(name = "gainStrategy", nullable = false)
    private GainStrategy gainStrategy;

    @Column(name = "gainAmount", precision = 10, scale = 2, nullable = false)
    private BigDecimal gainAmount;

    @Column(name = "stock", precision = 10, scale = 3, nullable = false)
    private BigDecimal stock;

    @Column(name = "reorderLevel", precision = 10, scale = 3)
    private BigDecimal reorderLevel;

    @Column(name = "barCode", columnDefinition = "CHAR(13)", unique = true)
    private String barCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "SaleType", nullable = false)
    private SaleType saleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(
            name = "registrationDate",
            nullable = false,
            updatable = false,
            columnDefinition = "TIMESTAMP"
    )
    private LocalDateTime registrationDate;
}


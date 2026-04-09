package com.minerva.infrastructure.rest.dto.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ReadProductDto {
    private String productName;
    private String gainStrategy;
    private BigDecimal gainAmount;
    private BigDecimal reorderLevel;
    private String barCode;
    private String saleType;
    private String category;
    private BigDecimal price;
    private String registrationDate;
    private BigDecimal stock;
}

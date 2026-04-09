package com.minerva.infrastructure.rest.dto.product;

import com.minerva.domain.constants.Category;
import com.minerva.domain.constants.GainStrategy;
import com.minerva.domain.constants.SaleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class RegisterProductDto {
    @NotNull
    private String productName;
    @NotNull
    private GainStrategy gainStrategy;
    @NotNull
    private BigDecimal gainAmount;

    private BigDecimal reorderLevel;
    private String barCode;

    @NotNull
    private SaleType saleType;
    @NotNull
    private Category category;
}

package com.minerva.domain.services;

import java.math.BigDecimal;

public class Math {

    public static boolean isInteger(BigDecimal number) {
        return number != null && number.stripTrailingZeros().scale() <= 0;
    }

    public static boolean isDecimal(BigDecimal number) {
        return number != null && number.stripTrailingZeros().scale() > 0;
    }

    public static boolean isPositive(BigDecimal number) {
        return number != null && number.compareTo(BigDecimal.ZERO) > 0;
    }

    public static boolean isNegative(BigDecimal value) {
        return value != null && value.compareTo(BigDecimal.ZERO) < 0;
    }

}

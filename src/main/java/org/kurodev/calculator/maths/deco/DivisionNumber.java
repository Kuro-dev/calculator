package org.kurodev.calculator.maths.deco;

import org.kurodev.calculator.maths.Calculation;

import java.math.BigDecimal;

public class DivisionNumber extends NumberDecorator {
    public DivisionNumber(BigDecimal value, Calculation parent) {
        super(value, parent);
    }

    @Override
    public BigDecimal getResult() {
        return parent.getResult().divide(super.getResult(),roundingMode);
    }
}

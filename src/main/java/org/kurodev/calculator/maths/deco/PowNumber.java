package org.kurodev.calculator.maths.deco;

import org.kurodev.calculator.maths.Calculation;

import java.math.BigDecimal;

public class PowNumber extends NumberDecorator {
    public PowNumber(BigDecimal value, Calculation parent) {
        super(value, parent);
    }

    @Override
    public BigDecimal getResult() {
        return parent.getResult().pow(value.intValue());
    }
}

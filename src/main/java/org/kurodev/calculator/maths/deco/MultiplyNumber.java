package org.kurodev.calculator.maths.deco;

import org.kurodev.calculator.maths.Calculation;

import java.math.BigDecimal;

public class MultiplyNumber extends NumberDecorator {
    public MultiplyNumber(BigDecimal value, Calculation parent) {
        super(value, parent);
    }

    @Override
    public BigDecimal getResult() {
        return parent.getResult().multiply(super.getResult());
    }
}

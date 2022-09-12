package org.kurodev.calculator.maths.deco;

import org.kurodev.calculator.maths.Calculation;

import java.math.BigDecimal;

public strictfp class MinusNumber extends NumberDecorator {
    public MinusNumber(BigDecimal value, Calculation parent) {
        super(value, parent);
    }

    @Override
    public BigDecimal getResult() {
        var parentResult = parent.getResult();
        return parentResult.subtract(super.getResult());
    }
}

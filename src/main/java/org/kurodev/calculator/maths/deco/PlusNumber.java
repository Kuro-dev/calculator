package org.kurodev.calculator.maths.deco;

import org.kurodev.calculator.maths.Calculation;

import java.math.BigDecimal;

public strictfp class PlusNumber extends NumberDecorator {

    public PlusNumber(BigDecimal number, Calculation parent) {
        super(number, parent);
    }

    @Override
    public BigDecimal getResult() {
        return parent.getResult().add(super.getResult());
    }
}

package org.kurodev.calculator.maths.deco;

import org.kurodev.calculator.maths.Calculation;

import java.math.BigDecimal;

public class NumberDecorator extends Calculation {
    protected final Calculation parent;

    public NumberDecorator(BigDecimal value, Calculation parent) {
        super(value, parent.getRoundingMode());
        this.parent = parent;
    }
}

package org.kurodev.calculator.maths;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Indicates that an operation using the Number class has resulted in an error of some sort.
 * <p>This class always overrides the {@link #toString()} method to represent the exact error that happened</p>
 * {@link Calculation#isNumber()} always will return false
 */
public final class NumberErrorInstance extends Calculation {
    private final String errorMsg;

    NumberErrorInstance(String errorMsg) {
        super(null, null);
        this.errorMsg = errorMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NumberErrorInstance that = (NumberErrorInstance) o;
        return Objects.equals(errorMsg, that.errorMsg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), errorMsg);
    }

    @Override
    public Calculation minus(BigDecimal other) {
        throw new UnsupportedOperationException(toString());
    }

    @Override
    public Calculation multiplyWith(BigDecimal b) {
        throw new UnsupportedOperationException(toString());
    }

    @Override
    public Calculation divideBy(BigDecimal b) {
        throw new UnsupportedOperationException(toString());
    }

    @Override
    public BigDecimal getResult() {
        throw new UnsupportedOperationException(toString());
    }

    @Override
    public String toString() {
        return errorMsg;
    }
}

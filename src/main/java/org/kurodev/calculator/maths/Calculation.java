package org.kurodev.calculator.maths;

import org.kurodev.calculator.maths.deco.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public strictfp class Calculation {
    protected final BigDecimal value;
    protected final RoundingMode roundingMode;

    public Calculation() {
        this(0.0d);
    }

    public Calculation(double value) {
        this(BigDecimal.valueOf(value), RoundingMode.HALF_UP);
    }

    public Calculation(BigDecimal value, RoundingMode roundingMode) {
        this.value = value;
        this.roundingMode = roundingMode;
    }

    /**
     * The given instance will have {@link Double#NaN} as its value.
     * <p>this means that {@link #isNumber()} will return {@code false}</p>
     * <p>Calculations like {@link #plus(BigDecimal) plus} and {@link #minus(BigDecimal) minus}
     * will still work as this value is treated like {@code 0}</p>
     *
     * @return a number instance with a null-like value.
     */
    public static Calculation nullInstance() {
        return new Calculation(null, RoundingMode.HALF_UP);
    }

    public static Calculation errorInstance(final String errorMsg) {
        return new NumberErrorInstance(errorMsg);
    }

    public RoundingMode getRoundingMode() {
        return roundingMode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calculation number = (Calculation) o;
        return Objects.equals(getResult(), number.getResult());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    public Calculation plus(BigDecimal other) {
        return new PlusNumber(other, this);
    }

    public Calculation plus(double other) {
        return new PlusNumber(BigDecimal.valueOf(other), this);
    }

    public final Calculation plus(Calculation other) {
        return plus(other.getResult());
    }

    public Calculation minus(BigDecimal other) {
        return new MinusNumber(other, this);
    }

    public Calculation minus(double other) {
        return new MinusNumber(BigDecimal.valueOf(other), this);
    }

    public final Calculation minus(Calculation other) {
        return minus(other.getResult());
    }

    public BigDecimal getResult() {
        if (isNumber())
            return value;
        return BigDecimal.valueOf(0L);
    }

    public final boolean isNumber() {
        return value != null && !(this instanceof NumberErrorInstance);
    }

    public final boolean isError() {
        return this instanceof NumberErrorInstance;
    }

    @Override
    public String toString() {
        return getResult().toPlainString();
    }

    public Calculation multiplyWith(Calculation b) {
        return multiplyWith(b.getResult());
    }

    public Calculation multiplyWith(BigDecimal b) {
        return new MultiplyNumber(b, this);
    }

    public Calculation multiplyWith(double b) {
        return new MultiplyNumber(BigDecimal.valueOf(b), this);
    }

    public Calculation divideBy(Calculation b) {
        return divideBy(b.getResult());
    }

    public Calculation divideBy(BigDecimal b) {
        return new DivisionNumber(b, this);
    }

    public Calculation divideBy(double b) {
        return new DivisionNumber(BigDecimal.valueOf(b), this);
    }

    public Calculation pow(Calculation b) {
        return pow(b.getResult());
    }

    public Calculation pow(int b) {
        return new PowNumber(BigDecimal.valueOf(b), this);
    }

    public Calculation pow(BigDecimal b) {
        return pow(b.intValue());
    }

    public Calculation sqrt() {
        return pow(2);
    }

}

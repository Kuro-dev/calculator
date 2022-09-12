package org.kurodev.calculator.maths;

/**
 * Conclusive list of all operations that can be calculated by the calculator in order of precedence.
 */
@SuppressWarnings("unused") //they are all used.
public enum DefaultOperations implements Operation {
    ADD('+') {
        @Override
        public Calculation conclude(Calculation a, Calculation b) {
            return a.plus(b);
        }
    },
    SUBTRACT('-') {
        @Override
        public Calculation conclude(Calculation a, Calculation b) {
            return a.minus(b);
        }
    },
    MULTIPLY('*') {
        @Override
        public Calculation conclude(Calculation a, Calculation b) {
            return a.multiplyWith(b);
        }
    },
    DIVIDE('/') {
        @Override
        public Calculation conclude(Calculation a, Calculation b) {
            return a.divideBy(b);
        }
    },
    SQUARE('^') {
        @Override
        public Calculation conclude(Calculation a, Calculation b) {
            return a.pow(b);
        }
    };

    private final char chara;
    private final int precedence;

    DefaultOperations(char sign) {
        this.chara = sign;
        this.precedence = ordinal();
    }

    public abstract Calculation conclude(Calculation a, Calculation b);

    public char getOperator() {
        return chara;
    }

    public int getPrecedence() {
        return precedence;
    }
}

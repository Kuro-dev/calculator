package org.kurodev.calculator.maths;

import java.util.function.Supplier;

/**
 * Conclusive list of all operations that can be calculated by the calculator in order of precedence.
 */
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

    private static final String REGEX;

    static {
        StringBuilder out = new StringBuilder("[");
        for (DefaultOperations value : values()) {
            out.append("\\").append(value.chara);
        }
        out.append("]*");
        REGEX = out.toString();
    }

    private final char chara;
    private final int precedence;

    DefaultOperations(char sign) {
        this.chara = sign;
        this.precedence = ordinal();
    }

    public static DefaultOperations of(String search) {
        return of(search, () -> {
            throw new IllegalArgumentException(search);
        });
    }

    public static DefaultOperations of(String search, DefaultOperations orElse) {
        return of(search, () -> orElse);
    }

    public static DefaultOperations of(String search, Supplier<DefaultOperations> orElse) {
        for (DefaultOperations value : DefaultOperations.values()) {
            if (value.name().equalsIgnoreCase(search)) {
                return value;
            }
            if (search.equals(String.valueOf(value.chara))) {
                return value;
            }
        }
        return orElse.get();
    }

    public static DefaultOperations of(char chara) {
        return of(String.valueOf(chara));
    }

    public static String getRegex() {
        return REGEX;
    }

    /**
     * @return The sign which is being parsed form the string. "ADD" for example would return "+"
     */
    public char getIdentifier() {
        return chara;
    }

    public DefaultOperations interact(DefaultOperations other) {
        if (other == this) {
            return ADD;
        }
        return SUBTRACT;
    }

    public abstract Calculation conclude(Calculation a, Calculation b);

    public char getOperator() {
        return chara;
    }

    public int getPrecedence() {
        return precedence;
    }
}

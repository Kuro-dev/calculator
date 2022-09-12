package org.kurodev.calculator.maths;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Calculates any given string or mathematical expression that consists of any of the supported operations and parenthesis.
 *
 * @see DefaultOperations
 */
public class FormulaParser {
    /**
     * Identifier variable to symbolize invalid formulas.
     * <p>Example cases of invalid expressions (not conclusive):</p>
     * <p>a+</p>
     * <p>a + b -</p>
     * <p>a + (b</p>
     * <p>a + b)</p>
     *
     * @see NumberErrorInstance
     */
    public static final Calculation INVALID_EXPRESSION = Calculation.errorInstance("Invalid Expression");
    /**
     * Identifier variable to symbolize an unknown variable.
     * <p>a = 6</p>
     * <p>b = c</p>
     * <p>because 'c' is unknown this instance will be returned</p>
     *
     * @apiNote {@link Calculation#isNumber()} will always return false
     */
    public static final Calculation UNKNOWN_VARIABLE = Calculation.errorInstance("Unknown Variable");
    public static final Calculation INVALID_ASSIGNMENT = Calculation.errorInstance("Invalid assigment");
    private static final Pattern CONTAINS_VARIABLE = Pattern.compile("[a-zA-Z]+");
    private static final Pattern MULTIPLE_SIGNS = Pattern.compile("[+-]{2,}");

    private static final Set<Operation> OPERATIONS = new HashSet<>(EnumSet.allOf(DefaultOperations.class));
    private final Map<String, BigDecimal> variables = new HashMap<>();


    /**
     * Adds the given operation to the parser and enables calculation with it using strings.
     * For an operation to be added it must conform to the following rules:
     *
     * <p>- It must not be an Alphabetic Character</p>
     * <p>- It must not be a Digit</p>
     * <p>- It must be unique (not yet used by another item in the set)</p>
     *
     * <p>if any of these conditions are not met, the item will not be added.</p>
     *
     * @param o The operation to support.
     * @return {@code true} if the operation has been added, {@code false} otherwise
     * @implNote Recomputes the parser regex everytime something is added.
     * To add multiple operations use {@link #addOperation(Collection)}
     * @see #addOperation(Collection)
     */
    public static boolean addOperation(Operation o) {
        if (!OPERATIONS.stream().map(Operation::getOperator).toList().contains(o.getOperator()) &&
                !Character.isAlphabetic(o.getOperator()) && !Character.isDigit(o.getOperator()))
            return OPERATIONS.add(o);
        return false;
    }

    /**
     * adds multiple operations to the parser.
     * recomputes regex once they have all be added.
     *
     * @see #addOperation(Operation)
     */
    public static void addOperation(Collection<Operation> o) {
        o.forEach(FormulaParser::addOperation);
    }

    public static void removeOperation(Operation o) {
        OPERATIONS.removeIf(operation -> operation.getOperator() == o.getOperator());
    }

    public static void restoreDefaultOperations() {
        OPERATIONS.clear();
        OPERATIONS.addAll(EnumSet.allOf(DefaultOperations.class));
    }

    /**
     * @param formula formula like 5+3-7+9
     * @return The results can be as follows:
     * <p>{@link #UNKNOWN_VARIABLE} if the variable given is unknown</p>
     * <p>{@link #INVALID_EXPRESSION} if the formula is incomplete or contains unsupported expressions</p>
     * <p>the normal result of the calculation</p>
     * <p>{@code null} if a variable has just been assigned by passing {@code x = integer-value} as the string</p>
     */
    public Calculation calculate(String formula) {
        var normalized = formula.replaceAll("\\s", "");
        if (!formula.contains("=")) {
            normalized = resolveVars(normalized);
            if (normalized == null) return UNKNOWN_VARIABLE;
            normalized = normalize(normalized);
            if (PostfixCalculator.IS_NUMBER.matcher(normalized).matches())
                return new Calculation(Double.parseDouble(normalized));
            PostfixConverter converter = new PostfixConverter(OPERATIONS);
            var prefix = converter.toPostfix(normalized);
            PostfixCalculator calculator = new PostfixCalculator(OPERATIONS);
            if (prefix == null) return INVALID_EXPRESSION;
            return calculator.evaluate(prefix);
        } else {
            return assignVar(normalized);
        }
    }

    private String normalize(String formula) {
        Pattern[] patterns = {
                Pattern.compile("(\\d\\()"), // 3( -> 3*(
                Pattern.compile("(\\)\\d)"), // )3 -> )*3
                Pattern.compile("(\\)\\()"), // )( -> )*(
        };
        for (Pattern pattern : patterns) {
            var matcher = pattern.matcher(formula);
            StringBuilder formulaCorrected = new StringBuilder();
            replaceStringSmart(matcher, formulaCorrected, formula);
            formula = formulaCorrected.toString();
        }
        return normalizeCorrectedFormula(formula);
    }

    private void replaceStringSmart(Matcher m, StringBuilder b, String formula) {
        int lastEnd = 0;
        if (m.find()) {
            do {
                String grp = m.group();
                b.append(formula, lastEnd, m.start())
                        .append(grp.charAt(0)).append("*").append(grp.charAt(1));
                lastEnd = m.end();
            } while (m.find());
        }
        b.append(formula.substring(lastEnd));
    }

    private String normalizeCorrectedFormula(String formula) {
        var matcher = MULTIPLE_SIGNS.matcher(formula);
        final var builder = new StringBuilder();
        int lastEnd = 0;
        while (matcher.find()) {
            var grp = matcher.group();
            int minus = 0;
            for (int i = 0; i < grp.length(); i++) {
                var chara = grp.charAt(i);
                if (chara == '-') minus++;
            }
            builder.append(formula, lastEnd, matcher.start());
            if ((minus & 1) == 0) {
                builder.append("+");
            } else {
                builder.append("-");
            }
            lastEnd = matcher.end();
        }
        builder.append(formula.substring(lastEnd));
        return builder.toString();
    }

    private Calculation assignVar(String normalized) {
        if (normalized == null) return UNKNOWN_VARIABLE;
        int first = normalized.indexOf("=");
        if (first >= 0) {
            if (first == normalized.lastIndexOf("=")) {
                var assignment = normalized.split("=");
                if (assignment.length < 2) {
                    return INVALID_ASSIGNMENT;
                }
                var name = assignment[0];
                var norm = resolveVars(assignment[1]);
                if (norm == null) return UNKNOWN_VARIABLE;
                var value = calculate(norm);
                if (value instanceof NumberErrorInstance) {
                    return value; //some syntax error occurred
                } else {
                    //name must only contain characters, not numbers or other symbols
                    if (CONTAINS_VARIABLE.matcher(name).matches()) {
                        variables.put(name, value.getResult());
                        return null;
                    }
                }
            }
            return INVALID_ASSIGNMENT;
        }
        return INVALID_EXPRESSION;
    }

    /**
     * example: {@code a = 16}; {@code b = 15}
     * <p>
     * Input: 5 + a - b +6
     * <p>
     * resolved output: 5 + 16 - 15 + 6
     *
     * @param str The normalized input string
     * @return a resolve equation
     */
    private String resolveVars(String str) {
        final var matcher = CONTAINS_VARIABLE.matcher(str);
        final var builder = new StringBuilder();
        int lastEnd = 0;
        while (matcher.find()) {
            var grp = matcher.group();
            var val = variables.get(grp);
            if (val == null) {
                return null;
            }
            builder.append(str, lastEnd, matcher.start());
            builder.append(val.toPlainString());
            lastEnd = matcher.end();
        }
        if (!builder.toString().isBlank()) {
            builder.append(str.substring(lastEnd));
            str = builder.toString();
        }
        return str;
    }

    @Override
    public String toString() {
        return "FormulaParser{" +
                "variables=" + variables +
                '}';
    }

    public Map<String, BigDecimal> getVariables() {
        return variables;
    }

}

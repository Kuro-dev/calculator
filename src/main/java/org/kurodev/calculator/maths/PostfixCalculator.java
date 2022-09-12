package org.kurodev.calculator.maths;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;
import java.util.regex.Pattern;

public class PostfixCalculator {
    static final Pattern IS_NUMBER = Pattern.compile("-?((\\d+\\.\\d+)|\\d+)");
    private final Set<Operation> operations;

    PostfixCalculator(Set<Operation> operations) {
        this.operations = operations;
    }

    private static boolean isOperand(String part) {
        return IS_NUMBER.matcher(part).matches();
    }

    public Calculation evaluate(String expr) {
        final Deque<Calculation> stack = new ArrayDeque<>();
        var parts = expr.split(PostfixConverter.DELIM);
        for (String part : parts) {
            if (isOperand(part)) {
                stack.push(new Calculation(new BigDecimal(part), RoundingMode.HALF_UP));
            } else {
                Operation op = getOperation(part);
                if (op == null) {
                    return FormulaParser.INVALID_EXPRESSION;
                }
                var o2 = stack.pop();
                var o1 = stack.pop();
                var res = op.conclude(o1, o2);
                stack.push(res);
            }
        }
        return stack.pop();
    }

    private Operation getOperation(String part) {
        return operations.stream()
                .filter(operation -> part.equals(String.valueOf(operation.getOperator())))
                .findFirst().orElse(null);
    }
}

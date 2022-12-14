package org.kurodev.calculator.maths;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Set;

public class PostfixConverter {
    //some very unique character
    public static final String DELIM = " "; //ǀ
    private final Set<Operation> operations;

    PostfixConverter(Set<Operation> operations) {
        this.operations = operations;
    }

    private static void flushParenthesis(Deque<Character> stack, StringBuilder postfixExp) {
        while (!stack.isEmpty() && stack.peek() != '(') {
            postfixExp.append(DELIM).append(stack.pop());
        }
        if (!stack.isEmpty())
            stack.removeFirst(); //remove opening parenthesis
    }

    private Boolean isParenthesis(char c, Deque<Character> stack, StringBuilder postfixExp) {
        if (c == '(') {
            stack.push(c);
            return true;
        }
        if (c == ')') {
            if (stack.contains('(')) {
                flushParenthesis(stack, postfixExp);
                return true;
            }
            return null;
        }
        return false;
    }

    public String toPostfix(String infix) {
        Deque<Character> stack = new ArrayDeque<>();
        StringBuilder result = new StringBuilder();
        char[] charArray = infix.toCharArray();
        for (int i = 0, charArrayLength = charArray.length; i < charArrayLength; i++) {
            char c = charArray[i];
            var parenthesis = isParenthesis(c, stack, result);
            if (parenthesis == null) {
                return null;
            }
            if (parenthesis) continue;
            Operation operator = getOp(c);
            if (operator == null) {
                result.append(c);
            } else {
                if (i != 0 && i + 1 < charArrayLength) {
                    char before = charArray[i - 1];
                    char after = charArray[i + 1];
                    if ((before == '*' || before == '+' || before == '/') && Character.isDigit(after)) {
                        result.append(c);
                        continue;
                    }
                }
                if (stack.isEmpty()) {
                    if (result.isEmpty()) {
                        result.append(c);
                    } else {
                        result.append(DELIM);
                        stack.push(c);
                    }

                } else {
                    addOperator(c, stack, result);
                }
            }

        }
        while (!stack.isEmpty()) {
            result.append(DELIM).append(stack.pop());
        }
//        System.err.println("infix: " + infix + " || postfix: " + result);
        return result.toString();
    }

    @SuppressWarnings("ConstantConditions") //never happens
    private void addOperator(char c, Deque<Character> stack, StringBuilder postfixExp) {
        var a = precedence(stack.peek());
        var b = precedence(c);
        if (b <= a) {
            while (b <= a) {
                postfixExp.append(DELIM).append(stack.pop());
                if (!stack.isEmpty())
                    a = precedence(stack.peek());
                else
                    break;
            }
        }
        postfixExp.append(DELIM);
        stack.push(c);
    }

    private int precedence(char c) {
        var out = getOp(c);
        return out == null ? -1 : out.getPrecedence();
    }

    private Operation getOp(char c) {
        for (var op : operations) {
            if (op.getOperator() == c) {
                return op;
            }
        }
        return null;
    }
}

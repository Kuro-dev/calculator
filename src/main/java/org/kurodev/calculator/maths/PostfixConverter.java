package org.kurodev.calculator.maths;

import java.util.ArrayDeque;
import java.util.Deque;

public class PostfixConverter {
    //some very unique character
    public static final String DELIM = " "; //ǀ

    private PostfixConverter() {
    }

    public static String toPostfix(String infix) {
        Deque<Character> stack = new ArrayDeque<>();
        StringBuilder result = new StringBuilder();
        for (char c : infix.toCharArray()) {
            var parenthesis = isParenthesis(c, stack, result);
            if (parenthesis == null) {
                return null;
            }
            if (parenthesis) continue;
            var operator = getOp(c);
            if (operator == null) {
                result.append(c);
            } else {
                if (stack.isEmpty()) {
                    result.append(DELIM);
                    stack.push(c);
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

    private static Boolean isParenthesis(char c, Deque<Character> stack, StringBuilder postfixExp) {
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

    @SuppressWarnings("ConstantConditions") //never happens
    private static void addOperator(char c, Deque<Character> stack, StringBuilder postfixExp) {
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

    private static void flushParenthesis(Deque<Character> stack, StringBuilder postfixExp) {
        while (!stack.isEmpty() && stack.peek() != '(') {
            postfixExp.append(DELIM).append(stack.pop());
        }
        if (!stack.isEmpty())
            stack.removeFirst(); //remove opening parenthesis
    }

    private static int precedence(char c) {
        var out = getOp(c);
        return out == null ? -1 : out.getPrecedence();
    }

    private static Operation getOp(char c) {
        for (var op : Operation.values()) {
            if (op.getOperator() == c) {
                return op;
            }
        }
        return null;
    }
}

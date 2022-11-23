package parsing;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.kurodev.calculator.maths.Calculation;
import org.kurodev.calculator.maths.FormulaParser;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class ParserTest {
    private FormulaParser parser;

    @Before
    public void prepare() {
        parser = new FormulaParser();
    }

    @Test
    public void testAddition() {
        String formula = "2 + 2";
        String expected = "4";
        Calculation result = parser.calculate(formula);
        assertTrue(result.isNumber());
        assertEquals(expected, result.toString());
    }

    @Test
    public void testMultiplicationRange() {
        for (int i = -5; i <= 5; i++) {
            parser.getVariables().put("x", BigDecimal.valueOf(i));
            assertEquals(i * 2, parser.calculate("2x").getResult().intValue());
        }
    }

    @Test
    public void testMultiSameVar() {
        String formula = "x^2-(10+x)";
        for (int i = -5; i <= 5; i++) {
            parser.getVariables().put("x", BigDecimal.valueOf(i));
            assertEquals((int) Math.pow(i, 2) - (10 + i),
                    parser.calculate(formula).getResult().intValue());
        }
    }

    @Test
    public void testPowRange() {
        for (int i = -5; i <= 5; i++) {
            parser.getVariables().put("x", BigDecimal.valueOf(i));
            assertEquals((int) Math.pow(i, 2), parser.calculate("x^2").getResult().intValue());
        }
    }

    @Test
    public void testImplicitMultiplication() {
        String formula = "2x";
        String expected = "20";
        parser.getVariables().put("x", BigDecimal.valueOf(10));
        Calculation result = parser.calculate(formula);
        assertTrue(result.isNumber());
        assertEquals(expected, result.toString());
    }

    @Test
    public void testSubtraction() {
        String formula = "6 - 2";
        String expected = "4";
        Calculation result = parser.calculate(formula);
        assertTrue(result.isNumber());
        assertEquals(expected, result.toString());
    }

    @Test
    public void testMultiplication() {
        String formula = "3 * 2";
        String expected = "6";
        Calculation result = parser.calculate(formula);
        assertTrue(result.isNumber());
        assertEquals(expected, result.toString());
    }

    @Test
    public void testMultipleSigns() {
        String formula = "3  +-+-++--+ 2";
        String expected = "5";
        Calculation result = parser.calculate(formula);
        assertEquals(expected, result.toString());
    }


    @Test
    public void testDivision() {
        String formula = "6 / 2";
        String expected = "3";
        Calculation result = parser.calculate(formula);
        assertTrue(result.isNumber());
        assertEquals(expected, result.toString());
    }

    @Test
    public void testParenthesis() {
        String formulaWithoutParenthesis = "12 / 2 + 4";
        String expected = "10";
        Calculation result = parser.calculate(formulaWithoutParenthesis);
        assertTrue(result.isNumber());
        assertEquals(expected, result.toString());

        String formulaWithParenthesis = "12 / (2 + 4)";
        String expected2 = "2";
        Calculation result2 = parser.calculate(formulaWithParenthesis);
        assertTrue(result2.isNumber());
        assertEquals(expected2, result2.toString());
    }

    @Test
    public void testAssignVariable() {
        String assign1 = "x = 5";
        Calculation result = parser.calculate(assign1);
        assertNull(result);
        assertEquals("5.0", parser.calculate("x").toString());
        assertFalse(parser.calculate("x").isError());
        assertTrue(parser.calculate("x").isNumber());


        String assign2 = "y = ";
        Calculation invalid = parser.calculate(assign2);
        assertEquals(FormulaParser.INVALID_ASSIGNMENT, invalid);
        assertTrue(parser.calculate("y").isError());
        assertFalse(parser.calculate("y").isNumber());
        assertEquals(FormulaParser.UNKNOWN_VARIABLE, parser.calculate("y"));
    }

    @Test
    public void testAssignVariableComplex() {
        String assignX = "x = 5 + 3 * 8 - 2";
        String expectedX = "27.0";
        Calculation result = parser.calculate(assignX);
        assertNull(result);
        assertEquals(expectedX, parser.calculate("x").toString());
    }

    @Test
    public void testAssignExpressionInvalid() {
        String assignX = "6 + 2 +";
        Calculation result = parser.calculate(assignX);
        assertSame(FormulaParser.INVALID_EXPRESSION, result);
    }

    @Test
    public void testExpressionRandomSpaces() {
        String assignX = "5   + 3       *     8-  2";
        String expectedX = "27";
        Calculation result = parser.calculate(assignX);
        assertNotNull(result);
        assertEquals(expectedX, result.toString());
    }

    @Test
    public void testExpressionSquared() {
        String assignX = "5 ^ 2";
        String expectedX = "25";
        Calculation result = parser.calculate(assignX);
        assertNotNull(result);
        assertEquals(expectedX, result.toString());
    }

    @Test
    public void testNoSignBeforeParenthesisDefaultsToMultiplication() {
        String op = "4 (3+2)";
        String expected = "20";
        var result = parser.calculate(op);
        assertTrue(result.isNumber());
        assertEquals(expected, result.toString());
        op = "(3+2)4";
        var result2 = parser.calculate(op);
        assertTrue(result2.isNumber());
        assertEquals(expected, result2.toString());
    }

    @Test
    public void testMultiNoSignBeforeParenthesisDefaultsToMultiplication() {
        String op = "1 (2+2) 1 (2+2) (2+2)";
        String expected = "64";
        var result = parser.calculate(op);
        assertTrue(result.isNumber());
        assertEquals(expected, result.toString());
    }

    @Test
    public void testMultipleExpressionsInOnceInstance() {
        String[] operations = {
                "x = 4 +(6  (3*2)) /3^2", // 10
                "y = x + 7",
                "z = x + y",
                "veryLongVarName = 251.52"
        };
        for (String operation : operations) {
            Calculation result = parser.calculate(operation);
            assertNull(operation, result);
        }
        assertEquals(8, parser.calculate("x").getResult().intValue());
        assertEquals(15, parser.calculate("y").getResult().intValue());
        assertEquals(23, parser.calculate("z").getResult().intValue());
        assertEquals(251.52d, parser.calculate("veryLongVarName").getResult().doubleValue(), 0.0001);
    }

    @Test
    public void testMultipleSignsMultiplication() {
        String formula = "3 * +-+-++--+ 2";
        Calculation result = parser.calculate(formula);
        assertTrue(result.isError());
    }

    @Test
    @Ignore
    //TODO figure out how to handle this.
    public void testMultipleSignsMultiplicationNegative() {
        String formula = "3 * - 2";
        String expected = "-6";
        Calculation result = parser.calculate(formula);
        assertEquals(expected, result.toString());
    }

    @Test
    @Ignore
    //TODO figure out how to handle this.
    public void testMultipleSignsMultiplicationNegativeParenthesis() {
        String formula = "3 * (- 2)";
        String expected = "-6";
        Calculation result = parser.calculate(formula);
        assertEquals(expected, result.toString());
    }
}

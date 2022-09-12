package parsing;

import org.junit.Test;
import org.kurodev.calculator.maths.Calculation;

import static org.junit.Assert.assertEquals;

public class CalculatorTest {
    @Test
    public void testAddition() {
        Calculation calc = new Calculation();
        final var result = calc.plus(5).plus(3);
        assertEquals(8, result.getResult().intValue());
    }

    @Test
    public void testSubtraction() {
        Calculation calc = new Calculation();
        final var result = calc.plus(5).minus(3);
        assertEquals(2, result.getResult().intValue());
    }

    @Test
    public void testMultiplication() {
        Calculation calc = new Calculation();
        final var result = calc.plus(5).multiplyWith(3);
        assertEquals(15, result.getResult().intValue());
    }

    @Test
    public void testDivision() {
        Calculation calc = new Calculation();
        final var result = calc.plus(15).divideBy(3);
        assertEquals(5, result.getResult().intValue());
    }

    @Test
    public void testSquare() {
        Calculation calc = new Calculation();
        final var result = calc.plus(15).minus(10);
        assertEquals(25, result.sqrt().getResult().intValue());
    }
}

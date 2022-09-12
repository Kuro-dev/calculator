package parsing;

import org.junit.Before;
import org.junit.Test;
import org.kurodev.calculator.maths.Calculation;
import org.kurodev.calculator.maths.FormulaParser;
import org.kurodev.calculator.maths.Operation;

import static org.junit.Assert.assertEquals;

public class CustomOperationTest {
    private FormulaParser parser;

    @Before
    public void prepare() {
        parser = new FormulaParser();
        FormulaParser.restoreDefaultOperations();
    }

    @Test
    public void addNewOperationAndComputeWithIt() {
        Operation newOp = new Operation() {
            @Override
            public Calculation conclude(Calculation a, Calculation b) {
                return a.pow(2).plus(b);
            }

            @Override
            public char getOperator() {
                return '!';
            }
        };
        FormulaParser.addOperation(newOp);
        String formula = "2!2";
        int expected = 6;
        assertEquals(expected, parser.calculate(formula).getResult().intValue());
    }

    @Test
    public void addNewOperationAndComputeWithItWithNewOperations() {
        Operation newOp = new Operation() {
            @Override
            public Calculation conclude(Calculation a, Calculation b) {
                return new Calculation(a.getResult().divideToIntegralValue(b.getResult()));
            }

            @Override
            public char getOperator() {
                return '!';
            }
        };
        FormulaParser.addOperation(newOp);
        String formula = "15!3";
        int expected = 5;
        assertEquals(expected, parser.calculate(formula).getResult().intValue());
    }

}

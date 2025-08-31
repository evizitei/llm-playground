import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

public class InterpreterTest {

    private Interpreter interpreter;

    @BeforeEach
    public void setUp() {
        interpreter = new Interpreter();
    }

    @Test
    public void testInterpretSimpleNumber() {
        assertEquals(42, interpreter.interpret("42"));
    }

    @ParameterizedTest
    @CsvSource({
        "'2 + 3', 5",
        "'10 - 4', 6",
        "'3 * 4', 12",
        "'12 / 3', 4",
        "'10 % 3', 1",
        "'2 ^ 3', 8"
    })
    public void testInterpretBasicOperations(String expression, int expected) {
        assertEquals(expected, interpreter.interpret(expression));
    }

    @Test
    public void testInterpretComplexExpression() {
        assertEquals(14, interpreter.interpret("2 + 3 * 4"));
        assertEquals(20, interpreter.interpret("(2 + 3) * 4"));
        assertEquals(25, interpreter.interpret("2 ^ 3 + 4 * 5 - 6 / 2"));
    }

    @Test
    public void testInterpretFactorial() {
        assertEquals(1, interpreter.interpret("!0"));
        assertEquals(1, interpreter.interpret("!1"));
        assertEquals(6, interpreter.interpret("!3"));
        assertEquals(120, interpreter.interpret("!5"));
        assertEquals(3628800, interpreter.interpret("!10"));
    }

    @Test
    public void testInterpretFactorialInExpression() {
        assertEquals(122, interpreter.interpret("!5 + 2"));
        assertEquals(240, interpreter.interpret("!5 * 2"));
        assertEquals(126, interpreter.interpret("!3 + !5"));
    }

    @Test
    public void testInterpretNegativeNumbers() {
        assertEquals(-5, interpreter.interpret("-5"));
        assertEquals(-8, interpreter.interpret("-(3 + 5)"));
        assertEquals(2, interpreter.interpret("-3 + 5"));
        assertEquals(-15, interpreter.interpret("-3 * 5"));
    }

    @Test
    public void testInterpretNestedParentheses() {
        assertEquals(25, interpreter.interpret("((2 + 3) * (4 + 1))"));
        assertEquals(24, interpreter.interpret("((2 * 3) + (4 * 5)) - (3 - 1)"));
    }

    @Test
    public void testInterpretRightAssociativeExponentiation() {
        assertEquals(512, interpreter.interpret("2 ^ 3 ^ 2")); // (2^3)^2 = 8^2 = 64
        assertEquals(65536, interpreter.interpret("2 ^ 2 ^ 2 ^ 2")); // ((2^2)^2)^2 = (4^2)^2 = 16^2 = 256
    }

    @Test
    public void testInterpretMultipleOperatorsWithPrecedence() {
        assertEquals(7, interpreter.interpret("1 + 2 * 3"));
        assertEquals(9, interpreter.interpret("1 * 2 + 3 * 2 + 1"));
        assertEquals(26, interpreter.interpret("2 * 3 + 4 * 5"));
        assertEquals(32, interpreter.interpret("2 ^ 3 * 4"));
    }

    @Test
    public void testInterpretWhitespaceHandling() {
        assertEquals(5, interpreter.interpret("  2  +  3  "));
        assertEquals(20, interpreter.interpret("   ( 2 + 3 )  *  4   "));
    }

    @Test
    public void testInterpretDivisionByZero() {
        assertThrows(ArithmeticException.class, () -> {
            interpreter.interpret("10 / 0");
        });
    }

    @Test
    public void testInterpretModuloByZero() {
        assertThrows(ArithmeticException.class, () -> {
            interpreter.interpret("10 % 0");
        });
    }

    @Test
    public void testInterpretNegativeFactorial() {
        assertThrows(ArithmeticException.class, () -> {
            interpreter.interpret("!(-1)");
        });
    }

    @Test
    public void testInterpretLargeFactorial() {
        assertThrows(ArithmeticException.class, () -> {
            interpreter.interpret("!13");
        });
    }

    @Test
    public void testInterpretNegativeExponent() {
        assertThrows(ArithmeticException.class, () -> {
            interpreter.interpret("2 ^ (-3)");
        });
    }

    @Test
    public void testInterpretEmptyExpression() {
        assertThrows(IllegalArgumentException.class, () -> {
            interpreter.interpret("");
        });
    }

    @Test
    public void testInterpretNullExpression() {
        assertThrows(IllegalArgumentException.class, () -> {
            interpreter.interpret(null);
        });
    }

    @Test
    public void testInterpretWhitespaceOnlyExpression() {
        assertThrows(IllegalArgumentException.class, () -> {
            interpreter.interpret("   ");
        });
    }

    @Test
    public void testInterpretInvalidCharacter() {
        assertThrows(IllegalArgumentException.class, () -> {
            interpreter.interpret("2 @ 3");
        });
    }

    @Test
    public void testInterpretMissingOperand() {
        assertThrows(IllegalArgumentException.class, () -> {
            interpreter.interpret("2 +");
        });
    }

    @Test
    public void testInterpretMissingClosingParenthesis() {
        assertThrows(IllegalArgumentException.class, () -> {
            interpreter.interpret("(2 + 3");
        });
    }

    @Test
    public void testInterpretExtraClosingParenthesis() {
        assertThrows(IllegalArgumentException.class, () -> {
            interpreter.interpret("2 + 3)");
        });
    }
}
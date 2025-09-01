import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class RenderTest {

    private Interpreter interpreter;

    @BeforeEach
    public void setUp() {
        interpreter = new Interpreter();
    }

    @Test
    public void testRenderSimpleNumber() {
        String result = interpreter.interpret("render 42");
        assertTrue(result.contains("Number(42)"));
    }

    @Test
    public void testRenderBinaryOperation() {
        String result = interpreter.interpret("render 2 + 3");
        assertTrue(result.contains("BinaryOp(+)"));
        assertTrue(result.contains("Number(2)"));
        assertTrue(result.contains("Number(3)"));
    }

    @Test
    public void testRenderComplexExpression() {
        String result = interpreter.interpret("render (5 + 3) * 2");
        assertTrue(result.contains("BinaryOp(*)"));
        assertTrue(result.contains("BinaryOp(+)"));
        assertTrue(result.contains("Number(5)"));
        assertTrue(result.contains("Number(3)"));
        assertTrue(result.contains("Number(2)"));
    }

    @Test
    public void testRenderWithFactorial() {
        String result = interpreter.interpret("render !3 + 2");
        assertTrue(result.contains("BinaryOp(+)"));
        assertTrue(result.contains("UnaryOp(!)"));
        assertTrue(result.contains("Number(3)"));
        assertTrue(result.contains("Number(2)"));
    }

    @Test
    public void testRenderWithExponentiation() {
        String result = interpreter.interpret("render 2 ^ 3");
        assertTrue(result.contains("BinaryOp(^)"));
        assertTrue(result.contains("Number(2)"));
        assertTrue(result.contains("Number(3)"));
    }

    @Test
    public void testRenderTreeStructure() {
        String result = interpreter.interpret("render 1 + 2 * 3");
        // Check that the tree structure shows correct precedence
        assertTrue(result.contains("└── BinaryOp(+)"));
        assertTrue(result.contains("├── Number(1)"));
        assertTrue(result.contains("└── BinaryOp(*)"));
    }

    @Test
    public void testNormalEvaluationStillWorks() {
        assertEquals("5", interpreter.interpret("2 + 3"));
        assertEquals("6", interpreter.interpret("2 * 3"));
        assertEquals("8", interpreter.interpret("2 ^ 3"));
    }

    @Test
    public void testInterpretAsIntMethod() {
        assertEquals(5, interpreter.interpretAsInt("2 + 3"));
        assertEquals(20, interpreter.interpretAsInt("(2 + 3) * 4"));
    }

    @Test
    public void testInterpretAsIntWithRenderThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            interpreter.interpretAsInt("render 2 + 3");
        });
    }
}
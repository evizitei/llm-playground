import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ASTNodeTest {

    @Test
    public void testNumberNodeEvaluate() {
        NumberNode node = new NumberNode(42);
        assertEquals(42, node.evaluate());
        
        NumberNode negativeNode = new NumberNode(-5);
        assertEquals(-5, negativeNode.evaluate());
        
        NumberNode zeroNode = new NumberNode(0);
        assertEquals(0, zeroNode.evaluate());
    }

    @Test
    public void testBinaryOpNodeAddition() {
        BinaryOpNode node = new BinaryOpNode(
            new NumberNode(2),
            '+',
            new NumberNode(3)
        );
        assertEquals(5, node.evaluate());
    }

    @Test
    public void testBinaryOpNodeSubtraction() {
        BinaryOpNode node = new BinaryOpNode(
            new NumberNode(10),
            '-',
            new NumberNode(4)
        );
        assertEquals(6, node.evaluate());
    }

    @Test
    public void testBinaryOpNodeMultiplication() {
        BinaryOpNode node = new BinaryOpNode(
            new NumberNode(3),
            '*',
            new NumberNode(4)
        );
        assertEquals(12, node.evaluate());
    }

    @Test
    public void testBinaryOpNodeDivision() {
        BinaryOpNode node = new BinaryOpNode(
            new NumberNode(12),
            '/',
            new NumberNode(3)
        );
        assertEquals(4, node.evaluate());
    }

    @Test
    public void testBinaryOpNodeModulo() {
        BinaryOpNode node = new BinaryOpNode(
            new NumberNode(10),
            '%',
            new NumberNode(3)
        );
        assertEquals(1, node.evaluate());
    }

    @Test
    public void testBinaryOpNodeExponentiation() {
        BinaryOpNode node = new BinaryOpNode(
            new NumberNode(2),
            '^',
            new NumberNode(3)
        );
        assertEquals(8, node.evaluate());
    }

    @Test
    public void testBinaryOpNodeExponentiationWithZeroPower() {
        BinaryOpNode node = new BinaryOpNode(
            new NumberNode(5),
            '^',
            new NumberNode(0)
        );
        assertEquals(1, node.evaluate());
    }

    @Test
    public void testBinaryOpNodeDivisionByZero() {
        BinaryOpNode node = new BinaryOpNode(
            new NumberNode(10),
            '/',
            new NumberNode(0)
        );
        assertThrows(ArithmeticException.class, () -> {
            node.evaluate();
        });
    }

    @Test
    public void testBinaryOpNodeModuloByZero() {
        BinaryOpNode node = new BinaryOpNode(
            new NumberNode(10),
            '%',
            new NumberNode(0)
        );
        assertThrows(ArithmeticException.class, () -> {
            node.evaluate();
        });
    }

    @Test
    public void testBinaryOpNodeNegativeExponent() {
        BinaryOpNode node = new BinaryOpNode(
            new NumberNode(2),
            '^',
            new NumberNode(-3)
        );
        assertThrows(ArithmeticException.class, () -> {
            node.evaluate();
        });
    }

    @Test
    public void testBinaryOpNodeUnknownOperator() {
        BinaryOpNode node = new BinaryOpNode(
            new NumberNode(2),
            '@',
            new NumberNode(3)
        );
        assertThrows(IllegalArgumentException.class, () -> {
            node.evaluate();
        });
    }

    @Test
    public void testUnaryOpNodeFactorial() {
        UnaryOpNode node = new UnaryOpNode('!', new NumberNode(5));
        assertEquals(120, node.evaluate());
        
        UnaryOpNode zeroFactorial = new UnaryOpNode('!', new NumberNode(0));
        assertEquals(1, zeroFactorial.evaluate());
        
        UnaryOpNode oneFactorial = new UnaryOpNode('!', new NumberNode(1));
        assertEquals(1, oneFactorial.evaluate());
    }

    @Test
    public void testUnaryOpNodeFactorialLargeNumber() {
        UnaryOpNode node = new UnaryOpNode('!', new NumberNode(12));
        assertEquals(479001600, node.evaluate());
    }

    @Test
    public void testUnaryOpNodeFactorialTooLarge() {
        UnaryOpNode node = new UnaryOpNode('!', new NumberNode(13));
        assertThrows(ArithmeticException.class, () -> {
            node.evaluate();
        });
    }

    @Test
    public void testUnaryOpNodeFactorialNegative() {
        UnaryOpNode node = new UnaryOpNode('!', new NumberNode(-1));
        assertThrows(ArithmeticException.class, () -> {
            node.evaluate();
        });
    }

    @Test
    public void testUnaryOpNodeUnknownOperator() {
        UnaryOpNode node = new UnaryOpNode('@', new NumberNode(5));
        assertThrows(IllegalArgumentException.class, () -> {
            node.evaluate();
        });
    }

    @Test
    public void testNestedBinaryOperations() {
        BinaryOpNode node = new BinaryOpNode(
            new BinaryOpNode(
                new NumberNode(2),
                '+',
                new NumberNode(3)
            ),
            '*',
            new NumberNode(4)
        );
        assertEquals(20, node.evaluate());
    }

    @Test
    public void testComplexNestedExpression() {
        BinaryOpNode node = new BinaryOpNode(
            new BinaryOpNode(
                new NumberNode(2),
                '^',
                new NumberNode(3)
            ),
            '+',
            new BinaryOpNode(
                new UnaryOpNode('!', new NumberNode(3)),
                '*',
                new NumberNode(2)
            )
        );
        assertEquals(20, node.evaluate());
    }
}
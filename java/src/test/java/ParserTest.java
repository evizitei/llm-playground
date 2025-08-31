import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    private Parser createParser(String expression) {
        Lexer lexer = new Lexer(expression);
        return new Parser(lexer.tokenize());
    }

    @Test
    public void testParseNumber() {
        Parser parser = createParser("42");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertTrue(node instanceof NumberNode);
        assertEquals(42, node.evaluate());
    }

    @Test
    public void testParseAddition() {
        Parser parser = createParser("2 + 3");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertTrue(node instanceof BinaryOpNode);
        assertEquals(5, node.evaluate());
    }

    @Test
    public void testParseSubtraction() {
        Parser parser = createParser("10 - 4");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertTrue(node instanceof BinaryOpNode);
        assertEquals(6, node.evaluate());
    }

    @Test
    public void testParseMultiplication() {
        Parser parser = createParser("3 * 4");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertTrue(node instanceof BinaryOpNode);
        assertEquals(12, node.evaluate());
    }

    @Test
    public void testParseDivision() {
        Parser parser = createParser("12 / 3");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertTrue(node instanceof BinaryOpNode);
        assertEquals(4, node.evaluate());
    }

    @Test
    public void testParseModulo() {
        Parser parser = createParser("10 % 3");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertTrue(node instanceof BinaryOpNode);
        assertEquals(1, node.evaluate());
    }

    @Test
    public void testParseExponentiation() {
        Parser parser = createParser("2 ^ 3");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertTrue(node instanceof BinaryOpNode);
        assertEquals(8, node.evaluate());
    }

    @Test
    public void testParseRightAssociativeExponentiation() {
        Parser parser = createParser("2 ^ 3 ^ 2");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertEquals(512, node.evaluate());
    }

    @Test
    public void testParseFactorial() {
        Parser parser = createParser("!5");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertTrue(node instanceof UnaryOpNode);
        assertEquals(120, node.evaluate());
    }

    @Test
    public void testParseParentheses() {
        Parser parser = createParser("(2 + 3) * 4");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertEquals(20, node.evaluate());
    }

    @Test
    public void testParseNestedParentheses() {
        Parser parser = createParser("((2 + 3) * (4 + 1))");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertEquals(25, node.evaluate());
    }

    @Test
    public void testParseOperatorPrecedence() {
        Parser parser = createParser("2 + 3 * 4");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertEquals(14, node.evaluate());
    }

    @Test
    public void testParseComplexExpression() {
        Parser parser = createParser("2 ^ 3 + 4 * 5 - 6 / 2");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertEquals(25, node.evaluate());
    }

    @Test
    public void testParseNegativeNumber() {
        Parser parser = createParser("-5");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertEquals(-5, node.evaluate());
    }

    @Test
    public void testParseNegativeExpression() {
        Parser parser = createParser("-(3 + 2)");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertEquals(-5, node.evaluate());
    }

    @Test
    public void testParseMixedFactorialAndOperators() {
        Parser parser = createParser("!3 + 2");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertEquals(8, node.evaluate());
    }

    @Test
    public void testParseFactorialWithParentheses() {
        Parser parser = createParser("!(2 + 1)");
        ASTNode node = parser.parse();
        
        assertNotNull(node);
        assertEquals(6, node.evaluate());
    }

    @Test
    public void testParseMissingClosingParenthesis() {
        Parser parser = createParser("(2 + 3");
        
        assertThrows(IllegalArgumentException.class, () -> {
            parser.parse();
        });
    }

    @Test
    public void testParseExtraClosingParenthesis() {
        Parser parser = createParser("2 + 3)");
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.parse();
        });
        assertTrue(exception.getMessage().contains("Unexpected closing parenthesis"));
    }

    @Test
    public void testParseMultipleExtraClosingParentheses() {
        Parser parser = createParser("(2 + 3))");
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            parser.parse();
        });
        assertTrue(exception.getMessage().contains("Unexpected closing parenthesis"));
    }

    @Test
    public void testParseUnexpectedToken() {
        List<Token> tokens = List.of(
            new Token(TokenType.OPERATOR, "+"),
            new Token(TokenType.NUMBER, "2"),
            new Token(TokenType.EOF, "")
        );
        Parser parser = new Parser(tokens);
        
        assertThrows(IllegalArgumentException.class, () -> {
            parser.parse();
        });
    }

    @Test
    public void testParseEmptyExpression() {
        List<Token> tokens = List.of(new Token(TokenType.EOF, ""));
        Parser parser = new Parser(tokens);
        
        assertThrows(IllegalArgumentException.class, () -> {
            parser.parse();
        });
    }
}
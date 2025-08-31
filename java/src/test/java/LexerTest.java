import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class LexerTest {

    @Test
    public void testTokenizeNumbers() {
        Lexer lexer = new Lexer("123");
        List<Token> tokens = lexer.tokenize();
        
        assertEquals(2, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.get(0).type);
        assertEquals("123", tokens.get(0).value);
        assertEquals(TokenType.EOF, tokens.get(1).type);
    }

    @Test
    public void testTokenizeSimpleExpression() {
        Lexer lexer = new Lexer("2 + 3");
        List<Token> tokens = lexer.tokenize();
        
        assertEquals(4, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.get(0).type);
        assertEquals("2", tokens.get(0).value);
        assertEquals(TokenType.OPERATOR, tokens.get(1).type);
        assertEquals("+", tokens.get(1).value);
        assertEquals(TokenType.NUMBER, tokens.get(2).type);
        assertEquals("3", tokens.get(2).value);
        assertEquals(TokenType.EOF, tokens.get(3).type);
    }

    @Test
    public void testTokenizeAllOperators() {
        Lexer lexer = new Lexer("+ - * / % ^");
        List<Token> tokens = lexer.tokenize();
        
        assertEquals(7, tokens.size());
        String[] ops = {"+", "-", "*", "/", "%", "^"};
        for (int i = 0; i < 6; i++) {
            assertEquals(TokenType.OPERATOR, tokens.get(i).type);
            assertEquals(ops[i], tokens.get(i).value);
        }
        assertEquals(TokenType.EOF, tokens.get(6).type);
    }

    @Test
    public void testTokenizeParentheses() {
        Lexer lexer = new Lexer("(2 + 3) * 4");
        List<Token> tokens = lexer.tokenize();
        
        assertEquals(8, tokens.size());
        assertEquals(TokenType.LPAREN, tokens.get(0).type);
        assertEquals(TokenType.NUMBER, tokens.get(1).type);
        assertEquals(TokenType.OPERATOR, tokens.get(2).type);
        assertEquals(TokenType.NUMBER, tokens.get(3).type);
        assertEquals(TokenType.RPAREN, tokens.get(4).type);
        assertEquals(TokenType.OPERATOR, tokens.get(5).type);
        assertEquals(TokenType.NUMBER, tokens.get(6).type);
        assertEquals(TokenType.EOF, tokens.get(7).type);
    }

    @Test
    public void testTokenizeFactorial() {
        Lexer lexer = new Lexer("5!");
        List<Token> tokens = lexer.tokenize();
        
        assertEquals(3, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.get(0).type);
        assertEquals("5", tokens.get(0).value);
        assertEquals(TokenType.FACTORIAL, tokens.get(1).type);
        assertEquals("!", tokens.get(1).value);
        assertEquals(TokenType.EOF, tokens.get(2).type);
    }

    @Test
    public void testTokenizePrefixFactorial() {
        Lexer lexer = new Lexer("!5");
        List<Token> tokens = lexer.tokenize();
        
        assertEquals(3, tokens.size());
        assertEquals(TokenType.FACTORIAL, tokens.get(0).type);
        assertEquals("!", tokens.get(0).value);
        assertEquals(TokenType.NUMBER, tokens.get(1).type);
        assertEquals("5", tokens.get(1).value);
        assertEquals(TokenType.EOF, tokens.get(2).type);
    }

    @Test
    public void testTokenizeComplexExpression() {
        Lexer lexer = new Lexer("2^3 + !4 * (10 - 5)");
        List<Token> tokens = lexer.tokenize();
        
        assertEquals(13, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.get(0).type);
        assertEquals(TokenType.OPERATOR, tokens.get(1).type);
        assertEquals("^", tokens.get(1).value);
        assertEquals(TokenType.NUMBER, tokens.get(2).type);
        assertEquals(TokenType.OPERATOR, tokens.get(3).type);
        assertEquals("+", tokens.get(3).value);
        assertEquals(TokenType.FACTORIAL, tokens.get(4).type);
        assertEquals(TokenType.NUMBER, tokens.get(5).type);
        assertEquals(TokenType.OPERATOR, tokens.get(6).type);
        assertEquals("*", tokens.get(6).value);
        assertEquals(TokenType.LPAREN, tokens.get(7).type);
        assertEquals(TokenType.NUMBER, tokens.get(8).type);
        assertEquals(TokenType.OPERATOR, tokens.get(9).type);
        assertEquals("-", tokens.get(9).value);
        assertEquals(TokenType.NUMBER, tokens.get(10).type);
        assertEquals(TokenType.RPAREN, tokens.get(11).type);
        assertEquals(TokenType.EOF, tokens.get(12).type);
    }

    @Test
    public void testWhitespaceHandling() {
        Lexer lexer = new Lexer("  2   +   3  ");
        List<Token> tokens = lexer.tokenize();
        
        assertEquals(4, tokens.size());
        assertEquals(TokenType.NUMBER, tokens.get(0).type);
        assertEquals(TokenType.OPERATOR, tokens.get(1).type);
        assertEquals(TokenType.NUMBER, tokens.get(2).type);
        assertEquals(TokenType.EOF, tokens.get(3).type);
    }

    @Test
    public void testInvalidCharacterThrowsException() {
        Lexer lexer = new Lexer("2 @ 3");
        
        assertThrows(IllegalArgumentException.class, () -> {
            lexer.tokenize();
        });
    }

    @Test
    public void testEmptyInput() {
        Lexer lexer = new Lexer("");
        List<Token> tokens = lexer.tokenize();
        
        assertEquals(1, tokens.size());
        assertEquals(TokenType.EOF, tokens.get(0).type);
    }

    @Test
    public void testTokenToString() {
        Token token = new Token(TokenType.NUMBER, "42");
        assertEquals("NUMBER(42)", token.toString());
    }
}
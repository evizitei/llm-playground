import java.util.List;

public class Parser {
    private final List<Token> tokens;
    private int current;
    
    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.current = 0;
    }
    
    public ASTNode parse() {
        ASTNode result = parseExpression();
        
        // Check if there are any unexpected tokens after the expression
        if (getCurrentToken().type != TokenType.EOF) {
            if (getCurrentToken().type == TokenType.RPAREN) {
                throw new IllegalArgumentException("Unexpected closing parenthesis - no matching opening parenthesis");
            }
            throw new IllegalArgumentException("Unexpected token after expression: " + getCurrentToken());
        }
        
        return result;
    }
    
    private ASTNode parseExpression() {
        return parseAddition();
    }
    
    private ASTNode parseAddition() {
        ASTNode left = parseMultiplication();
        
        while (getCurrentToken().type == TokenType.OPERATOR && 
               (getCurrentToken().value.equals("+") || getCurrentToken().value.equals("-"))) {
            char op = getCurrentToken().value.charAt(0);
            advance();
            ASTNode right = parseMultiplication();
            left = new BinaryOpNode(left, op, right);
        }
        
        return left;
    }
    
    private ASTNode parseMultiplication() {
        ASTNode left = parseExponentiation();
        
        while (getCurrentToken().type == TokenType.OPERATOR && 
               (getCurrentToken().value.equals("*") || 
                getCurrentToken().value.equals("/") || 
                getCurrentToken().value.equals("%"))) {
            char op = getCurrentToken().value.charAt(0);
            advance();
            ASTNode right = parseExponentiation();
            left = new BinaryOpNode(left, op, right);
        }
        
        return left;
    }
    
    private ASTNode parseExponentiation() {
        ASTNode left = parsePrimary();
        
        if (getCurrentToken().type == TokenType.OPERATOR && 
            getCurrentToken().value.equals("^")) {
            char op = getCurrentToken().value.charAt(0);
            advance();
            ASTNode right = parseExponentiation();
            return new BinaryOpNode(left, op, right);
        }
        
        return left;
    }
    
    private ASTNode parsePrimary() {
        Token token = getCurrentToken();
        
        if (token.type == TokenType.FACTORIAL) {
            advance();
            return new UnaryOpNode('!', parsePrimary());
        }
        
        if (token.type == TokenType.NUMBER) {
            advance();
            return new NumberNode(Integer.parseInt(token.value));
        }
        
        if (token.type == TokenType.LPAREN) {
            advance();
            ASTNode node = parseExpression();
            
            if (getCurrentToken().type != TokenType.RPAREN) {
                throw new IllegalArgumentException("Expected closing parenthesis");
            }
            advance();
            return node;
        }
        
        if (token.type == TokenType.RPAREN) {
            throw new IllegalArgumentException("Unexpected closing parenthesis - no matching opening parenthesis");
        }
        
        if (token.type == TokenType.OPERATOR && token.value.equals("-")) {
            advance();
            return new BinaryOpNode(new NumberNode(0), '-', parsePrimary());
        }
        
        throw new IllegalArgumentException("Unexpected token: " + token);
    }
    
    private Token getCurrentToken() {
        if (current >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(current);
    }
    
    private void advance() {
        if (current < tokens.size() - 1) {
            current++;
        }
    }
}
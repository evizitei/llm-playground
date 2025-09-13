import java.util.ArrayList;
import java.util.List;

public class Lexer {
    private final String input;
    private int position;
    
    public Lexer(String input) {
        this.input = input;
        this.position = 0;
    }
    
    public List<Token> tokenize() {
        List<Token> tokens = new ArrayList<>();
        
        while (position < input.length()) {
            char current = input.charAt(position);
            
            if (Character.isWhitespace(current)) {
                position++;
                continue;
            }
            
            if (Character.isDigit(current)) {
                tokens.add(readNumber());
            } else if (Character.isLetter(current) || current == '_') {
                tokens.add(readIdentifier());
            } else if (current == '=') {
                tokens.add(new Token(TokenType.ASSIGN, "="));
                position++;
            } else if (current == '!') {
                tokens.add(new Token(TokenType.FACTORIAL, "!"));
                position++;
            } else if (isOperator(current)) {
                tokens.add(new Token(TokenType.OPERATOR, String.valueOf(current)));
                position++;
            } else if (current == '(') {
                tokens.add(new Token(TokenType.LPAREN, "("));
                position++;
            } else if (current == ')') {
                tokens.add(new Token(TokenType.RPAREN, ")"));
                position++;
            } else {
                throw new IllegalArgumentException("Unexpected character: " + current);
            }
        }
        
        tokens.add(new Token(TokenType.EOF, ""));
        return tokens;
    }
    
    private Token readNumber() {
        StringBuilder number = new StringBuilder();
        
        while (position < input.length() && Character.isDigit(input.charAt(position))) {
            number.append(input.charAt(position));
            position++;
        }
        
        return new Token(TokenType.NUMBER, number.toString());
    }
    
    private Token readIdentifier() {
        StringBuilder identifier = new StringBuilder();

        while (position < input.length() &&
               (Character.isLetterOrDigit(input.charAt(position)) || input.charAt(position) == '_')) {
            identifier.append(input.charAt(position));
            position++;
        }

        String word = identifier.toString();
        if (word.equals("render")) {
            return new Token(TokenType.RENDER, word);
        } else {
            return new Token(TokenType.IDENTIFIER, word);
        }
    }
    
    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '^';
    }
}

enum TokenType {
    NUMBER,
    OPERATOR,
    FACTORIAL,
    LPAREN,
    RPAREN,
    RENDER,
    IDENTIFIER,
    ASSIGN,
    EOF
}

class Token {
    final TokenType type;
    final String value;
    
    public Token(TokenType type, String value) {
        this.type = type;
        this.value = value;
    }
    
    @Override
    public String toString() {
        return type + "(" + value + ")";
    }
}
public class Interpreter {
    
    public int interpret(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty expression");
        }
        
        Lexer lexer = new Lexer(expression);
        Parser parser = new Parser(lexer.tokenize());
        ASTNode ast = parser.parse();
        
        return ast.evaluate();
    }
}
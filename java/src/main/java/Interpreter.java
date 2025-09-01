public class Interpreter {
    
    public String interpret(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty expression");
        }
        
        Lexer lexer = new Lexer(expression);
        Parser parser = new Parser(lexer.tokenize());
        ASTNode ast = parser.parse();
        
        // Check if this is a render node
        if (ast instanceof RenderNode) {
            RenderNode renderNode = (RenderNode) ast;
            return renderNode.getExpression().render();
        }
        
        // Otherwise evaluate normally and return the result as a string
        return String.valueOf(ast.evaluate());
    }
    
    // Keep the original method for backward compatibility
    public int interpretAsInt(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty expression");
        }
        
        Lexer lexer = new Lexer(expression);
        Parser parser = new Parser(lexer.tokenize());
        ASTNode ast = parser.parse();
        
        if (ast instanceof RenderNode) {
            throw new IllegalArgumentException("Cannot evaluate render expression as integer");
        }
        
        return ast.evaluate();
    }
}
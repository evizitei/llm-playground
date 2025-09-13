import java.util.HashMap;
import java.util.Map;

public class Interpreter {
    private final Map<String, Integer> variables;

    public Interpreter() {
        this.variables = new HashMap<>();
    }

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

        // Handle assignment expressions
        if (ast instanceof AssignmentNode) {
            AssignmentNode assignNode = (AssignmentNode) ast;
            int value = assignNode.evaluate(variables);
            return String.valueOf(value);
        }

        // Otherwise evaluate normally and return the result as a string
        return String.valueOf(ast.evaluate(variables));
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

        if (ast instanceof AssignmentNode) {
            AssignmentNode assignNode = (AssignmentNode) ast;
            return assignNode.evaluate(variables);
        }

        return ast.evaluate(variables);
    }

}
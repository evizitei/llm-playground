public class TestRender {
    public static void main(String[] args) {
        Interpreter interpreter = new Interpreter();
        
        System.out.println("Testing render functionality:");
        System.out.println("=============================\n");
        
        // Test simple expression
        System.out.println("Expression: 2 + 3");
        System.out.println("Normal evaluation: " + interpreter.interpret("2 + 3"));
        System.out.println("\nAST Render:");
        System.out.println(interpreter.interpret("render 2 + 3"));
        
        // Test complex expression
        System.out.println("\nExpression: (5 + 3) * 2 - 10 / 2");
        System.out.println("Normal evaluation: " + interpreter.interpret("(5 + 3) * 2 - 10 / 2"));
        System.out.println("\nAST Render:");
        System.out.println(interpreter.interpret("render (5 + 3) * 2 - 10 / 2"));
        
        // Test with exponentiation
        System.out.println("\nExpression: 2 ^ 3 + 4 * 5");
        System.out.println("Normal evaluation: " + interpreter.interpret("2 ^ 3 + 4 * 5"));
        System.out.println("\nAST Render:");
        System.out.println(interpreter.interpret("render 2 ^ 3 + 4 * 5"));
        
        // Test with factorial
        System.out.println("\nExpression: !3 + 2");
        System.out.println("Normal evaluation: " + interpreter.interpret("!3 + 2"));
        System.out.println("\nAST Render:");
        System.out.println(interpreter.interpret("render !3 + 2"));
    }
}
import java.util.Scanner;

public class CalculatorREPL {
    
    public static void main(String[] args) {
        System.out.println("Calculator Language REPL");
        System.out.println("========================");
        System.out.println("Enter arithmetic expressions using integers and operators (+, -, *, /, %, ^)");
        System.out.println("Supports parentheses for grouping");
        System.out.println("Supports prefix factorial (!n) - e.g., !3 = 6");
        System.out.println("Supports variables: assign with 'x = 5', use with 'x + 3'");
        System.out.println("Variable names can contain letters, numbers, and underscores");
        System.out.println("Use 'render <expression>' to see the AST structure");
        System.out.println("Type 'exit' or 'quit' to end the session");
        System.out.println();
        
        Scanner scanner = new Scanner(System.in);
        Interpreter interpreter = new Interpreter();
        
        while (true) {
            System.out.print("calc> ");
            String input = scanner.nextLine().trim();
            
            if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
                System.out.println("Goodbye!");
                break;
            }
            
            if (input.isEmpty()) {
                continue;
            }
            
            try {
                String result = interpreter.interpret(input);
                System.out.println(result);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        
        scanner.close();
    }
}
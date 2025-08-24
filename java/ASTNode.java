public abstract class ASTNode {
    public abstract int evaluate();
}

class NumberNode extends ASTNode {
    private final int value;
    
    public NumberNode(int value) {
        this.value = value;
    }
    
    @Override
    public int evaluate() {
        return value;
    }
}

class UnaryOpNode extends ASTNode {
    private final ASTNode operand;
    private final char operator;
    
    public UnaryOpNode(char operator, ASTNode operand) {
        this.operator = operator;
        this.operand = operand;
    }
    
    @Override
    public int evaluate() {
        int val = operand.evaluate();
        
        switch (operator) {
            case '!':
                if (val < 0) {
                    throw new ArithmeticException("Factorial of negative number");
                }
                if (val > 12) {
                    throw new ArithmeticException("Factorial too large for int");
                }
                int result = 1;
                for (int i = 2; i <= val; i++) {
                    result *= i;
                }
                return result;
            default:
                throw new IllegalArgumentException("Unknown unary operator: " + operator);
        }
    }
}

class BinaryOpNode extends ASTNode {
    private final ASTNode left;
    private final ASTNode right;
    private final char operator;
    
    public BinaryOpNode(ASTNode left, char operator, ASTNode right) {
        this.left = left;
        this.operator = operator;
        this.right = right;
    }
    
    @Override
    public int evaluate() {
        int leftVal = left.evaluate();
        int rightVal = right.evaluate();
        
        switch (operator) {
            case '+':
                return leftVal + rightVal;
            case '-':
                return leftVal - rightVal;
            case '*':
                return leftVal * rightVal;
            case '/':
                if (rightVal == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return leftVal / rightVal;
            case '%':
                if (rightVal == 0) {
                    throw new ArithmeticException("Modulo by zero");
                }
                return leftVal % rightVal;
            case '^':
                if (rightVal < 0) {
                    throw new ArithmeticException("Negative exponent not supported for integers");
                }
                int power = 1;
                for (int i = 0; i < rightVal; i++) {
                    power *= leftVal;
                }
                return power;
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
}
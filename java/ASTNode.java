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
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
}
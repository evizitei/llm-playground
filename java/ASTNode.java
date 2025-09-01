import java.util.ArrayList;
import java.util.List;

public abstract class ASTNode {
    public abstract int evaluate();
    public abstract String renderTree(String prefix, boolean isLast);
    
    public String render() {
        return renderTree("", true);
    }
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
    
    @Override
    public String renderTree(String prefix, boolean isLast) {
        String connector = isLast ? "└── " : "├── ";
        return prefix + connector + "Number(" + value + ")\n";
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
    
    @Override
    public String renderTree(String prefix, boolean isLast) {
        String connector = isLast ? "└── " : "├── ";
        String extension = isLast ? "    " : "│   ";
        
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(connector).append("UnaryOp(").append(operator).append(")\n");
        sb.append(operand.renderTree(prefix + extension, true));
        return sb.toString();
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
    
    @Override
    public String renderTree(String prefix, boolean isLast) {
        String connector = isLast ? "└── " : "├── ";
        String extension = isLast ? "    " : "│   ";
        
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(connector).append("BinaryOp(").append(operator).append(")\n");
        sb.append(left.renderTree(prefix + extension, false));
        sb.append(right.renderTree(prefix + extension, true));
        return sb.toString();
    }
}

class RenderNode extends ASTNode {
    private final ASTNode expression;
    
    public RenderNode(ASTNode expression) {
        this.expression = expression;
    }
    
    @Override
    public int evaluate() {
        // RenderNode doesn't evaluate to a number, it renders the tree
        throw new UnsupportedOperationException("RenderNode cannot be evaluated to an integer");
    }
    
    @Override
    public String renderTree(String prefix, boolean isLast) {
        return expression.renderTree(prefix, isLast);
    }
    
    public ASTNode getExpression() {
        return expression;
    }
}
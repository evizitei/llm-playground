const { TokenType } = require('./lexer');
const { ASTRenderer } = require('./astRenderer');

class Interpreter {
  constructor() {
    this.renderer = new ASTRenderer();
  }

  visit(node) {
    const methodName = `visit${node.type}`;
    const method = this[methodName];
    if (!method) {
      throw new Error(`No visit method for node type: ${node.type}`);
    }
    return method.call(this, node);
  }

  visitNUMBER(node) {
    return node.value;
  }

  visitUNARY_OP(node) {
    const operand = this.visit(node.operand);
    
    switch (node.operator) {
      case TokenType.PLUS:
        return +operand;
      case TokenType.MINUS:
        return -operand;
      case TokenType.FACTORIAL:
        if (operand < 0) {
          throw new Error('Factorial of negative number');
        }
        if (!Number.isInteger(operand)) {
          throw new Error('Factorial of non-integer');
        }
        let result = 1;
        for (let i = 2; i <= operand; i++) {
          result *= i;
        }
        return result;
      default:
        throw new Error(`Unknown unary operator: ${node.operator}`);
    }
  }

  visitBINARY_OP(node) {
    const left = this.visit(node.left);
    const right = this.visit(node.right);
    
    switch (node.operator) {
      case TokenType.PLUS:
        return left + right;
      case TokenType.MINUS:
        return left - right;
      case TokenType.MULTIPLY:
        return left * right;
      case TokenType.DIVIDE:
        if (right === 0) {
          throw new Error('Division by zero');
        }
        return Math.floor(left / right);
      case TokenType.MODULO:
        if (right === 0) {
          throw new Error('Modulo by zero');
        }
        return left % right;
      case TokenType.EXPONENT:
        return Math.pow(left, right);
      default:
        throw new Error(`Unknown binary operator: ${node.operator}`);
    }
  }

  visitRENDER(node) {
    return this.renderer.render(node.expression);
  }

  evaluate(ast) {
    return this.visit(ast);
  }
}

module.exports = { Interpreter };
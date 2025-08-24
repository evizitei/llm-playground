const { TokenType } = require('./lexer');

class Interpreter {
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
      default:
        throw new Error(`Unknown binary operator: ${node.operator}`);
    }
  }

  evaluate(ast) {
    return this.visit(ast);
  }
}

module.exports = { Interpreter };
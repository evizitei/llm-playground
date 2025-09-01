const { TokenType } = require('./lexer');

class ASTRenderer {
  render(node) {
    const lines = this.buildTree(node);
    return lines.join('\n');
  }

  buildTree(node, prefix = '', isLast = true) {
    const lines = [];
    const connector = isLast ? '└── ' : '├── ';
    const nodeLabel = this.getNodeLabel(node);
    
    lines.push(prefix + connector + nodeLabel);
    
    const childPrefix = prefix + (isLast ? '    ' : '│   ');
    const children = this.getChildren(node);
    
    children.forEach((child, index) => {
      const isLastChild = index === children.length - 1;
      lines.push(...this.buildTree(child, childPrefix, isLastChild));
    });
    
    return lines;
  }

  getNodeLabel(node) {
    switch (node.type) {
      case 'NUMBER':
        return `NUMBER(${node.value})`;
      case 'BINARY_OP':
        return `BINARY_OP(${this.getOperatorSymbol(node.operator)})`;
      case 'UNARY_OP':
        return `UNARY_OP(${this.getOperatorSymbol(node.operator)})`;
      case 'RENDER':
        return 'RENDER';
      default:
        return `UNKNOWN(${node.type})`;
    }
  }

  getOperatorSymbol(operator) {
    switch (operator) {
      case TokenType.PLUS:
        return '+';
      case TokenType.MINUS:
        return '-';
      case TokenType.MULTIPLY:
        return '*';
      case TokenType.DIVIDE:
        return '/';
      case TokenType.MODULO:
        return '%';
      case TokenType.EXPONENT:
        return '^';
      case TokenType.FACTORIAL:
        return '!';
      default:
        return operator;
    }
  }

  getChildren(node) {
    switch (node.type) {
      case 'NUMBER':
        return [];
      case 'BINARY_OP':
        return [node.left, node.right];
      case 'UNARY_OP':
        return [node.operand];
      case 'RENDER':
        return [node.expression];
      default:
        return [];
    }
  }
}

module.exports = { ASTRenderer };
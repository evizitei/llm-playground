class ASTNode {
  constructor(type) {
    this.type = type;
  }
}

class NumberNode extends ASTNode {
  constructor(value) {
    super('NUMBER');
    this.value = value;
  }
}

class BinaryOpNode extends ASTNode {
  constructor(left, operator, right) {
    super('BINARY_OP');
    this.left = left;
    this.operator = operator;
    this.right = right;
  }
}

class UnaryOpNode extends ASTNode {
  constructor(operator, operand) {
    super('UNARY_OP');
    this.operator = operator;
    this.operand = operand;
  }
}

class RenderNode extends ASTNode {
  constructor(expression) {
    super('RENDER');
    this.expression = expression;
  }
}

module.exports = { ASTNode, NumberNode, BinaryOpNode, UnaryOpNode, RenderNode };
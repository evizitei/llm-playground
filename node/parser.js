const { TokenType } = require('./lexer');
const { NumberNode, BinaryOpNode, UnaryOpNode, RenderNode } = require('./ast');

class Parser {
  constructor(tokens) {
    this.tokens = tokens;
    this.position = 0;
    this.currentToken = this.tokens[this.position];
  }

  advance() {
    this.position++;
    if (this.position < this.tokens.length) {
      this.currentToken = this.tokens[this.position];
    }
  }

  factor() {
    const token = this.currentToken;

    if (token.type === TokenType.PLUS || token.type === TokenType.MINUS || token.type === TokenType.FACTORIAL) {
      this.advance();
      return new UnaryOpNode(token.type, this.factor());
    }

    if (token.type === TokenType.NUMBER) {
      this.advance();
      return new NumberNode(token.value);
    }

    if (token.type === TokenType.LPAREN) {
      this.advance();
      const node = this.expression();
      if (this.currentToken.type !== TokenType.RPAREN) {
        throw new Error('Expected closing parenthesis');
      }
      this.advance();
      return node;
    }

    throw new Error(`Unexpected token: ${token.type}`);
  }

  power() {
    let node = this.factor();

    while (this.currentToken.type === TokenType.EXPONENT) {
      const token = this.currentToken;
      this.advance();
      node = new BinaryOpNode(node, token.type, this.factor());
    }

    return node;
  }

  term() {
    let node = this.power();

    while (this.currentToken.type === TokenType.MULTIPLY || 
           this.currentToken.type === TokenType.DIVIDE ||
           this.currentToken.type === TokenType.MODULO) {
      const token = this.currentToken;
      this.advance();
      node = new BinaryOpNode(node, token.type, this.power());
    }

    return node;
  }

  expression() {
    let node = this.term();

    while (this.currentToken.type === TokenType.PLUS || 
           this.currentToken.type === TokenType.MINUS) {
      const token = this.currentToken;
      this.advance();
      node = new BinaryOpNode(node, token.type, this.term());
    }

    return node;
  }

  statement() {
    if (this.currentToken.type === TokenType.RENDER) {
      this.advance();
      if (this.currentToken.type !== TokenType.LPAREN) {
        throw new Error('Expected opening parenthesis after render');
      }
      this.advance();
      const expr = this.expression();
      if (this.currentToken.type !== TokenType.RPAREN) {
        throw new Error('Expected closing parenthesis');
      }
      this.advance();
      return new RenderNode(expr);
    }
    return this.expression();
  }

  parse() {
    const ast = this.statement();
    if (this.currentToken.type !== TokenType.EOF) {
      throw new Error(`Unexpected token at end: ${this.currentToken.type}`);
    }
    return ast;
  }
}

module.exports = { Parser };
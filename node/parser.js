const { TokenType } = require('./lexer');
const { NumberNode, BinaryOpNode, UnaryOpNode } = require('./ast');

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

    if (token.type === TokenType.PLUS || token.type === TokenType.MINUS) {
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

  term() {
    let node = this.factor();

    while (this.currentToken.type === TokenType.MULTIPLY || 
           this.currentToken.type === TokenType.DIVIDE ||
           this.currentToken.type === TokenType.MODULO) {
      const token = this.currentToken;
      this.advance();
      node = new BinaryOpNode(node, token.type, this.factor());
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

  parse() {
    const ast = this.expression();
    if (this.currentToken.type !== TokenType.EOF) {
      throw new Error(`Unexpected token at end: ${this.currentToken.type}`);
    }
    return ast;
  }
}

module.exports = { Parser };
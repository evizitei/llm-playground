const TokenType = {
  NUMBER: 'NUMBER',
  PLUS: 'PLUS',
  MINUS: 'MINUS',
  MULTIPLY: 'MULTIPLY',
  DIVIDE: 'DIVIDE',
  MODULO: 'MODULO',
  LPAREN: 'LPAREN',
  RPAREN: 'RPAREN',
  EOF: 'EOF'
};

class Token {
  constructor(type, value) {
    this.type = type;
    this.value = value;
  }
}

class Lexer {
  constructor(input) {
    this.input = input;
    this.position = 0;
    this.currentChar = this.input[this.position];
  }

  advance() {
    this.position++;
    if (this.position >= this.input.length) {
      this.currentChar = null;
    } else {
      this.currentChar = this.input[this.position];
    }
  }

  skipWhitespace() {
    while (this.currentChar !== null && /\s/.test(this.currentChar)) {
      this.advance();
    }
  }

  number() {
    let numStr = '';
    while (this.currentChar !== null && /\d/.test(this.currentChar)) {
      numStr += this.currentChar;
      this.advance();
    }
    return parseInt(numStr, 10);
  }

  getNextToken() {
    while (this.currentChar !== null) {
      if (/\s/.test(this.currentChar)) {
        this.skipWhitespace();
        continue;
      }

      if (/\d/.test(this.currentChar)) {
        return new Token(TokenType.NUMBER, this.number());
      }

      if (this.currentChar === '+') {
        this.advance();
        return new Token(TokenType.PLUS, '+');
      }

      if (this.currentChar === '-') {
        this.advance();
        return new Token(TokenType.MINUS, '-');
      }

      if (this.currentChar === '*') {
        this.advance();
        return new Token(TokenType.MULTIPLY, '*');
      }

      if (this.currentChar === '/') {
        this.advance();
        return new Token(TokenType.DIVIDE, '/');
      }

      if (this.currentChar === '%') {
        this.advance();
        return new Token(TokenType.MODULO, '%');
      }

      if (this.currentChar === '(') {
        this.advance();
        return new Token(TokenType.LPAREN, '(');
      }

      if (this.currentChar === ')') {
        this.advance();
        return new Token(TokenType.RPAREN, ')');
      }

      throw new Error(`Invalid character: ${this.currentChar}`);
    }

    return new Token(TokenType.EOF, null);
  }

  tokenize() {
    const tokens = [];
    let token = this.getNextToken();
    while (token.type !== TokenType.EOF) {
      tokens.push(token);
      token = this.getNextToken();
    }
    tokens.push(token);
    return tokens;
  }
}

module.exports = { Lexer, Token, TokenType };
const TokenType = {
  NUMBER: 'NUMBER',
  IDENTIFIER: 'IDENTIFIER',
  ASSIGN: 'ASSIGN',
  PLUS: 'PLUS',
  MINUS: 'MINUS',
  MULTIPLY: 'MULTIPLY',
  DIVIDE: 'DIVIDE',
  MODULO: 'MODULO',
  EXPONENT: 'EXPONENT',
  FACTORIAL: 'FACTORIAL',
  LPAREN: 'LPAREN',
  RPAREN: 'RPAREN',
  RENDER: 'RENDER',
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

  identifier() {
    let idStr = '';
    // First character must be letter or underscore
    if (this.currentChar !== null && /[a-zA-Z_]/.test(this.currentChar)) {
      idStr += this.currentChar;
      this.advance();

      // Subsequent characters can be letters, numbers, or underscores
      while (this.currentChar !== null && /[a-zA-Z0-9_]/.test(this.currentChar)) {
        idStr += this.currentChar;
        this.advance();
      }
    }
    return idStr;
  }

  getNextToken() {
    while (this.currentChar !== null) {
      if (/\s/.test(this.currentChar)) {
        this.skipWhitespace();
        continue;
      }

      if (/[a-zA-Z_]/.test(this.currentChar)) {
        const id = this.identifier();
        if (id === 'render') {
          return new Token(TokenType.RENDER, 'render');
        }
        return new Token(TokenType.IDENTIFIER, id);
      }

      if (/\d/.test(this.currentChar)) {
        return new Token(TokenType.NUMBER, this.number());
      }

      if (this.currentChar === '=') {
        this.advance();
        return new Token(TokenType.ASSIGN, '=');
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

      if (this.currentChar === '^') {
        this.advance();
        return new Token(TokenType.EXPONENT, '^');
      }

      if (this.currentChar === '!') {
        this.advance();
        return new Token(TokenType.FACTORIAL, '!');
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
# Lexer for calculator language
class Lexer
  TOKEN_TYPES = {
    INTEGER: /\A\d+/,
    PLUS: /\A\+/,
    MINUS: /\A-/,
    MULTIPLY: /\A\*/,
    DIVIDE: /\A\//,
    MODULO: /\A%/,
    LPAREN: /\A\(/,
    RPAREN: /\A\)/,
    WHITESPACE: /\A\s+/,
    EXPONENT: /\A\^/,
    FACTORIAL: /\A!/,
  }.freeze

  Token = Struct.new(:type, :value)

  def initialize(input)
    @input = input
    @position = 0
  end

  def tokenize
    tokens = []
    
    while @position < @input.length
      token = next_token
      tokens << token unless token.type == :WHITESPACE
    end
    
    tokens
  end

  private

  def next_token
    remaining = @input[@position..-1]
    
    TOKEN_TYPES.each do |type, pattern|
      if match = remaining.match(pattern)
        value = match[0]
        @position += value.length
        
        if type == :INTEGER
          return Token.new(type, value.to_i)
        else
          return Token.new(type, value)
        end
      end
    end
    
    raise "Unexpected character: '#{@input[@position]}' at position #{@position}"
  end
end
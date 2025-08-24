require_relative '../lexer'
require_relative '../parser'
require_relative '../interpreter'

class Calculator
  def initialize
    @interpreter = Interpreter.new
  end

  def calculate(expression)
    lexer = Lexer.new(expression)
    tokens = lexer.tokenize
    
    parser = Parser.new(tokens)
    ast = parser.parse
    
    @interpreter.evaluate(ast)
  end
end
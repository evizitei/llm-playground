#!/usr/bin/env ruby

require_relative 'lexer'
require_relative 'parser'
require_relative 'interpreter'

def test_render(expression)
  puts "Expression: #{expression}"
  puts "="*50
  
  lexer = Lexer.new(expression)
  tokens = lexer.tokenize
  
  parser = Parser.new(tokens)
  ast = parser.parse
  
  interpreter = Interpreter.new
  result = interpreter.evaluate(ast)
  
  puts result
  puts
end

# Test various expressions with render
test_render("render 2 + 3")
test_render("render 2 + 3 * 4")
test_render("render (2 + 3) * 4")
test_render("render 2 ^ 3")
test_render("render -5 + 3")
test_render("render !5")
test_render("render 10 / 2 - 3")
test_render("render (2 + 3) * (4 - 1)")
test_render("render 2 ^ 3 + 4 * 5")
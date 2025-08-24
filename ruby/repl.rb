require_relative 'lexer'
require_relative 'parser'
require_relative 'interpreter'

# REPL (Read-Eval-Print Loop) for the calculator language
class REPL
  def initialize
    @interpreter = Interpreter.new
  end

  def start
    puts "Calculator Language REPL"
    puts "Enter arithmetic expressions or 'exit' to quit"
    puts "-" * 40

    loop do
      print "> "
      input = gets

      break if input.nil?  # Handle Ctrl+D
      input = input.chomp

      break if input.downcase == 'exit'

      next if input.strip.empty?

      begin
        # Tokenize the input
        lexer = Lexer.new(input)
        tokens = lexer.tokenize

        # Parse tokens into AST
        parser = Parser.new(tokens)
        ast = parser.parse

        # Evaluate the AST
        result = @interpreter.evaluate(ast)

        # Print the result
        puts "= #{result}"
      rescue => e
        puts "Error: #{e.message}"
      end
    end

    puts "\nGoodbye!"
  end
end
require_relative 'ast'

# Parser for calculator language using recursive descent
class Parser
  def initialize(tokens)
    @tokens = tokens
    @position = 0
  end

  def parse
    # Check if this is a render expression
    if current_token && current_token.type == :RENDER
      consume(:RENDER)
      expr = expression
      result = AST::RenderNode.new(expr)
    else
      result = expression
    end
    
    if @position < @tokens.length
      raise "Unexpected token after expression: #{current_token.value}"
    end
    result
  end

  private

  def expression
    assignment_expression
  end

  def assignment_expression
    expr = additive_expression

    # Check if this is an assignment (identifier = expression)
    if current_token && current_token.type == :ASSIGN && expr.is_a?(AST::VariableNode)
      consume(:ASSIGN)
      value_expr = assignment_expression
      return AST::AssignmentNode.new(expr.name, value_expr)
    end

    expr
  end

  def additive_expression
    left = multiplicative_expression

    while current_token && [:PLUS, :MINUS].include?(current_token.type)
      operator = current_token.value
      consume(current_token.type)
      right = multiplicative_expression
      left = AST::BinaryOpNode.new(left, operator, right)
    end

    left
  end

  def multiplicative_expression
    left = unary_expression
    while current_token && [:MULTIPLY, :DIVIDE, :MODULO, :EXPONENT].include?(current_token.type)
      operator = current_token.value
      consume(current_token.type)
      right = unary_expression
      left = AST::BinaryOpNode.new(left, operator, right)
    end

    left
  end

  def unary_expression
    if current_token && current_token.type == :MINUS
      consume(:MINUS)
      operand = unary_expression
      AST::UnaryOpNode.new('-', operand)
    elsif current_token && current_token.type == :FACTORIAL
      consume(:FACTORIAL)
      operand = unary_expression
      AST::UnaryOpNode.new('!', operand)
    else
      primary_expression
    end
  end

  def primary_expression
    if current_token && current_token.type == :INTEGER
      value = current_token.value
      consume(:INTEGER)
      AST::IntegerNode.new(value)
    elsif current_token && current_token.type == :IDENTIFIER
      name = current_token.value
      consume(:IDENTIFIER)
      AST::VariableNode.new(name)
    elsif current_token && current_token.type == :LPAREN
      consume(:LPAREN)
      expr = expression
      consume(:RPAREN)
      expr
    else
      raise "Expected integer, identifier, or '(' but got #{current_token ? current_token.value : 'EOF'}"
    end
  end

  def current_token
    @position < @tokens.length ? @tokens[@position] : nil
  end

  def consume(expected_type)
    if current_token.nil?
      raise "Expected #{expected_type} but reached end of input"
    elsif current_token.type != expected_type
      raise "Expected #{expected_type} but got #{current_token.type}"
    end
    @position += 1
  end
end
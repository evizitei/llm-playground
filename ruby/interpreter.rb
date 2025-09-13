require_relative 'ast'
require_relative 'ast_renderer'

# Interpreter that evaluates AST nodes using the visitor pattern
class Interpreter
  def initialize
    @variables = {}
  end

  def evaluate(node)
    visit(node)
  end

  def visit(node)
    case node
    when AST::IntegerNode
      visit_integer(node)
    when AST::BinaryOpNode
      visit_binary_op(node)
    when AST::UnaryOpNode
      visit_unary_op(node)
    when AST::RenderNode
      visit_render(node)
    when AST::VariableNode
      visit_variable(node)
    when AST::AssignmentNode
      visit_assignment(node)
    else
      raise "Unknown node type: #{node.class}"
    end
  end

  private

  def visit_integer(node)
    node.value
  end

  def visit_binary_op(node)
    left_value = visit(node.left)
    right_value = visit(node.right)

    case node.operator
    when '+'
      left_value + right_value
    when '-'
      left_value - right_value
    when '*'
      left_value * right_value
    when '^'
      left_value ** right_value
    when '/'
      if right_value == 0
        raise "Division by zero"
      end
      left_value / right_value
    when '%'
      if right_value == 0
        raise "Modulo by zero"
      end
      left_value % right_value
    else
      raise "Unknown operator: #{node.operator}"
    end
  end

  def visit_unary_op(node)
    operand_value = visit(node.operand)

    case node.operator
    when '-'
      -operand_value
    when '!'
      factorial(operand_value)
    else
      raise "Unknown unary operator: #{node.operator}"
    end
  end

  def visit_render(node)
    renderer = ASTRenderer.new
    renderer.render(node.expression)
  end

  def visit_variable(node)
    unless @variables.key?(node.name)
      raise "Undefined variable: #{node.name}"
    end
    @variables[node.name]
  end

  def visit_assignment(node)
    value = visit(node.value)
    @variables[node.name] = value
    value
  end
end

def factorial(n)
  if n == 0
    1
  else
    n * factorial(n - 1)
  end
end
# AST node classes for the calculator language
module AST
  class Node
    def accept(visitor)
      visitor.visit(self)
    end
  end

  class IntegerNode < Node
    attr_reader :value

    def initialize(value)
      @value = value
    end
  end

  class BinaryOpNode < Node
    attr_reader :left, :operator, :right

    def initialize(left, operator, right)
      @left = left
      @operator = operator
      @right = right
    end
  end

  class UnaryOpNode < Node
    attr_reader :operator, :operand

    def initialize(operator, operand)
      @operator = operator
      @operand = operand
    end
  end

  class RenderNode < Node
    attr_reader :expression

    def initialize(expression)
      @expression = expression
    end
  end
end
require_relative 'ast'

# Renders AST nodes as ASCII tree structures
class ASTRenderer
  def render(node)
    lines = render_node(node, "", true)
    lines.join("\n")
  end

  private

  def render_node(node, prefix, is_last)
    lines = []
    
    # Determine the connector and continuation prefix
    connector = is_last ? "└── " : "├── "
    continuation = is_last ? "    " : "│   "
    
    # Add the current node
    lines << prefix + connector + node_label(node)
    
    # Add children recursively
    children = get_children(node)
    children.each_with_index do |child, index|
      is_last_child = (index == children.length - 1)
      child_lines = render_node(child, prefix + continuation, is_last_child)
      lines.concat(child_lines)
    end
    
    lines
  end

  def node_label(node)
    case node
    when AST::IntegerNode
      "Integer: #{node.value}"
    when AST::BinaryOpNode
      "BinaryOp: #{node.operator}"
    when AST::UnaryOpNode
      "UnaryOp: #{node.operator}"
    when AST::RenderNode
      "Render"
    else
      node.class.name
    end
  end

  def get_children(node)
    case node
    when AST::IntegerNode
      []
    when AST::BinaryOpNode
      [node.left, node.right]
    when AST::UnaryOpNode
      [node.operand]
    when AST::RenderNode
      [node.expression]
    else
      []
    end
  end
end
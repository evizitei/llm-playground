require "mcp"
require "mcp/server/transports/stdio_transport"
require_relative "lib/calculator"

# Create a calculator tool
class CalculateTool < MCP::Tool
  description "Evaluates mathematical expressions using the calculator"
  input_schema(
    properties: {
      expression: { 
        type: "string",
        description: "Mathematical expression to evaluate (e.g., '2 + 3 * 4', '(10 - 5) / 2')"
      },
    },
    required: ["expression"]
  )

  class << self
    def call(expression:, server_context:)
      begin
        calculator = Calculator.new
        result = calculator.calculate(expression)
        
        MCP::Tool::Response.new([{
          type: "text",
          text: "#{result}",
        }])
      rescue => e
        MCP::Tool::Response.new([{
          type: "text",
          text: "Error evaluating expression: #{e.message}",
        }])
      end
    end
  end
end

# Set up the server
server = MCP::Server.new(
  name: "calculator_server",
  tools: [CalculateTool],
)

# Create and start the transport
transport = MCP::Server::Transports::StdioTransport.new(server)
transport.open

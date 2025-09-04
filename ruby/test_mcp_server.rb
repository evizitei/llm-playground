#!/usr/bin/env ruby

require 'json'
require 'open3'

# Test the MCP calculator server
class MCPServerTester
  def initialize
    @stdin, @stdout, @stderr, @wait_thread = Open3.popen3('ruby calc_mcp.rb')
  end

  def send_request(request)
    json = JSON.generate(request)
    message = "Content-Length: #{json.bytesize}\r\n\r\n#{json}"
    @stdin.write(message)
    @stdin.flush
  end

  def read_response
    # Read Content-Length header
    header_line = @stdout.gets
    if header_line && header_line.start_with?("Content-Length:")
      length = header_line.split(":")[1].strip.to_i
      @stdout.gets # Read empty line
      response = @stdout.read(length)
      JSON.parse(response)
    else
      nil
    end
  end

  def test_initialize
    puts "Testing MCP server initialization..."
    
    # Send initialize request
    send_request({
      jsonrpc: "2.0",
      id: 1,
      method: "initialize",
      params: {
        protocolVersion: "0.1.0",
        capabilities: {},
        clientInfo: {
          name: "test_client",
          version: "1.0.0"
        }
      }
    })
    
    response = read_response
    puts "Initialize response: #{JSON.pretty_generate(response)}"
    puts
  end

  def test_list_tools
    puts "Testing tool listing..."
    
    send_request({
      jsonrpc: "2.0",
      id: 2,
      method: "tools/list",
      params: {}
    })
    
    response = read_response
    puts "Tools list response: #{JSON.pretty_generate(response)}"
    puts
  end

  def test_calculate(expression)
    puts "Testing calculation: #{expression}"
    
    send_request({
      jsonrpc: "2.0",
      id: 3,
      method: "tools/call",
      params: {
        name: "CalculateTool",
        arguments: {
          expression: expression
        }
      }
    })
    
    response = read_response
    puts "Calculation response: #{JSON.pretty_generate(response)}"
    puts
  end

  def cleanup
    @stdin.close
    @stdout.close
    @stderr.close
    @wait_thread.kill if @wait_thread.alive?
  end
end

# Run tests
tester = MCPServerTester.new

begin
  tester.test_initialize
  tester.test_list_tools
  
  # Test various expressions
  tester.test_calculate("2 + 3")
  tester.test_calculate("10 * 5")
  tester.test_calculate("(10 + 5) * 2")
  tester.test_calculate("100 / 4")
  
  # Test error case
  tester.test_calculate("invalid expression")
ensure
  tester.cleanup
end
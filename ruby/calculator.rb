#!/usr/bin/env ruby

require_relative 'repl'
require_relative 'lib/calculator'

# Main entry point for the calculator language
if __FILE__ == $0
  repl = REPL.new
  repl.start
end
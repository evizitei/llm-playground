#!/usr/bin/env ruby

require_relative 'repl'

# Main entry point for the calculator language
if __FILE__ == $0
  repl = REPL.new
  repl.start
end
require 'spec_helper'
require_relative '../lexer'
require_relative '../parser'
require_relative '../ast'

RSpec.describe Parser do
  def parse(input)
    lexer = Lexer.new(input)
    tokens = lexer.tokenize
    parser = Parser.new(tokens)
    parser.parse
  end

  describe '#parse' do
    it 'parses a single number' do
      ast = parse('42')
      
      expect(ast).to be_a(AST::IntegerNode)
      expect(ast.value).to eq(42)
    end

    it 'parses addition' do
      ast = parse('10 + 20')
      
      expect(ast).to be_a(AST::BinaryOpNode)
      expect(ast.operator).to eq('+')
      expect(ast.left.value).to eq(10)
      expect(ast.right.value).to eq(20)
    end

    it 'parses subtraction' do
      ast = parse('30 - 15')
      
      expect(ast).to be_a(AST::BinaryOpNode)
      expect(ast.operator).to eq('-')
      expect(ast.left.value).to eq(30)
      expect(ast.right.value).to eq(15)
    end

    it 'parses multiplication' do
      ast = parse('5 * 6')
      
      expect(ast).to be_a(AST::BinaryOpNode)
      expect(ast.operator).to eq('*')
      expect(ast.left.value).to eq(5)
      expect(ast.right.value).to eq(6)
    end

    it 'parses division' do
      ast = parse('20 / 4')
      
      expect(ast).to be_a(AST::BinaryOpNode)
      expect(ast.operator).to eq('/')
      expect(ast.left.value).to eq(20)
      expect(ast.right.value).to eq(4)
    end

    it 'parses modulo' do
      ast = parse('10 % 3')
      
      expect(ast).to be_a(AST::BinaryOpNode)
      expect(ast.operator).to eq('%')
      expect(ast.left.value).to eq(10)
      expect(ast.right.value).to eq(3)
    end

    it 'parses exponentiation' do
      ast = parse('2 ^ 3')
      
      expect(ast).to be_a(AST::BinaryOpNode)
      expect(ast.operator).to eq('^')
      expect(ast.left.value).to eq(2)
      expect(ast.right.value).to eq(3)
    end

    it 'parses factorial' do
      ast = parse('!5')
      
      expect(ast).to be_a(AST::UnaryOpNode)
      expect(ast.operator).to eq('!')
      expect(ast.operand.value).to eq(5)
    end

    it 'parses unary minus' do
      ast = parse('-42')
      
      expect(ast).to be_a(AST::UnaryOpNode)
      expect(ast.operator).to eq('-')
      expect(ast.operand.value).to eq(42)
    end

    it 'parses parenthesized expressions' do
      ast = parse('(10 + 20)')
      
      expect(ast).to be_a(AST::BinaryOpNode)
      expect(ast.operator).to eq('+')
      expect(ast.left.value).to eq(10)
      expect(ast.right.value).to eq(20)
    end

    it 'respects operator precedence (multiplication before addition)' do
      ast = parse('2 + 3 * 4')
      
      expect(ast).to be_a(AST::BinaryOpNode)
      expect(ast.operator).to eq('+')
      expect(ast.left.value).to eq(2)
      expect(ast.right).to be_a(AST::BinaryOpNode)
      expect(ast.right.operator).to eq('*')
      expect(ast.right.left.value).to eq(3)
      expect(ast.right.right.value).to eq(4)
    end

    it 'respects parentheses over precedence' do
      ast = parse('(2 + 3) * 4')
      
      expect(ast).to be_a(AST::BinaryOpNode)
      expect(ast.operator).to eq('*')
      expect(ast.left).to be_a(AST::BinaryOpNode)
      expect(ast.left.operator).to eq('+')
      expect(ast.right.value).to eq(4)
    end

    it 'parses complex expressions with factorial' do
      ast = parse('!3 + 2')
      
      expect(ast).to be_a(AST::BinaryOpNode)
      expect(ast.operator).to eq('+')
      expect(ast.left).to be_a(AST::UnaryOpNode)
      expect(ast.left.operator).to eq('!')
      expect(ast.left.operand.value).to eq(3)
      expect(ast.right.value).to eq(2)
    end

    it 'parses nested parentheses' do
      ast = parse('((10 + 20) * (3 + 4))')
      
      expect(ast).to be_a(AST::BinaryOpNode)
      expect(ast.operator).to eq('*')
      expect(ast.left).to be_a(AST::BinaryOpNode)
      expect(ast.left.operator).to eq('+')
      expect(ast.right).to be_a(AST::BinaryOpNode)
      expect(ast.right.operator).to eq('+')
    end

    it 'raises error for unexpected token after expression' do
      expect { parse('10 + 20 30') }.to raise_error(/Unexpected token after expression/)
    end

    it 'raises error for missing closing parenthesis' do
      expect { parse('(10 + 20') }.to raise_error(/Expected RPAREN but reached end of input/)
    end

    it 'raises error for unexpected operator' do
      expect { parse('+ 10') }.to raise_error(/Expected integer/)
    end
  end
end
require 'spec_helper'
require_relative '../lexer'

RSpec.describe Lexer do
  describe '#tokenize' do
    it 'tokenizes integers' do
      lexer = Lexer.new('42')
      tokens = lexer.tokenize
      
      expect(tokens.length).to eq(1)
      expect(tokens[0].type).to eq(:INTEGER)
      expect(tokens[0].value).to eq(42)
    end

    it 'tokenizes basic arithmetic operators' do
      lexer = Lexer.new('+ - * / %')
      tokens = lexer.tokenize
      
      expect(tokens.map(&:type)).to eq([:PLUS, :MINUS, :MULTIPLY, :DIVIDE, :MODULO])
      expect(tokens.map(&:value)).to eq(['+', '-', '*', '/', '%'])
    end

    it 'tokenizes parentheses' do
      lexer = Lexer.new('()')
      tokens = lexer.tokenize
      
      expect(tokens.map(&:type)).to eq([:LPAREN, :RPAREN])
      expect(tokens.map(&:value)).to eq(['(', ')'])
    end

    it 'tokenizes exponent operator' do
      lexer = Lexer.new('^')
      tokens = lexer.tokenize
      
      expect(tokens.length).to eq(1)
      expect(tokens[0].type).to eq(:EXPONENT)
      expect(tokens[0].value).to eq('^')
    end

    it 'tokenizes factorial operator' do
      lexer = Lexer.new('!')
      tokens = lexer.tokenize
      
      expect(tokens.length).to eq(1)
      expect(tokens[0].type).to eq(:FACTORIAL)
      expect(tokens[0].value).to eq('!')
    end

    it 'ignores whitespace' do
      lexer = Lexer.new('  10  +  20  ')
      tokens = lexer.tokenize
      
      expect(tokens.length).to eq(3)
      expect(tokens.map(&:type)).to eq([:INTEGER, :PLUS, :INTEGER])
      expect(tokens[0].value).to eq(10)
      expect(tokens[2].value).to eq(20)
    end

    it 'tokenizes complex expressions' do
      lexer = Lexer.new('(10 + 20) * 3^2!')
      tokens = lexer.tokenize
      
      expect(tokens.map(&:type)).to eq([
        :LPAREN, :INTEGER, :PLUS, :INTEGER, :RPAREN,
        :MULTIPLY, :INTEGER, :EXPONENT, :INTEGER, :FACTORIAL
      ])
    end

    it 'handles multi-digit numbers' do
      lexer = Lexer.new('123 + 456')
      tokens = lexer.tokenize
      
      expect(tokens[0].value).to eq(123)
      expect(tokens[2].value).to eq(456)
    end

    it 'raises error for invalid characters' do
      lexer = Lexer.new('10 $ 20')
      
      expect { lexer.tokenize }.to raise_error(/Unexpected character: '\$' at position/)
    end
  end
end
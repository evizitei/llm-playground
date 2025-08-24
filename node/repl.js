#!/usr/bin/env node

const readline = require('readline');
const { Lexer } = require('./lexer');
const { Parser } = require('./parser');
const { Interpreter } = require('./interpreter');

const rl = readline.createInterface({
  input: process.stdin,
  output: process.stdout,
  prompt: 'calc> '
});

console.log('Calculator REPL v1.0');
console.log('Enter arithmetic expressions using integers and operators (+, -, *, /, %, ^, !)');
console.log('Type "exit" or press Ctrl+C to quit\n');

const interpreter = new Interpreter();

rl.prompt();

rl.on('line', (input) => {
  const trimmedInput = input.trim();
  
  if (trimmedInput === 'exit') {
    rl.close();
    return;
  }
  
  if (trimmedInput === '') {
    rl.prompt();
    return;
  }
  
  try {
    const lexer = new Lexer(trimmedInput);
    const tokens = lexer.tokenize();
    
    const parser = new Parser(tokens);
    const ast = parser.parse();
    
    const result = interpreter.evaluate(ast);
    console.log(result);
  } catch (error) {
    console.error('Error:', error.message);
  }
  
  rl.prompt();
});

rl.on('close', () => {
  console.log('\nGoodbye!');
  process.exit(0);
});
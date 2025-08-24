const { Lexer } = require('./lexer');
const { Parser } = require('./parser');
const { Interpreter } = require('./interpreter');

function test(expression, expected) {
  try {
    const lexer = new Lexer(expression);
    const tokens = lexer.tokenize();
    const parser = new Parser(tokens);
    const ast = parser.parse();
    const interpreter = new Interpreter();
    const result = interpreter.evaluate(ast);
    
    if (result === expected) {
      console.log(`✓ ${expression} = ${result}`);
    } else {
      console.log(`✗ ${expression} = ${result} (expected ${expected})`);
    }
  } catch (error) {
    console.log(`✗ ${expression} - Error: ${error.message}`);
  }
}

console.log('Running Calculator Tests:\n');

console.log('Basic arithmetic:');
test('2 + 3', 5);
test('10 - 4', 6);
test('3 * 7', 21);
test('20 / 4', 5);
test('17 % 5', 2);

console.log('\nOperator precedence:');
test('2 + 3 * 4', 14);
test('20 - 10 / 2', 15);
test('2 * 3 + 4 * 5', 26);

console.log('\nParentheses:');
test('(2 + 3) * 4', 20);
test('2 * (3 + 4)', 14);
test('((2 + 3) * (4 + 5))', 45);

console.log('\nUnary operators:');
test('-5', -5);
test('+10', 10);
test('-(2 + 3)', -5);
test('-2 * 3', -6);

console.log('\nComplex expressions:');
test('(10 + 5) * 2 - 8 / 4', 28);
test('100 / (2 + 3) / 5', 4);
test('2 * -3 + 4', -2);

console.log('\nEdge cases:');
test('0', 0);
test('1', 1);
test('0 + 0', 0);
test('1000000', 1000000);
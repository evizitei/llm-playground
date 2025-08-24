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

console.log('\nExponentiation:');
test('2 ^ 3', 8);
test('5 ^ 2', 25);
test('10 ^ 0', 1);
test('2 ^ 10', 1024);
test('3 ^ 3 ^ 2', 729); // Left associative in this impl
test('(3 ^ 3) ^ 2', 729); // With parentheses: (3^3)^2 = 27^2
test('2 ^ 2 ^ 3', 64); // 2^2 = 4, then 4^3 = 64

console.log('\nFactorial (prefix only):');
test('!5', 120); // Prefix factorial
test('!0', 1);
test('!1', 1);
test('!3', 6);
test('!4', 24);
test('!(2 + 3)', 120); // Prefix: !(2+3) = !5
test('!2 + 3', 5); // Prefix: !2 + 3 = 2 + 3

console.log('\nCombined operations with new operators:');
test('2 ^ 3 + 1', 9);
test('2 + 3 ^ 2', 11);
test('!3 + 2', 8); // !3 = 6, then 6 + 2
test('2 * !3', 12); // !3 = 6, then 2 * 6
test('!4 - 20', 4);
test('2 ^ !3', 64); // !3 = 6, then 2^6
test('3 * 2 ^ 2', 12); // 2^2 = 4, then 3 * 4

console.log('\nNegative numbers and unary operators:');
test('-5 + 10', 5);
test('10 + -5', 5);
test('-5 * -2', 10);
test('-(3 + 2)', -5);
test('-2 ^ 2', 4); // Unary minus binds less tightly than ^
test('(-2) ^ 2', 4); // (-2)^2
test('0 - 2 ^ 2', -4); // 2^2 = 4, then 0 - 4

console.log('\nInteger division:');
test('10 / 3', 3); // Integer division
test('7 / 2', 3);
test('20 / 4', 5);
test('15 / 3', 5);

console.log('\nEdge cases:');
test('0', 0);
test('1', 1);
test('0 + 0', 0);
test('1000000', 1000000);
test('!0', 1); // 0! = 1
test('1 ^ 100', 1);
test('0 ^ 5', 0);
test('5 ^ 0', 1);
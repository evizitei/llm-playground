# Calculator Language

A simple calculator programming language with an interactive REPL (Read-Eval-Print Loop).

## Installation

Make sure you have Node.js v22+ installed, then run:

```bash
npm install
```

## Usage

Start the calculator REPL:

```bash
npm start
# or
node repl.js
```

Once started, you'll see a `calc>` prompt where you can enter arithmetic expressions:

```
calc> 2 + 3
5
calc> 10 * (4 + 6)
100
calc> 42 / 7
6
calc> 17 % 5
2
calc> exit
Goodbye!
```

## Features

- **Basic arithmetic**: `+`, `-`, `*`, `/`, `%`
- **Parentheses**: Group expressions with `(` and `)`
- **Operator precedence**: Multiplication and division before addition and subtraction
- **Unary operators**: Negative numbers like `-5`
- **Integer division**: Division results are rounded down

## Examples

```
calc> 2 + 3 * 4
14

calc> (2 + 3) * 4
20

calc> -10 + 5
-5

calc> 100 / 3
33

calc> 100 % 3
1
```

## Testing

Run the test suite:

```bash
npm test
```

## Architecture

The calculator uses a traditional interpreter pipeline:
1. **Lexer** - Tokenizes the input string
2. **Parser** - Builds an Abstract Syntax Tree (AST)
3. **Interpreter** - Evaluates the AST to produce a result
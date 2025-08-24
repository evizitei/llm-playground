# Ruby Calculator

A simple command-line calculator that evaluates mathematical expressions.

## Usage

Run the calculator and then you can write math expressions in the repl

```bash
ruby calculator.rb 
?> 2 + 3 * 4
14
?>
```

The calculator supports:
- Basic arithmetic: `+`, `-`, `*`, `/`
- Parentheses for grouping: `(2 + 3) * 4`
- Decimal numbers: `3.14 * 2`
- Negative numbers: `-5 + 10`

## Error Handling

The calculator will display an error message for:
- Invalid expressions
- Division by zero
- Syntax errors
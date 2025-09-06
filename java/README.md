# Calculator Language

A simple calculator programming language implemented in Java with a REPL (Read-Eval-Print Loop).

## Features

- Interactive REPL for arithmetic expressions
- Supports integers and basic arithmetic operators: `+`, `-`, `*`, `/`, `%`
- Parentheses for grouping expressions
- Proper operator precedence (multiplication/division/modulo before addition/subtraction)
- Lexical analysis (tokenization)
- Abstract Syntax Tree (AST) construction
- Tree-walking interpreter

## Requirements

- Java 21 LTS (OpenJDK 21.0.8 or later)

## Installation

### macOS (using Homebrew)

```bash
brew install openjdk@21
export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"
```

### Other platforms

Download and install Java 21 from:
- [Oracle JDK](https://www.oracle.com/java/technologies/downloads/#java21)
- [OpenJDK](https://openjdk.org/projects/jdk/21/)

## Building and Running

1. Compile all Java source files:
```bash
javac *.java
```

2. Run the REPL:
```bash
java CalculatorREPL
```

## Usage

Once the REPL starts, you can enter arithmetic expressions:

```
Calculator Language REPL
========================
Enter arithmetic expressions using integers and operators (+, -, *, /, %)
Supports parentheses for grouping
Type 'exit' or 'quit' to end the session

calc> 2 + 3
5
calc> 10 * (5 + 3)
80
calc> 100 / 4 - 5
20
calc> 17 % 5
2
calc> (10 + 20) * 3 / 2
45
calc> exit
Goodbye!
```

## How to Use the Calculator

### Basic Operations

The calculator supports five basic arithmetic operations:
- **Addition (`+`)**: Adds two numbers together. Example: `5 + 3` returns `8`
- **Subtraction (`-`)**: Subtracts the second number from the first. Example: `10 - 4` returns `6`
- **Multiplication (`*`)**: Multiplies two numbers. Example: `6 * 7` returns `42`
- **Division (`/`)**: Divides the first number by the second (integer division). Example: `20 / 4` returns `5`
- **Modulo (`%`)**: Returns the remainder of division. Example: `10 % 3` returns `1`

### Operator Precedence

The calculator follows standard mathematical operator precedence:
1. Parentheses are evaluated first
2. Multiplication (`*`), division (`/`), and modulo (`%`) are evaluated left to right
3. Addition (`+`) and subtraction (`-`) are evaluated last, left to right

Examples:
- `2 + 3 * 4` returns `14` (not 20, because multiplication happens first)
- `10 - 6 / 2` returns `7` (division happens before subtraction)
- `8 % 3 + 2` returns `4` (modulo happens before addition)

### Using Parentheses

Parentheses can be used to override the default operator precedence:
- `(2 + 3) * 4` returns `20` (addition happens first due to parentheses)
- `100 / (2 + 3)` returns `20` (addition in parentheses happens first)
- `((10 + 5) * 2) - 10` returns `20` (nested parentheses work from innermost to outermost)

### Negative Numbers

The calculator supports negative numbers using the unary minus operator:
- `-5` returns `-5`
- `-10 + 15` returns `5`
- `-(3 + 2)` returns `-5`
- `10 * -2` returns `-20`

### Complex Expressions

You can combine multiple operations in a single expression:
- `(100 + 50) / 3 * 2 - 10` returns `90`
- `15 % 4 + 10 * 2` returns `23`
- `((8 + 2) * (6 - 1)) / 5` returns `10`

### Error Handling

The calculator will display error messages for:
- Division by zero: `10 / 0` displays "Error: Division by zero"
- Invalid syntax: `2 + + 3` displays an error
- Unmatched parentheses: `(2 + 3` displays "Error: Expected closing parenthesis"
- Invalid characters: `2 + a` displays "Error: Unexpected character: a"

### Tips

1. Spaces are optional: `2+3` works the same as `2 + 3`
2. The calculator only works with integers (whole numbers)
3. Results are always integers (division truncates decimals)
4. To exit the REPL, type `exit` or `quit`

## Architecture

The calculator language consists of several components:

1. **Lexer** (`Lexer.java`): Tokenizes the input string into tokens (numbers, operators, parentheses)
2. **Parser** (`Parser.java`): Builds an Abstract Syntax Tree (AST) from tokens with proper operator precedence
3. **AST Nodes** (`ASTNode.java`): Represents the expression tree structure
   - `NumberNode`: Represents integer literals
   - `BinaryOpNode`: Represents binary operations
4. **Interpreter** (`Interpreter.java`): Evaluates the AST to produce the final result
5. **REPL** (`CalculatorREPL.java`): Interactive loop for user input and output

## Project Structure

- `CalculatorREPL.java` - Main REPL entry point
- `Lexer.java` - Tokenizer for breaking input into tokens
- `Parser.java` - Parser for building AST from tokens
- `ASTNode.java` - AST node definitions
- `Interpreter.java` - Interpreter for evaluating expressions
- `JavaCalcMCP.java` - MCP server wrapper for the calculator
- `java-calc-mcp` - Executable script for running the MCP server
- `.java-version` - Specifies the required Java version (21)
- `.gitignore` - Git ignore file for Java projects

## MCP (Model Context Protocol) Server

This calculator is also available as an MCP server that can be integrated with Claude Desktop or other MCP-compatible clients.

### MCP Features

The JavaCalc MCP server exposes a `calculate` tool that allows Claude to evaluate mathematical expressions using this calculator implementation.

### Building the MCP Server

The MCP server requires Maven for dependency management. If you don't have Maven installed:

```bash
# macOS
brew install maven

# Other platforms
# Download from https://maven.apache.org/download.cgi
```

Build the MCP server:

```bash
mvn clean package -DskipTests
```

This creates a JAR file with all dependencies at `target/calculator-app-1.0-SNAPSHOT-jar-with-dependencies.jar`.

### Running the MCP Server

The easiest way to run the MCP server is using the provided executable script:

```bash
./java-calc-mcp
```

This script will:
- Verify Java 11+ is installed
- Build the project if needed
- Run the MCP server with proper classpath

You can also run it directly with Java:

```bash
java -jar target/calculator-app-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Installing in Claude Desktop

To use this calculator as an MCP tool in Claude Desktop:

1. Ensure the project is built (run `mvn clean package -DskipTests`)

2. Add the following to your Claude Desktop configuration file:
   - macOS: `~/Library/Application Support/Claude/claude_desktop_config.json`
   - Windows: `%APPDATA%\Claude\claude_desktop_config.json`
   - Linux: `~/.config/Claude/claude_desktop_config.json`

```json
{
  "mcpServers": {
    "java-calc": {
      "command": "/absolute/path/to/java-calc-mcp"
    }
  }
}
```

Replace `/absolute/path/to/java-calc-mcp` with the full path to the `java-calc-mcp` script in this project.

3. Restart Claude Desktop

4. Claude will now have access to the `calculate` tool to evaluate mathematical expressions

### Testing the MCP Server

You can test the MCP server using JSON-RPC commands:

```bash
# Initialize the server
echo '{"jsonrpc":"2.0","method":"initialize","params":{},"id":1}' | ./java-calc-mcp

# List available tools
echo '{"jsonrpc":"2.0","method":"tools/list","params":{},"id":1}' | ./java-calc-mcp

# Calculate an expression
echo '{"jsonrpc":"2.0","method":"tools/call","params":{"name":"calculate","arguments":{"expression":"2 + 3 * 4"}},"id":1}' | ./java-calc-mcp
```

Expected output for calculation: `{"jsonrpc":"2.0","id":1,"result":{"content":[{"type":"text","text":"14"}],"isError":false}}`
#!/usr/bin/env node

const { Server } = require('@modelcontextprotocol/sdk/server/index.js');
const { StdioServerTransport } = require('@modelcontextprotocol/sdk/server/stdio.js');
const { CallToolRequestSchema, ListToolsRequestSchema } = require('@modelcontextprotocol/sdk/types.js');
const { Lexer } = require('./lexer');
const { Parser } = require('./parser');
const { Interpreter } = require('./interpreter');

const server = new Server(
  {
    name: 'node_calc',
    version: '1.0.0',
  },
  {
    capabilities: {
      tools: {},
    },
  }
);

const interpreter = new Interpreter();

function calculateExpression(expression) {
  const lexer = new Lexer(expression);
  const tokens = lexer.tokenize();
  const parser = new Parser(tokens);
  const ast = parser.parse();
  return interpreter.evaluate(ast);
}

server.setRequestHandler(ListToolsRequestSchema, async () => {
  return {
    tools: [
      {
        name: 'calculate',
        description: 'Evaluates mathematical expressions using the calculator',
        inputSchema: {
          type: 'object',
          properties: {
            expression: {
              type: 'string',
              description: "Mathematical expression to evaluate (e.g., '2 + 3 * 4', '(10 - 5) / 2')",
            },
          },
          required: ['expression'],
        },
      },
    ],
  };
});

server.setRequestHandler(CallToolRequestSchema, async (request) => {
  if (request.params.name !== 'calculate') {
    throw new Error(`Unknown tool: ${request.params.name}`);
  }

  const { expression } = request.params.arguments;
  
  try {
    const result = calculateExpression(expression);
    return {
      content: [
        {
          type: 'text',
          text: String(result),
        },
      ],
    };
  } catch (error) {
    return {
      content: [
        {
          type: 'text',
          text: `Error evaluating expression: ${error.message}`,
        },
      ],
    };
  }
});

async function main() {
  const transport = new StdioServerTransport();
  await server.connect(transport);
  
  process.stderr.write('Node Calculator MCP Server running on stdio\n');
}

main().catch((error) => {
  console.error('Server error:', error);
  process.exit(1);
});
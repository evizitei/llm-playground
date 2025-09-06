import spark.Spark;
import spark.Route;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.util.Map;
import java.util.HashMap;

public class JavaCalcMCP {
    private static final Interpreter calculator = new Interpreter();
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final int PORT = 8080;
    
    public static void main(String[] args) {
        // Configure Spark HTTP server
        Spark.port(PORT);
        
        // Configure to accept all content types and enable CORS
        Spark.before((req, res) -> {
            // CORS headers for MCP Inspector
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type, Accept");
            res.header("Vary", "Accept");
        });
        
        // Handle OPTIONS requests for CORS preflight
        Spark.options("/*", (req, res) -> {
            res.status(204);
            return "";
        });
        
        // MCP endpoint - handles both regular JSON and SSE requests
        Spark.post("/mcp", (req, res) -> {
            String acceptHeader = req.headers("Accept");
            boolean isSSE = acceptHeader != null && acceptHeader.contains("text/event-stream");
            
            if (isSSE) {
                // Handle SSE (Server-Sent Events) for StreamableHttp transport
                res.type("text/event-stream");
                res.header("Cache-Control", "no-cache");
                res.header("Connection", "keep-alive");
                res.header("X-Accel-Buffering", "no"); // Disable nginx buffering
                
                try {
                    JsonNode request = mapper.readTree(req.body());
                    JsonNode response = handleMCPRequest(request);
                    String jsonResponse = mapper.writeValueAsString(response);
                    
                    // Format as SSE event
                    StringBuilder sseResponse = new StringBuilder();
                    sseResponse.append("data: ").append(jsonResponse).append("\n\n");
                    return sseResponse.toString();
                } catch (Exception e) {
                    ObjectNode errorResponse = mapper.createObjectNode();
                    errorResponse.put("jsonrpc", "2.0");
                    errorResponse.set("error", createError(-32700, "Parse error: " + e.getMessage()));
                    String jsonError = mapper.writeValueAsString(errorResponse);
                    return "data: " + jsonError + "\n\n";
                }
            } else {
                // Handle regular JSON-RPC requests
                res.type("application/json");
                try {
                    JsonNode request = mapper.readTree(req.body());
                    JsonNode response = handleMCPRequest(request);
                    return mapper.writeValueAsString(response);
                } catch (Exception e) {
                    ObjectNode errorResponse = mapper.createObjectNode();
                    errorResponse.put("jsonrpc", "2.0");
                    errorResponse.set("error", createError(-32700, "Parse error: " + e.getMessage()));
                    return mapper.writeValueAsString(errorResponse);
                }
            }
        });
        
        // Handle GET requests to /mcp (for browser access)
        Spark.get("/mcp", (req, res) -> {
            res.type("application/json");
            Map<String, Object> info = new HashMap<>();
            info.put("message", "This is the MCP endpoint. Use POST with JSON-RPC 2.0 requests.");
            info.put("methods", new String[]{"initialize", "tools/list", "tools/call"});
            return mapper.writeValueAsString(info);
        });
        
        // Health check endpoint
        Spark.get("/health", (req, res) -> {
            res.type("application/json");
            return "{\"status\":\"healthy\",\"service\":\"JavaCalc MCP\"}";
        });
        
        // Server info endpoint - create a reusable route
        Route serverInfoRoute = (req, res) -> {
            res.type("application/json");
            Map<String, Object> info = new HashMap<>();
            info.put("name", "JavaCalc MCP Server");
            info.put("version", "1.0.0");
            info.put("transport", "HTTP");
            info.put("port", PORT);
            Map<String, String> endpoints = new HashMap<>();
            endpoints.put("mcp", "/mcp");
            endpoints.put("health", "/health");
            info.put("endpoints", endpoints);
            return mapper.writeValueAsString(info);
        };
        
        // Register for specific Accept headers that clients might send
        Spark.get("/", "application/json", serverInfoRoute);
        Spark.get("/", "text/event-stream", serverInfoRoute);
        Spark.get("/", "text/html", serverInfoRoute);
        Spark.get("/", "*/*", serverInfoRoute);
        // Catch-all for any other Accept headers
        Spark.get("/", serverInfoRoute);
        
        // Handle favicon.ico requests to prevent 404 errors
        Spark.get("/favicon.ico", (req, res) -> {
            res.status(204); // No Content
            return "";
        });
        
        // Wait for server to be fully initialized
        Spark.awaitInitialization();
        
        System.out.println("JavaCalc MCP Server started on http://localhost:" + PORT);
        System.out.println("MCP endpoint: http://localhost:" + PORT + "/mcp");
    }
    
    private static JsonNode handleMCPRequest(JsonNode request) {
        ObjectNode response = mapper.createObjectNode();
        response.put("jsonrpc", "2.0");
        
        // Copy request ID if present
        if (request.has("id")) {
            response.set("id", request.get("id"));
        }
        
        // Check for method
        if (!request.has("method")) {
            response.set("error", createError(-32600, "Invalid Request: missing method"));
            return response;
        }
        
        String method = request.get("method").asText();
        
        switch (method) {
            case "initialize":
                response.set("result", handleInitialize());
                break;
            case "tools/list":
                response.set("result", handleToolsList());
                break;
            case "tools/call":
                response.set("result", handleToolCall(request.get("params")));
                break;
            default:
                response.set("error", createError(-32601, "Method not found: " + method));
        }
        
        return response;
    }
    
    private static JsonNode handleInitialize() {
        ObjectNode result = mapper.createObjectNode();
        result.put("protocolVersion", "2024-11-05");
        
        ObjectNode capabilities = mapper.createObjectNode();
        ObjectNode tools = mapper.createObjectNode();
        tools.set("listChanged", mapper.nullNode());
        capabilities.set("tools", tools);
        result.set("capabilities", capabilities);
        
        ObjectNode serverInfo = mapper.createObjectNode();
        serverInfo.put("name", "JavaCalc");
        serverInfo.put("version", "1.0.0");
        result.set("serverInfo", serverInfo);
        
        return result;
    }
    
    private static JsonNode handleToolsList() {
        ObjectNode result = mapper.createObjectNode();
        ArrayNode tools = mapper.createArrayNode();
        
        ObjectNode calculateTool = mapper.createObjectNode();
        calculateTool.put("name", "calculate");
        calculateTool.put("description", "Evaluates mathematical expressions using the Java calculator");
        
        ObjectNode inputSchema = mapper.createObjectNode();
        inputSchema.put("type", "object");
        
        ObjectNode properties = mapper.createObjectNode();
        ObjectNode expressionProp = mapper.createObjectNode();
        expressionProp.put("type", "string");
        expressionProp.put("description", "Mathematical expression to evaluate (e.g., '2 + 3 * 4', '(10 - 5) / 2')");
        properties.set("expression", expressionProp);
        inputSchema.set("properties", properties);
        
        ArrayNode required = mapper.createArrayNode();
        required.add("expression");
        inputSchema.set("required", required);
        
        calculateTool.set("inputSchema", inputSchema);
        tools.add(calculateTool);
        
        result.set("tools", tools);
        return result;
    }
    
    private static JsonNode handleToolCall(JsonNode params) {
        if (params == null || !params.has("name")) {
            ObjectNode error = mapper.createObjectNode();
            error.set("error", createError(-32602, "Invalid params: missing tool name"));
            return error;
        }
        
        String toolName = params.get("name").asText();
        
        if (!"calculate".equals(toolName)) {
            ObjectNode error = mapper.createObjectNode();
            error.set("error", createError(-32602, "Unknown tool: " + toolName));
            return error;
        }
        
        try {
            JsonNode arguments = params.get("arguments");
            if (arguments == null || !arguments.has("expression")) {
                throw new IllegalArgumentException("Missing expression argument");
            }
            
            String expression = arguments.get("expression").asText();
            String calculationResult = calculator.interpret(expression);
            
            ObjectNode result = mapper.createObjectNode();
            ArrayNode content = mapper.createArrayNode();
            ObjectNode textContent = mapper.createObjectNode();
            textContent.put("type", "text");
            textContent.put("text", calculationResult);
            content.add(textContent);
            result.set("content", content);
            result.put("isError", false);
            
            return result;
        } catch (Exception e) {
            ObjectNode result = mapper.createObjectNode();
            ArrayNode content = mapper.createArrayNode();
            ObjectNode textContent = mapper.createObjectNode();
            textContent.put("type", "text");
            textContent.put("text", "Error evaluating expression: " + e.getMessage());
            content.add(textContent);
            result.set("content", content);
            result.put("isError", true);
            
            return result;
        }
    }
    
    private static ObjectNode createError(int code, String message) {
        ObjectNode error = mapper.createObjectNode();
        error.put("code", code);
        error.put("message", message);
        return error;
    }
}
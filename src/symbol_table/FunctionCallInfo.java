package symbol_table;

/**
 * Represents a function call record for semantic analysis.
 * Stores information about each function call encountered during visitor walks.
 *
 * Used by:
 *   - InvalidFuncCallChecker: to verify the called function exists and is a function
 *   - WrongArgsCountChecker: to verify argument count matches parameter count
 *
 * Example (Python):
 *   greet("Ali", "Hi")
 *   → functionName = "greet"
 *   → argCount = 2
 *   → line = 25
 *   → source = "python"
 *   → isMethodCall = false
 *   → isJinjaFilter = false
 *
 * Example (Jinja):
 *   {{ name|upper }}
 *   → functionName = "upper"
 *   → argCount = 0
 *   → line = 10
 *   → source = "template"
 *   → isMethodCall = false
 *   → isJinjaFilter = true
 */
public class FunctionCallInfo {

    private String functionName;
    private int argCount;
    private int line;
    private String source;        // "python" or "template"
    private boolean isMethodCall;  // true if obj.method() call
    private boolean isJinjaFilter; // true if Jinja filter like {{ x|filter }}

    public FunctionCallInfo(String functionName, int argCount, int line,
                            String source, boolean isMethodCall, boolean isJinjaFilter) {
        this.functionName = functionName;
        this.argCount = argCount;
        this.line = line;
        this.source = source;
        this.isMethodCall = isMethodCall;
        this.isJinjaFilter = isJinjaFilter;
    }

    public String getFunctionName() {
        return functionName;
    }

    public int getArgCount() {
        return argCount;
    }

    public int getLine() {
        return line;
    }

    public String getSource() {
        return source;
    }

    public boolean isMethodCall() {
        return isMethodCall;
    }

    public boolean isJinjaFilter() {
        return isJinjaFilter;
    }

    @Override
    public String toString() {
        return "FunctionCallInfo{" +
                "functionName='" + functionName + '\'' +
                ", argCount=" + argCount +
                ", line=" + line +
                ", source='" + source + '\'' +
                ", isMethodCall=" + isMethodCall +
                ", isJinjaFilter=" + isJinjaFilter +
                '}';
    }
}
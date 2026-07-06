package symbol_table;

/**
 * Records the type of an argument passed to a specific built-in function,
 * so OperationTypeErrorChecker can verify the function accepts that type.
 *
 * Currently used for: len() -> argument must be string/list/dict/tuple/set.
 * Designed generically (functionName field) so it can be reused later for
 * other type-sensitive built-ins (abs, sum, sorted...) without redesign.
 *
 * Example:
 *   len(10)  -> functionName="len", argType="int" -> ERROR (no len())
 */
public class FunctionArgTypeInfo {

    private String functionName;
    private String argType;
    private String argDisplay;
    private int line;
    private String fileName = "";
    private String filePath = "";

    public FunctionArgTypeInfo(String functionName, String argType,
                               String argDisplay, int line) {
        this.functionName = functionName;
        this.argType = argType;
        this.argDisplay = argDisplay;
        this.line = line;
    }

    public String getFunctionName() { return functionName; }
    public String getArgType() { return argType; }
    public String getArgDisplay() { return argDisplay; }
    public int getLine() { return line; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
}
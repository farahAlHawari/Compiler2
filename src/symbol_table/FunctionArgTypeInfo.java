package symbol_table;


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
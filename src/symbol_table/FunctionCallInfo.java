package symbol_table;


public class FunctionCallInfo {

    private String functionName;
    private int argCount;
    private int line;
    private String source;
    private boolean isMethodCall;
    private boolean isJinjaFilter;
    private String fileName = "";
    private String filePath = "";
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
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
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
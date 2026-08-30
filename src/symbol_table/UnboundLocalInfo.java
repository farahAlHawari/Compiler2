package symbol_table;


public class UnboundLocalInfo {

    private String variableName;
    private String operator;
    private int line;
    private String fileName;
    private String filePath;
    private String scopeType;
    private String scopeContextName;
    private boolean isInsideFunction;
    private boolean variableInCurrentScope;
    private boolean variableInOuterScope;
    private boolean declaredGlobal;

    public UnboundLocalInfo(String variableName, String operator, int line,
                            String fileName, String filePath,
                            String scopeType, String scopeContextName,
                            boolean isInsideFunction,
                            boolean variableInCurrentScope,
                            boolean variableInOuterScope,
                            boolean declaredGlobal) {
        this.variableName = variableName;
        this.operator = operator;
        this.line = line;
        this.fileName = fileName;
        this.filePath = filePath;
        this.scopeType = scopeType;
        this.scopeContextName = scopeContextName;
        this.isInsideFunction = isInsideFunction;
        this.variableInCurrentScope = variableInCurrentScope;
        this.variableInOuterScope = variableInOuterScope;
        this.declaredGlobal = declaredGlobal;
    }


    public String getVariableName() { return variableName; }
    public String getOperator() { return operator; }
    public int getLine() { return line; }
    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public String getScopeType() { return scopeType; }
    public String getScopeContextName() { return scopeContextName; }
    public boolean isInsideFunction() { return isInsideFunction; }
    public boolean isVariableInCurrentScope() { return variableInCurrentScope; }
    public boolean isVariableInOuterScope() { return variableInOuterScope; }
    public boolean isDeclaredGlobal() { return declaredGlobal; }
}
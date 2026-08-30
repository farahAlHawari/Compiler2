package symbol_table;


public class UseBeforeInitInfo {

    private String variableName;
    private int usageLine;
    private String fileName;
    private String filePath;
    private String scopeType;
    private String scopeContextName;
    private boolean isInsideFunction;
    private boolean declaredLater;
    private boolean isConditionalAssignment;
    private boolean hasTypeAnnotationOnly;

    public UseBeforeInitInfo() {}

    public String getVariableName() { return variableName; }
    public void setVariableName(String variableName) { this.variableName = variableName; }

    public int getUsageLine() { return usageLine; }
    public void setUsageLine(int usageLine) { this.usageLine = usageLine; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getScopeType() { return scopeType; }
    public void setScopeType(String scopeType) { this.scopeType = scopeType; }

    public String getScopeContextName() { return scopeContextName; }
    public void setScopeContextName(String scopeContextName) { this.scopeContextName = scopeContextName; }

    public boolean isInsideFunction() { return isInsideFunction; }
    public void setInsideFunction(boolean insideFunction) { isInsideFunction = insideFunction; }

    public boolean isDeclaredLater() { return declaredLater; }
    public void setDeclaredLater(boolean declaredLater) { this.declaredLater = declaredLater; }

    public boolean isConditionalAssignment() { return isConditionalAssignment; }
    public void setConditionalAssignment(boolean conditionalAssignment) { isConditionalAssignment = conditionalAssignment; }

    public boolean hasTypeAnnotationOnly() { return hasTypeAnnotationOnly; }
    public void setTypeAnnotationOnly(boolean typeAnnotationOnly) { hasTypeAnnotationOnly = typeAnnotationOnly; }
}
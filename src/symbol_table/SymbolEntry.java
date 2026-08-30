package symbol_table;


public class SymbolEntry {

    private String name;
    private String kind;

    private String type;
    private String scopeType;
    private int scopeLevel;
    private int line;
    private String value;
    private String source;
    private String fileName = "";
    private String returnType;

    private int paramCount;

    private String declaredType = "";
    private String filePath = "";
    public SymbolEntry(String name, String kind, String type, String scopeType,
                       int scopeLevel, int line, String source) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.scopeType = scopeType;
        this.scopeLevel = scopeLevel;
        this.line = line;
        this.source = source;

        this.value = "";
        this.returnType = "";
        this.paramCount = -1;
        // NEW: default -1 (unknown)
        this.declaredType = null;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public int getScopeLevel() {
        return scopeLevel;
    }

    public void setScopeLevel(int scopeLevel) {
        this.scopeLevel = scopeLevel;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public int getParamCount() {
        return paramCount;
    }

    public void setParamCount(int paramCount) {
        this.paramCount = paramCount;
    }

    public String getDeclaredType() {
        return declaredType;
    }

    public void setDeclaredType(String declaredType) {
        this.declaredType = declaredType;
    }

    public String getQualifiedName() {
        return scopeType + "(" + scopeLevel + ")::" + name;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("| %-18s | %-12s | %-10s | %-12s | %-5d | %-5d | %-10s | %-15s |",
                name, kind, type, scopeType, scopeLevel, line, source,
                value.length() > 15 ? value.substring(0, 12) + "..." : value));
        return sb.toString();
    }
}

package symbol_table;

/**
 * Represents a single entry in the Symbol Table.
 * Each entry stores information about a symbol (variable, function, class, etc.)
 * including its name, type, scope level, and additional attributes.
 *
 * We use a flexible attribute map so that different kinds of symbols
 * (Python variables, CSS properties, Jinja variables, etc.) can store
 * their own specific information without needing separate classes.
 */
public class SymbolEntry {

    private String name;           // Symbol name (variable, function, class, etc.)
    private String kind;           // Kind: "variable", "function", "class", "parameter",
    //       "css_property", "css_selector", "jinja_var", "html_tag", "html_attribute"
    private String type;           // Data type: "int", "string", "bool", "list", "dict", "none", "unknown", etc.
    private String scopeType;      // Scope type: "global", "class", "function", "block", "style", "jinja_block"
    private int scopeLevel;        // Scope nesting depth (0=global, 1=first nested, etc.)
    private int line;              // Line number where the symbol was declared
    private String value;          // Initial/assigned value (as string representation)
    private String source;         // Source of the symbol: "python" or "template"
    private String fileName = "";
    private String returnType;     // For functions: declared return type from type hint (e.g., "int", "string")
    // Empty/null if no type hint declared
    private int paramCount;        // For functions: number of parameters in the definition
    // -1 if unknown
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
        this.returnType = "";     // NEW: default empty (no type hint)
        this.paramCount = -1;
        // NEW: default -1 (unknown)
        this.declaredType = null;
    }

    // ==================== Getters and Setters ====================

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
    /**
     * Returns a scope-qualified name like "global::x" or "function(index)::products"
     * This helps distinguish symbols with the same name in different scopes.
     */
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

package main.pythoncompiler.ast;

/**
 * AST node for Python function definitions.
 * MODIFIED: Added returnType and paramCount fields for semantic error checking.
 *
 * returnType: The declared return type from Python type hints (e.g., "-> int").
 *             Empty string if no type hint is declared.
 *             Used by ReturnTypeMismatchChecker.
 *
 * paramCount: The number of parameters the function accepts.
 *             Set by ASTBuilderVisitor during AST construction.
 *             Used by WrongArgsCountChecker.
 */
public class FunctionDefNode extends ASTNode {
    public String functionName;
    public String returnType;    // NEW: return type hint (e.g., "int", "string"), empty if none
    public int paramCount;       // NEW: number of parameters, 0 if none

    public FunctionDefNode(String functionName) {
        super("FunctionDef");
        this.functionName = functionName;
        this.returnType = "";     // default: no type hint
        this.paramCount = 0;      // default: no parameters
    }
/*
    @Override
    public String getDetails() {
        return " (Name: " + functionName + ")";
    }*/

@Override
public String getDetails() {
    String details = " (Name: " + functionName;
    if (!returnType.isEmpty()) {
        details += " -> " + returnType;
    }
    details += ")";
    return details;
}
}


package main.pythoncompiler.ast;

/**
 * AST node for function call expressions.
 * MODIFIED: Added argCount field for semantic error checking.
 *
 * argCount: The number of arguments passed in the function call.
 *           Set by ASTBuilderVisitor during AST construction.
 *           Used by WrongArgsCountChecker and InvalidFuncCallChecker.
 */
public class CallNode extends ASTNode {
    public String functionName;
    public int argCount;     // NEW: number of arguments in the call, 0 if none

    public CallNode(String functionName) {
        super("CallExpr");
        this.functionName = functionName;
        this.argCount = 0;      // default: no arguments
    }

    @Override
    public String getDetails() {
        return " (" + functionName + ", args=" + argCount + ")";
    }
}

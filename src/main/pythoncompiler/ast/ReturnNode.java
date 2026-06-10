package main.pythoncompiler.ast;

/**
 * AST node for return statements.
 * MODIFIED: Added returnExprType and enclosingFunctionName for semantic error checking.
 *
 * returnExprType: The inferred type of the return expression (e.g., "string", "int").
 *                 Set by SymbolTableVisitor during symbol table population.
 *                 Used by ReturnTypeMismatchChecker.
 *
 * enclosingFunctionName: The name of the function this return statement belongs to.
 *                        Set by SymbolTableVisitor when entering/exiting function scopes.
 *                        Used by ReturnTypeMismatchChecker to look up the declared return type.
 */
public class ReturnNode extends ASTNode {
    public String returnExprType;         // NEW: inferred type of return expression
    public String enclosingFunctionName;  // NEW: name of the enclosing function

    public ReturnNode() {
        super("ReturnStmt");
        this.returnExprType = "unknown";
        this.enclosingFunctionName = "";
    }

    @Override
    public String getDetails() {
        String details = "";
        if (!enclosingFunctionName.isEmpty()) {
            details += " (in " + enclosingFunctionName + ")";
        }
        if (!returnExprType.isEmpty() && !"unknown".equals(returnExprType)) {
            details += " [returns " + returnExprType + "]";
        }
        return details;
    }
}
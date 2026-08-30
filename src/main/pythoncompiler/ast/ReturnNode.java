package main.pythoncompiler.ast;


public class ReturnNode extends ASTNode {
    public String returnExprType;
    public String enclosingFunctionName;

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
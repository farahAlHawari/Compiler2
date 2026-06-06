package main.pythoncompiler.ast;


public class CallNode extends ASTNode {
    public String functionName;

    public CallNode(String functionName) {
        super("CallExpr");
        this.functionName = functionName;
    }

    @Override
    public String getDetails() {
        return " (" + functionName + ")";
    }
}

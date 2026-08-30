package main.pythoncompiler.ast;


public class CallNode extends ASTNode {
    public String functionName;
    public int argCount;

    public CallNode(String functionName) {
        super("CallExpr");
        this.functionName = functionName;
        this.argCount = 0;
    }

    @Override
    public String getDetails() {
        return " (" + functionName + ", args=" + argCount + ")";
    }
}

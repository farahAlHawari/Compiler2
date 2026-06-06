package main.pythoncompiler.ast;


public class FunctionDefNode extends ASTNode {
    public String functionName;

    public FunctionDefNode(String functionName) {
        super("FunctionDef");
        this.functionName = functionName;
    }

    @Override
    public String getDetails() {
        return " (Name: " + functionName + ")";
    }
}


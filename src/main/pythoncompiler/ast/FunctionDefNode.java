package main.pythoncompiler.ast;


public class FunctionDefNode extends ASTNode {
    public String functionName;
    public String returnType;
    public int paramCount;

    public FunctionDefNode(String functionName) {
        super("FunctionDef");
        this.functionName = functionName;
        this.returnType = "";
        this.paramCount = 0;
    }


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


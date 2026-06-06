package main.pythoncompiler.ast;

public class ForNode extends ASTNode {
    public String iteratorName;

    public ForNode() {
        super("ForLoop");
    }

    @Override
    public String getDetails() {
        return " (iterator: " + iteratorName + ")";
    }
}

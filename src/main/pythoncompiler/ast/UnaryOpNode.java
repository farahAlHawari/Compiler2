package main.pythoncompiler.ast;

public class UnaryOpNode extends ASTNode {
    public String operator;

    public UnaryOpNode(String operator) {
        super("UnaryOp");
        this.operator = operator;
    }

    @Override
    public String getDetails() {
        return " (" + operator + ")";
    }
}

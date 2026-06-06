package main.pythoncompiler.ast;


public class BinaryOpNode extends ASTNode {
    public String operator;

    public BinaryOpNode(String operator) {
        super("BinaryOp");
        this.operator = operator;
    }

    @Override
    public String getDetails() {
        return " (" + operator + ")";
    }
}

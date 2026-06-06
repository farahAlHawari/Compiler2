package main.pythoncompiler.ast;

public class LiteralNode extends ASTNode {
    public String value;
    public String type;

    public LiteralNode(String value, String type) {
        super("Literal");
        this.value = value;
        this.type = type;
    }

    @Override
    public String getDetails() {
        return " (" + type + ": " + value + ")";
    }
}

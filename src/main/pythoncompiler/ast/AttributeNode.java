package main.pythoncompiler.ast;

public class AttributeNode extends ASTNode {
    public String attributeName;

    public AttributeNode(String attributeName) {
        super("AttributeAccess");
        this.attributeName = attributeName;
    }

    @Override
    public String getDetails() {
        return " (." + attributeName + ")";
    }
}

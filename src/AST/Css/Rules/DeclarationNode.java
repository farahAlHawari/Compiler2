package AST.Css.Rules;
import AST.Core.ASTNode;

public class DeclarationNode extends ASTNode {

    private String property;
    private String value;

    public DeclarationNode(String property, String value, int line) {
        super("Declaration " + property + ": " + value, line);
        this.property = property;
        this.value = value != null ? value : "";
    }

    public String getProperty() {
        return property;
    }

    public String getValue() {
        return value;
    }
}
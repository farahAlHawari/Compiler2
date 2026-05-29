package AST.Html;

import AST.Core.ASTNode;

public class HtmlAttributeNode extends ASTNode {

    private String attrName;
    private String attrValue;

    public HtmlAttributeNode(String name, String value, int line) {
        super("Html Attribute " + name + "=" + value, line);
        this.attrName = name;
        this.attrValue = value != null ? value : "";
    }

    public String getAttrName() {
        return attrName;
    }

    public String getAttrValue() {
        return attrValue;
    }
}
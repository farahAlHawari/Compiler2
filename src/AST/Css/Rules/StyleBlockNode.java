
package AST.Css.Rules;

import AST.Core.ASTNode;

public class StyleBlockNode extends ASTNode {

    private int closingLine;

    public StyleBlockNode(int openingLine) {
        super("StyleBlock", openingLine);
        this.closingLine = openingLine;
    }

    public void setClosingLine(int closingLine) {
        this.closingLine = closingLine;
    }

    @Override
    public void print(String indent) {

        System.out.println(
                indent + "└──StyleBlock - Opening <style> (line number " + line + ")"
        );

        for (ASTNode child : children) {
            child.print(indent + "  ");
        }

        System.out.println(
                indent + "└──StyleBlock - Closing </style> (line number " + closingLine + ")"
        );
    }
}
package main.pythoncompiler.ast;

import java.util.ArrayList;
import java.util.List;
public abstract class ASTNode {
    public int lineNumber;
    public String nodeName;
    public List<ASTNode> children = new ArrayList<>();

    public ASTNode(String nodeName) {
        this.nodeName = nodeName;
        this.lineNumber = 0;
    }

    public ASTNode(String nodeName, int lineNumber) {
        this.nodeName = nodeName;
        this.lineNumber = lineNumber;
    }

    public void addChild(ASTNode node) {
        if (node != null) {
            this.children.add(node);
        }
    }

    public String getDetails() {
        return "";
    }

    public String toStringTree(String indent) {
        String result = indent + "└── " + nodeName;

        if (lineNumber > 0) {
            result += " (line " + lineNumber + ")";
        }

        String details = getDetails();
        if (details != null && !details.isEmpty()) {
            result += details;
        }

        result += "\n";

        for (ASTNode child : children) {
            result += child.toStringTree(indent + "   ");
        }
        return result;
    }
    @Override
    public String toString() {
        return toStringTree("");
    }
}


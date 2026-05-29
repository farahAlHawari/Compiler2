//package AST.Html;
//import AST.Core.ASTNode;
//
//public class HtmlElementNode extends ASTNode {
//
//    private String tagName;
//    private int closingLine;
//
//    public HtmlElementNode(String tagName, int openingLine) {
//        super("HtmlElement", openingLine);
//        this.tagName = tagName;
//        this.closingLine = openingLine;
//    }
//
//    public void setClosingLine(int closingLine) {
//        this.closingLine = closingLine;
//    }
//
//    @Override
//    public void print(String indent) {
//
//        if (isVoidTag(tagName)) {
//            System.out.println(
//                    indent + "└──HtmlElement - Self-Closing <" + tagName + "/> (line number " + line + ")"
//            );
//            for (ASTNode child : children) {
//                if (child instanceof HtmlAttributeNode) {
//                    child.print(indent + "  ");
//                }
//            }
//            return;
//        }
//
//        System.out.println(
//                indent + "└──HtmlElement - Opening <" + tagName + "> (line number " + line + ")"
//        );
//
//        for (ASTNode child : children) {
//            child.print(indent + "  ");
//        }
//
//        System.out.println(
//                indent + "└──HtmlElement - Closing </" + tagName + "> (line number " + closingLine + ")"
//        );
//    }
//
//    private boolean isVoidTag(String tag) {
//        return tag.equals("area") || tag.equals("base")  || tag.equals("br")||
//        tag.equals("col") || tag.equals("embed") || tag.equals("hr")||
//        tag.equals("img")  ||tag.equals("input") || tag.equals("link")||
//        tag.equals("meta") || tag.equals("param") || tag.equals("source")||
//        tag.equals("track") || tag.equals("wbr");
//    }
//}

package AST.Html;
import AST.Core.ASTNode;

public class HtmlElementNode extends ASTNode {

    private String tagName;
    private int closingLine;

    public HtmlElementNode(String tagName, int openingLine) {
        super("HtmlElement", openingLine);
        this.tagName = tagName;
        this.closingLine = openingLine;
    }

    public String getTagName() {
        return tagName;
    }

    public void setClosingLine(int closingLine) {
        this.closingLine = closingLine;
    }

    @Override
    public void print(String indent) {

        if (isVoidTag(tagName)) {
            System.out.println(
                    indent + "└──HtmlElement - Self-Closing <" + tagName + "/> (line number " + line + ")"
            );
            for (ASTNode child : children) {
                child.print(indent + "  ");
            }
            return;
        }

        System.out.println(
                indent + "└──HtmlElement - Opening <" + tagName + "> (line number " + line + ")"
        );

        for (ASTNode child : children) {
            child.print(indent + "  ");
        }

        System.out.println(
                indent + "└──HtmlElement - Closing </" + tagName + "> (line number " + closingLine + ")"
        );
    }

    private boolean isVoidTag(String tag) {
        return tag.equals("area") || tag.equals("base") || tag.equals("br")
                || tag.equals("col") || tag.equals("embed") || tag.equals("hr")
                || tag.equals("img") || tag.equals("input") || tag.equals("link")
                || tag.equals("meta") || tag.equals("param") || tag.equals("source")
                || tag.equals("track") || tag.equals("wbr");
    }
}

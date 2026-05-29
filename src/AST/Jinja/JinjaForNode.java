package AST.Jinja;
public class JinjaForNode extends JinjaNode {

    private String forExpr;

    public JinjaForNode(String expr, int line) {
        super("JinjaFor (" + expr + ")", line);
        this.forExpr = expr;
    }

    public String getForExpr() {
        return forExpr;
    }
}
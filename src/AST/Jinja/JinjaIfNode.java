//package AST.Jinja;
//public class JinjaIfNode extends JinjaNode {
//
//    public JinjaIfNode(String condition, int line) {
//        super("JinjaIf (" + condition + ")", line);
//    }
//}

package AST.Jinja;
public class JinjaIfNode extends JinjaNode {

    private String condition;

    public JinjaIfNode(String condition, int line) {
        super("JinjaIf (" + condition + ")", line);
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }
}
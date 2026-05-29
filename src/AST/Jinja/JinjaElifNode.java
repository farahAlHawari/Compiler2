//package AST.Jinja;
//public class JinjaElifNode extends JinjaNode {
//
//    public JinjaElifNode(String condition, int line) {
//        super("JinjaElif (" + condition + ")", line);
//    }
//}
package AST.Jinja;
public class JinjaElifNode extends JinjaNode {

    private String condition;

    public JinjaElifNode(String condition, int line) {
        super("JinjaElif (" + condition + ")", line);
        this.condition = condition;
    }

    public String getCondition() {
        return condition;
    }
}
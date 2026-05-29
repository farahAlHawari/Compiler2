package AST.Jinja;

public class JinjaIncludeNode extends JinjaNode {

    public JinjaIncludeNode(String template, int line) {
        super("JinjaInclude " + template, line);
    }
}

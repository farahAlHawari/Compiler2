package AST.Jinja;
public class JinjaExtendsNode extends JinjaNode {

    public JinjaExtendsNode(String template, int line) {
        super("JinjaExtends " + template, line);
    }
}

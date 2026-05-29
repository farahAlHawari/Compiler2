package AST.Jinja;
public class JinjaBlockNode extends JinjaNode {

    private String blockName;

    public JinjaBlockNode(String name, int line) {
        super("JinjaBlock " + name, line);
        this.blockName = name;
    }

    public String getBlockName() {
        return blockName;
    }
}
package AST.Jinja;
public class JinjaMacroNode extends JinjaNode {

    private String macroName;

    public JinjaMacroNode(String name, int line) {
        super("JinjaMacro " + name, line);
        this.macroName = name;
    }

    public String getMacroName() {
        return macroName;
    }
}
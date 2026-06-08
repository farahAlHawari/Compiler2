//package AST.Jinja;
//public class JinjaMacroNode extends JinjaNode {
//
//    private String macroName;
//
//    public JinjaMacroNode(String name, int line) {
//        super("JinjaMacro " + name, line);
//        this.macroName = name;
//    }
//
//    public String getMacroName() {
//        return macroName;
//    }
//}
package AST.Jinja;

import java.util.ArrayList;
import java.util.List;

public class JinjaMacroNode extends JinjaNode {

    private String macroName;
    private List<String> parameters = new ArrayList<>();

    public JinjaMacroNode(String name, int line) {
        super("JinjaMacro " + name, line);
        this.macroName = name;
    }

    public String getMacroName() {
        return macroName;
    }

    public void parseParameters(String fullMacroText) {
        parameters.clear();
        if (fullMacroText == null || fullMacroText.isEmpty()) return;

        int parenStart = fullMacroText.indexOf("(");
        int parenEnd = fullMacroText.lastIndexOf(")");

        if (parenStart != -1 && parenEnd != -1 && parenEnd > parenStart) {
            String paramsStr = fullMacroText.substring(parenStart + 1, parenEnd).trim();
            if (paramsStr.isEmpty()) return;

            String[] params = paramsStr.split(",");
            for (String param : params) {
                String clean = param.trim();
                if (clean.contains(":")) clean = clean.substring(0, clean.indexOf(":")).trim();
                if (clean.contains("=")) clean = clean.substring(0, clean.indexOf("=")).trim();
                if (!clean.isEmpty()) parameters.add(clean);
            }
        }
    }

    public List<String> getParameters() {
        return parameters;
    }
}
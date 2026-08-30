package symbol_table;

import java.util.*;


public class FlaskTemplateCall {

    private String templateName;
    private List<String> passedVariables;
    private Map<String, Set<String>> passedVariableTypes = new HashMap<>();
    private Map<String, String> passedVariableValues = new HashMap<>();

    private int line;
    private String filePath = "";
    public FlaskTemplateCall(String templateName, int line) {
        this.templateName = templateName;
        this.line = line;
        this.passedVariables = new ArrayList<>();
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public List<String> getPassedVariables() {
        return passedVariables;
    }

    public void addPassedVariable(String varName) {
        if (!passedVariables.contains(varName)) {
            passedVariables.add(varName);
        }
    }
    public void addPassedVariableValue(String varName, String value) {
        passedVariableValues.put(varName, value);
    }
    public String getPassedVariableValue(String varName) {
        return passedVariableValues.getOrDefault(varName, null);
    }

    public void addPassedVariableType(String varName, String type) {
        passedVariableTypes
                .computeIfAbsent(varName, k -> new HashSet<>())
                .add(type);
    }


    public Set<String> getPassedVariableTypes(String varName) {
        return passedVariableTypes.getOrDefault(varName, Collections.emptySet());
    }


    public boolean hasPassedVariableType(String varName) {
        return passedVariableTypes.containsKey(varName);
    }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    @Override
    public String toString() {
        return "FlaskTemplateCall{" +
                "templateName='" + templateName + '\'' +
                ", passedVariables=" + passedVariables +
                ", line=" + line +
                '}';
    }
}
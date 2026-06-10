package symbol_table;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a render_template() call from Flask (Python) code.
 * Stores which template is being rendered and which variables are passed to it.
 *
 * Example:
 *   render_template("page.html", name=name, age=age)
 *   → templateName = "page.html"
 *   → passedVariables = ["name", "age"]
 *   → line = 15
 *
 * This data is used by MissingFlaskVarChecker to verify that
 * all Jinja template variables are properly passed from Flask.
 */
public class FlaskTemplateCall {

    private String templateName;
    private List<String> passedVariables;
    private int line;

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
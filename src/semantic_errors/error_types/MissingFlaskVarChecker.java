package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.FlaskTemplateCall;
import symbol_table.SourceFileReader;
import symbol_table.SymbolTable;
import symbol_table.SymbolEntry;

import java.util.*;

/**
 * Checker for MISSING_FLASK_VARIABLE error.
 *
 * Definition: A variable used in Jinja that was not passed from Flask through render_template.
 *
 * Cases:
 *   1. Variable in Jinja not passed from Flask:
 *      Flask: render_template("p.html")  |  Jinja: {{ name }}  → ERROR
 *   2. Variable passed with value None and used in operation:
 *      Flask: render_template("p.html", x=None)  |  Jinja: {{ x + 5 }}  → ERROR
 *   3. Loop variable not passed:
 *      Flask: render_template("p.html")  |  Jinja: {% for user in users %}  → ERROR (users not passed)
 *
 * Algorithm:
 *   1. Collect all variables passed via render_template() calls from Flask code
 *   2. Collect all "context variables" from Jinja templates (variables that templates expect from Flask)
 *   3. For each Jinja context variable, check if it was passed via render_template
 *   4. If not → MISSING_FLASK_VARIABLE error
 */
public class MissingFlaskVarChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;

    // Jinja built-in variables that are always available and should NOT be flagged
    private static final Set<String> JINJA_BUILTINS = Set.of(
            "loop", "request", "session", "g", "config", "self",
            "range", "dict", "lipsum", "cycler", "joiner", "namespace",
            "true", "false", "none", "True", "False", "None"
    );

    // Jinja built-in filters that should NOT be flagged as missing variables
    private static final Set<String> JINJA_BUILTIN_FILTERS = Set.of(
            "upper", "lower", "title", "trim", "default", "safe",
            "join", "first", "last", "count", "sort", "reverse",
            "length", "string", "int", "float", "list", "bool"
    );

    public MissingFlaskVarChecker(SymbolTable symbolTable, List<SemanticError> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    public void check() {
        // Step 1: Collect all variables passed via render_template() calls
        Set<String> flaskPassedVars = new HashSet<>();
        for (FlaskTemplateCall call : symbolTable.getRenderTemplateCalls()) {
            flaskPassedVars.addAll(call.getPassedVariables());
        }

        // Step 2: Collect all Jinja context variables that need to come from Flask
        // These are variables referenced in Jinja templates that are not locally defined
        Set<String> checkedVars = new HashSet<>(); // avoid duplicate errors for same var

        for (SymbolEntry entry : symbolTable.getAllEntries()) {
            if (!"template".equals(entry.getSource())) continue;

            String varName = entry.getName();
            String kind = entry.getKind();
            String type = entry.getType();

            // Only check variables that are expected to come from Flask context:
            // - kind="jinja_var" type="context_var" : variables used in {{ expr }} that aren't locally defined
            // - kind="variable" type="jinja_iterable" : iterable variables in {% for x in y %}
            boolean isContextVar = "jinja_var".equals(kind) && "context_var".equals(type);
            boolean isIterableVar = "variable".equals(kind) && "jinja_iterable".equals(type);

            if (!isContextVar && !isIterableVar) continue;

            // Skip builtins
            if (JINJA_BUILTINS.contains(varName)) continue;
            if (JINJA_BUILTIN_FILTERS.contains(varName)) continue;
            if (isLocallyDefinedInJinja(varName)) continue;
            // Skip if already checked (deduplicate by name)
            if (checkedVars.contains(varName)) continue;
            checkedVars.add(varName);

            // Step 3: Check if this variable was passed from Flask
            if (!flaskPassedVars.contains(varName)) {
//                errors.add(new SemanticError(
//                        SemanticErrorType.MISSING_FLASK_VARIABLE,
//                        "MISSING_FLASK_VARIABLE",
//                        "Variable '" + varName + "' is used in Jinja but not passed from Flask via render_template",
//                        entry.getLine()
//                ));
                errors.add(new SemanticError(
                        SemanticErrorType.MISSING_FLASK_VARIABLE,
                        "NameError",
                        "Variable '" + varName + "' is used in Jinja but not passed from Flask via render_template",
                        entry.getLine(),
                        entry.getFileName(),
                        varName,
                        "{{ " + varName + " }}",
                        SourceFileReader.getLine(entry.getFilePath(), entry.getLine())   // ← مو symbolTable
                ));
            }
        }
    }
    /**
     * ✅ يتحقق إذا كان المتغير معرّف محلياً في Jinja عبر {% set %} أو {% with %}
     * (بدل ما نعتبره missing من Flask)
     */
    private boolean isLocallyDefinedInJinja(String varName) {
        for (SymbolEntry entry : symbolTable.getAllEntries()) {
            if (!"template".equals(entry.getSource())) continue;
            if (varName.equals(entry.getName())
                    && "jinja_set_var".equals(entry.getType())) {
                return true;
            }
        }
        return false;
    }
}

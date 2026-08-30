
package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.FlaskTemplateCall;
import symbol_table.SourceFileReader;
import symbol_table.SymbolTable;
import symbol_table.SymbolEntry;

import java.util.*;


public class MissingFlaskVarChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;


    private static final Set<String> JINJA_BUILTINS = Set.of(
            "loop", "request", "session", "g", "config", "self",
            "range", "dict", "lipsum", "cycler", "joiner", "namespace",
            "true", "false", "none", "True", "False", "None"
    );

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

        Set<String> flaskPassedVars = new HashSet<>();
        for (FlaskTemplateCall call : symbolTable.getRenderTemplateCalls()) {
            flaskPassedVars.addAll(call.getPassedVariables());
        }


        Set<String> checkedVars = new HashSet<>();

        for (SymbolEntry entry : symbolTable.getAllEntries()) {
            if (!"template".equals(entry.getSource())) continue;

            String varName = entry.getName();
            String kind = entry.getKind();
            String type = entry.getType();


            boolean isContextVar = "jinja_var".equals(kind) && "context_var".equals(type);
            boolean isIterableVar = "variable".equals(kind) && "jinja_iterable".equals(type);

            if (!isContextVar && !isIterableVar) continue;


            if (JINJA_BUILTINS.contains(varName)) continue;
            if (JINJA_BUILTIN_FILTERS.contains(varName)) continue;
            if (isLocallyDefinedInJinja(varName)) continue;

            if (checkedVars.contains(varName)) continue;
            checkedVars.add(varName);


            if (!flaskPassedVars.contains(varName)) {
                errors.add(new SemanticError(
                        SemanticErrorType.MISSING_FLASK_VARIABLE,
                        "UndefinedError(missing flask variable)" ,
                        "Variable '" + varName + "' is used in Jinja but not passed from Flask via render_template",
                        entry.getLine(),
                        entry.getFileName(),
                        varName,
                        "{{ " + varName + " }}",
                        SourceFileReader.getLine(entry.getFilePath(), entry.getLine())
                ));
            }
        }
    }


    private boolean isLocallyDefinedInJinja(String varName) {
        for (SymbolEntry entry : symbolTable.getAllEntries()) {
            if (!"template".equals(entry.getSource())) continue;
            if (varName.equals(entry.getName())) {
                String type = entry.getType();

                if ("jinja_set_var".equals(type)) return true;

                if ("jinja_iterator".equals(type)) return true;
            }
        }
        return false;
    }
}

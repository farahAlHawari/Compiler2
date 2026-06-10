package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.FunctionCallInfo;
import symbol_table.SymbolTable;
import symbol_table.SymbolEntry;

import java.util.*;

/**
 * Checker for INVALID_FUNC_CALL error.
 *
 * Definition: Calling a function that doesn't exist, or calling a variable as a function.
 *
 * Cases:
 *   1. Calling undefined function: foo() where foo is not defined anywhere
 *   2. Calling variable as function: x = 5; then x() — x is not a function
 *   3. Jinja: non-existent filter: {{ name|unknown_filter }}
 *
 * Algorithm:
 *   1. Get all function call records from the symbol table
 *   2. For each call, look up the function name in the symbol table
 *   3. If not found → INVALID_FUNC_CALL (undefined function)
 *   4. If found but kind is not "function" or "route_function" → INVALID_FUNC_CALL (not a function)
 *   5. Skip built-in functions and Flask framework functions
 */
public class InvalidFuncCallChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;

    // Python built-in functions that should NOT be flagged
    private static final Set<String> PYTHON_BUILTINS = Set.of(
            "print", "len", "range", "int", "str", "float", "list", "dict",
            "set", "tuple", "type", "isinstance", "input", "open", "append",
            "super", "staticmethod", "classmethod", "property", "enumerate",
            "zip", "map", "filter", "sorted", "reversed", "min", "max", "sum",
            "abs", "round", "any", "all", "hasattr", "getattr", "setattr",
            "__name__"
    );

    // Flask framework functions that should NOT be flagged
    private static final Set<String> FLASK_FRAMEWORK = Set.of(
            "Flask", "render_template", "request", "redirect", "url_for",
            "jsonify", "SQLAlchemy", "db", "session", "g", "current_app",
            "flash", "get_flashed_messages", "abort", "make_response",
            "send_file", "send_from_directory", "safe_join",
            "app", "run"
    );

    // Jinja built-in filters that should NOT be flagged
    private static final Set<String> JINJA_BUILTIN_FILTERS = Set.of(
            "upper", "lower", "title", "trim", "default", "safe",
            "join", "first", "last", "count", "sort", "reverse",
            "length", "string", "int", "float", "list", "bool",
            "round", "batch", "center", "e", "escape", "filesizeformat",
            "format", "indent", "replace", "truncate", "striptags",
            "wordcount", "capitalize", "xmlattr", "urlencode"
    );

    public InvalidFuncCallChecker(SymbolTable symbolTable, List<SemanticError> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    public void check() {
        for (FunctionCallInfo callInfo : symbolTable.getFunctionCallInfos()) {
            String funcName = callInfo.getFunctionName();
            int line = callInfo.getLine();
            String source = callInfo.getSource();
            boolean isMethodCall = callInfo.isMethodCall();
            boolean isJinjaFilter = callInfo.isJinjaFilter();

            // Skip method calls (obj.method()) — we can't validate all object methods
            if (isMethodCall) continue;

            // Case 3: Jinja filter check
            if (isJinjaFilter) {
                if (JINJA_BUILTIN_FILTERS.contains(funcName)) continue;
                // Check if it's a user-defined Jinja macro
                SymbolEntry macroEntry = symbolTable.lookup(funcName);
                if (macroEntry == null || !"jinja_macro".equals(macroEntry.getType())) {
                    errors.add(new SemanticError(
                            SemanticErrorType.INVALID_FUNC_CALL,
                            "INVALID_FUNC_CALL",
                            "Jinja filter '" + funcName + "' does not exist",
                            line
                    ));
                }
                continue;
            }

            // Skip Python built-in functions
            if ("python".equals(source) && PYTHON_BUILTINS.contains(funcName)) continue;

            // Skip Flask framework functions
            if ("python".equals(source) && FLASK_FRAMEWORK.contains(funcName)) continue;

            // Look up the function name in the symbol table
            SymbolEntry funcEntry = symbolTable.lookup(funcName);

            // Case 1: Function not defined at all
            if (funcEntry == null) {
                errors.add(new SemanticError(
                        SemanticErrorType.INVALID_FUNC_CALL,
                        "INVALID_FUNC_CALL",
                        "Function '" + funcName + "' is called but not defined",
                        line
                ));
                continue;
            }

            // Case 2: Symbol exists but is not a function
            String kind = funcEntry.getKind();
            if (!"function".equals(kind) && !"route_function".equals(kind)
                    && !"jinja_macro".equals(kind) && !"imported_name".equals(kind)) {
                errors.add(new SemanticError(
                        SemanticErrorType.INVALID_FUNC_CALL,
                        "INVALID_FUNC_CALL",
                        "Identifier '" + funcName + "' is used as a function but is declared as " + kind,
                        line
                ));
            }
        }
    }
}

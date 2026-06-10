package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.FunctionCallInfo;
import symbol_table.SymbolTable;
import symbol_table.SymbolEntry;

import java.util.*;

/**
 * Checker for WRONG_ARGS_COUNT error.
 *
 * Definition: Calling a function with a different number of arguments from the definition.
 *
 * Cases:
 *   def greet(name, greeting):    # 2 parameters
 *   greet("Ali")                   Wrong Args — 1 missing
 *   greet("Ali", "Hi", "Bye")      Wrong Args — 1 extra
 *   greet("Ali", "Hi")             correct
 *
 * Algorithm:
 *   1. Get all function call records from the symbol table
 *   2. For each call, look up the function definition in the symbol table
 *   3. Get the expected parameter count from the function's SymbolEntry
 *   4. Compare with the actual argument count from the call
 *   5. If they differ → WRONG_ARGS_COUNT error
 *   6. Skip built-in functions, method calls, and functions with unknown param counts
 */
public class WrongArgsCountChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;

    // Python built-in functions — skip argument count checking
    private static final Set<String> PYTHON_BUILTINS = Set.of(
            "print", "len", "range", "int", "str", "float", "list", "dict",
            "set", "tuple", "type", "isinstance", "input", "open", "append",
            "super", "staticmethod", "classmethod", "property", "enumerate",
            "zip", "map", "filter", "sorted", "reversed", "min", "max", "sum",
            "abs", "round", "any", "all", "hasattr", "getattr", "setattr"
    );

    // Flask framework functions — skip argument count checking
    private static final Set<String> FLASK_FRAMEWORK = Set.of(
            "Flask", "render_template", "request", "redirect", "url_for",
            "jsonify", "session", "g", "current_app", "flash",
            "get_flashed_messages", "abort", "make_response", "run"
    );

    public WrongArgsCountChecker(SymbolTable symbolTable, List<SemanticError> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    public void check() {
        for (FunctionCallInfo callInfo : symbolTable.getFunctionCallInfos()) {
            String funcName = callInfo.getFunctionName();
            int actualArgCount = callInfo.getArgCount();
            int line = callInfo.getLine();
            String source = callInfo.getSource();
            boolean isMethodCall = callInfo.isMethodCall();
            boolean isJinjaFilter = callInfo.isJinjaFilter();

            // Skip method calls (obj.method()) — can't validate all methods
            if (isMethodCall) continue;

            // Skip Jinja filters (handled separately if needed)
            if (isJinjaFilter) {
                checkJinjaFilterArgs(funcName, actualArgCount, line);
                continue;
            }

            // Skip Python built-in and Flask framework functions
            if ("python".equals(source)) {
                if (PYTHON_BUILTINS.contains(funcName)) continue;
                if (FLASK_FRAMEWORK.contains(funcName)) continue;
            }

            // Look up the function definition
            SymbolEntry funcEntry = symbolTable.lookup(funcName);
            if (funcEntry == null) continue; // Already caught by InvalidFuncCallChecker

            // Only check functions (not variables used as functions)
            String kind = funcEntry.getKind();
            if (!"function".equals(kind) && !"route_function".equals(kind)
                    && !"jinja_macro".equals(kind)) {
                continue;
            }

            // Get expected parameter count
            int expectedParamCount = funcEntry.getParamCount();
            if (expectedParamCount < 0) continue; // Unknown param count, skip

            // Compare
            if (actualArgCount != expectedParamCount) {
                String detail;
                if (actualArgCount < expectedParamCount) {
                    detail = (expectedParamCount - actualArgCount) + " argument(s) missing";
                } else {
                    detail = (actualArgCount - expectedParamCount) + " extra argument(s)";
                }

                errors.add(new SemanticError(
                        SemanticErrorType.WRONG_ARGS_COUNT,
                        "WRONG_ARGS_COUNT",
                        "Function '" + funcName + "' expects " + expectedParamCount
                                + " argument(s) but called with " + actualArgCount
                                + " (" + detail + ")",
                        line
                ));
            }
        }
    }

    /**
     * Check Jinja macro calls against macro parameter counts.
     */
    private void checkJinjaFilterArgs(String filterName, int argCount, int line) {
        // Check if it's a user-defined Jinja macro
        SymbolEntry macroEntry = symbolTable.lookup(filterName);
        if (macroEntry != null && "jinja_macro".equals(macroEntry.getType())) {
            int expectedParamCount = macroEntry.getParamCount();
            if (expectedParamCount >= 0 && argCount != expectedParamCount) {
                errors.add(new SemanticError(
                        SemanticErrorType.WRONG_ARGS_COUNT,
                        "WRONG_ARGS_COUNT",
                        "Jinja macro '" + filterName + "' expects " + expectedParamCount
                                + " argument(s) but called with " + argCount,
                        line
                ));
            }
        }
        // Built-in filters have variable argument counts, so we skip them
    }
}

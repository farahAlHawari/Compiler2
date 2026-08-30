package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.FunctionCallInfo;
import symbol_table.SourceFileReader;
import symbol_table.SymbolTable;
import symbol_table.SymbolEntry;

import java.util.*;


public class InvalidFuncCallChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;


    private static final Set<String> PYTHON_BUILTINS = Set.of(
            "print", "len", "range", "int", "str", "float", "list", "dict",
            "set", "tuple", "type", "isinstance", "input", "open", "append",
            "super", "staticmethod", "classmethod", "property", "enumerate",
            "zip", "map", "filter", "sorted", "reversed", "min", "max", "sum",
            "abs", "round", "any", "all", "hasattr", "getattr", "setattr",
            "__name__"
    );


    private static final Set<String> FLASK_FRAMEWORK = Set.of(
            "Flask", "render_template", "request", "redirect", "url_for",
            "jsonify", "SQLAlchemy", "db", "session", "g", "current_app",
            "flash", "get_flashed_messages", "abort", "make_response",
            "send_file", "send_from_directory", "safe_join",
            "app", "run"
    );


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

            if (isMethodCall) continue;


            if (isJinjaFilter) {
                if (JINJA_BUILTIN_FILTERS.contains(funcName)) continue;
                // Check if it's a user-defined Jinja macro
                SymbolEntry macroEntry = symbolTable.lookup(funcName);
                if (macroEntry == null || !"jinja_macro".equals(macroEntry.getType())) {

                    errors.add(new SemanticError(
                            SemanticErrorType.INVALID_FUNC_CALL,
                            "NameError(Invalid Function Call)",
                            "Jinja filter '" + funcName + "' does not exist",
                            line,
                            callInfo.getFileName(),
                            "",
                            "{{ ...|" + funcName + " }}",
                            SourceFileReader.getLine(callInfo.getFilePath(), line)
                    ));
                }
                continue;
            }


            if ("python".equals(source) && PYTHON_BUILTINS.contains(funcName)) continue;


            if ("python".equals(source) && FLASK_FRAMEWORK.contains(funcName)) continue;


            SymbolEntry funcEntry = symbolTable.lookup(funcName);


            if (funcEntry == null) {

                errors.add(new SemanticError(
                        SemanticErrorType.INVALID_FUNC_CALL,
                        "NameError(Invalid Function Call)",
                        "Function '" + funcName + "' is called but not defined",
                        line,
                        callInfo.getFileName(),
                        funcName,
                        funcName + "()",
                        SourceFileReader.getLine(callInfo.getFilePath(), line)
                ));
                continue;
            }


            String kind = funcEntry.getKind();
            if (!"function".equals(kind) && !"route_function".equals(kind)
                    && !"jinja_macro".equals(kind) && !"imported_name".equals(kind)) {

                errors.add(new SemanticError(
                        SemanticErrorType.INVALID_FUNC_CALL,
                        "TypeError(Invalid Function Call)",
                        "Identifier '" + funcName + "' is used as a function but is declared as " + kind,
                        line,
                        callInfo.getFileName(),
                        funcName,
                        funcName + "()",
                        SourceFileReader.getLine(callInfo.getFilePath(), line)
                ));
            }
        }
    }
}

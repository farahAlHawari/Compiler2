package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.FunctionCallInfo;
import symbol_table.SourceFileReader;
import symbol_table.SymbolTable;
import symbol_table.SymbolEntry;

import java.util.*;


public class WrongArgsCountChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;


    private static final Set<String> PYTHON_BUILTINS = Set.of(
            "print", "len", "range", "int", "str", "float", "list", "dict",
            "set", "tuple", "type", "isinstance", "input", "open", "append",
            "super", "staticmethod", "classmethod", "property", "enumerate",
            "zip", "map", "filter", "sorted", "reversed", "min", "max", "sum",
            "abs", "round", "any", "all", "hasattr", "getattr", "setattr"
    );


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


            if (isMethodCall) continue;


            if (isJinjaFilter) {
                checkJinjaFilterArgs(callInfo);
                continue;
            }


            if ("python".equals(source)) {
                if (PYTHON_BUILTINS.contains(funcName)) continue;
                if (FLASK_FRAMEWORK.contains(funcName)) continue;
            }


            SymbolEntry funcEntry = symbolTable.lookup(funcName);
            if (funcEntry == null) continue;


            String kind = funcEntry.getKind();
            if (!"function".equals(kind) && !"route_function".equals(kind)
                    && !"jinja_macro".equals(kind)) {
                continue;
            }


            int expectedParamCount = funcEntry.getParamCount();
            if (expectedParamCount < 0) continue;


            if (actualArgCount != expectedParamCount) {
                String detail;
                if (actualArgCount < expectedParamCount) {
                    detail = (expectedParamCount - actualArgCount) + " argument(s) missing";
                } else {
                    detail = (actualArgCount - expectedParamCount) + " extra argument(s)";
                }

                errors.add(new SemanticError(
                        SemanticErrorType.WRONG_ARGS_COUNT,
                        "TypeError",
                        "Function '" + funcName + "' expects " + expectedParamCount
                                + " argument(s) but called with " + actualArgCount
                                + " (" + detail + ")",
                        line,
                        callInfo.getFileName(),
                        funcName,
                        funcName + "(" + "...".repeat(actualArgCount > 0 ? 1 : 0) + ")",
                        SourceFileReader.getLine(callInfo.getFilePath(), line)
                ));
            }
        }
    }


    private void checkJinjaFilterArgs(FunctionCallInfo callInfo) {
        String filterName = callInfo.getFunctionName();
        int argCount = callInfo.getArgCount();
        int line = callInfo.getLine();

        SymbolEntry macroEntry = symbolTable.lookup(filterName);
        if (macroEntry != null && "jinja_macro".equals(macroEntry.getType())) {
            int expectedParamCount = macroEntry.getParamCount();
            if (expectedParamCount >= 0 && argCount != expectedParamCount) {
                errors.add(new SemanticError(
                        SemanticErrorType.WRONG_ARGS_COUNT,
                        "TypeError",
                        "Jinja macro '" + filterName + "' expects " + expectedParamCount
                                + " argument(s) but called with " + argCount,
                        line,
                        callInfo.getFileName(),
                        filterName,
                        "{{ ...|" + filterName + " }}",
                        SourceFileReader.getLine(callInfo.getFilePath(), line)
                ));
            }
        }
    }
}

package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.*;

import java.util.*;

public class TypeMismatchChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;

    private static final Map<String, String> FILTER_EXPECTED_TYPES = new HashMap<>();
    static {
        FILTER_EXPECTED_TYPES.put("upper", "string");
        FILTER_EXPECTED_TYPES.put("lower", "string");
        FILTER_EXPECTED_TYPES.put("capitalize", "string");
        FILTER_EXPECTED_TYPES.put("title", "string");
        FILTER_EXPECTED_TYPES.put("replace", "string");
        FILTER_EXPECTED_TYPES.put("join", "iterable");
        FILTER_EXPECTED_TYPES.put("first", "iterable");
        FILTER_EXPECTED_TYPES.put("last", "iterable");
        FILTER_EXPECTED_TYPES.put("sort", "iterable");
        FILTER_EXPECTED_TYPES.put("reverse", "iterable");
        FILTER_EXPECTED_TYPES.put("unique", "iterable");
        FILTER_EXPECTED_TYPES.put("length", "any");
        FILTER_EXPECTED_TYPES.put("int", "any");
        FILTER_EXPECTED_TYPES.put("float", "any");
        FILTER_EXPECTED_TYPES.put("string", "any");
        FILTER_EXPECTED_TYPES.put("default", "any");
        FILTER_EXPECTED_TYPES.put("trim", "string");
    }

    private static final Set<String> ITERABLE_TYPES = new HashSet<>(
            Arrays.asList("list", "string", "str", "dict", "tuple", "set")
    );

    public TypeMismatchChecker(SymbolTable symbolTable, List<SemanticError> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    public void check() {
        checkDeclaredVsInferredType();
        checkJinjaBridgeUsages();
    }

    // ===== حالة 1 و 2: type hint ≠ inferred type =====
    private void checkDeclaredVsInferredType() {
        for (SymbolEntry entry : symbolTable.getAllEntries()) {
            if (!"python".equals(entry.getSource())) continue;
            String declared = entry.getDeclaredType();
            if (declared == null) continue;

            String actual = entry.getType();

            if ("NoneType".equals(actual) || "none".equalsIgnoreCase(actual)) {
                // حالة 2: None مع type hint
                errors.add(new SemanticError(
                        SemanticErrorType.TYPE_MISMATCH,
                        "TypeError",
                        "Type mismatch. Variable '" + entry.getName() + "' is declared as '"
                                + declared + "' but assigned a value of type 'NoneType'.",
                        entry.getLine(),
                        entry.getFileName(),
                        entry.getName(),
                        "",
                        SourceFileReader.getLine(entry.getFilePath(), entry.getLine())  // ← مو symbolTable
                ));
            } else if (!isCompatible(declared, actual)) {
                // حالة 1: type hint ≠ actual type
                errors.add(new SemanticError(
                        SemanticErrorType.TYPE_MISMATCH,
                        "TypeError",
                        "Type mismatch. Variable '" + entry.getName() + "' is declared as '"
                                + declared + "' but assigned a value of type '" + actual + "'.",
                        entry.getLine(),
                        entry.getFileName(),
                        entry.getName(),
                        "",
                        SourceFileReader.getLine(entry.getFilePath(), entry.getLine())
                ));
            }
        }
    }

    // ===== حالة 3 و 4: Bridge (Flask → Jinja) =====
    private void checkJinjaBridgeUsages() {
        Map<String, String> flaskVarTypes = new HashMap<>();
        for (FlaskTemplateCall call : symbolTable.getRenderTemplateCalls()) {
            for (String varName : call.getPassedVariables()) {
                String type = call.getPassedVariableType(varName);
                if (type != null) flaskVarTypes.put(varName, type);
            }
        }

        for (JinjaFilterUsage usage : symbolTable.getJinjaFilterUsages()) {
            String varName = usage.getVariableName();
            String flaskType = flaskVarTypes.get(varName);
            if (flaskType == null) continue;

            if ("filter".equals(usage.getUsageContext())) {
                String expected = FILTER_EXPECTED_TYPES.get(usage.getFilterName());
                if (expected == null || "any".equals(expected)) continue;

                if (!isCompatibleWithExpected(expected, flaskType)) {
                    String expectedMsg = "iterable".equals(expected) ? "iterable" : "'" + expected + "'";
                    errors.add(new SemanticError(
                            SemanticErrorType.TYPE_MISMATCH,
                            "TypeError",
                            "Filter '" + usage.getFilterName() + "' expects " + expectedMsg
                                    + " but variable '" + varName + "' is of type '" + flaskType + "'.",
                            usage.getLine(),
                            usage.getFileName(),
                            varName,
                            "{{ " + varName + "|" + usage.getFilterName() + " }}",
                            SourceFileReader.getLine(usage.getFilePath(), usage.getLine())   // ← مو symbolTable
                    ));
                }

            } else if ("for_loop".equals(usage.getUsageContext())) {
                if (!ITERABLE_TYPES.contains(flaskType)) {
                    errors.add(new SemanticError(
                            SemanticErrorType.TYPE_MISMATCH,
                            "TypeError",
                            "Variable '" + varName + "' of type '" + flaskType
                                    + "' is not iterable.",
                            usage.getLine(),
                            usage.getFileName(),
                            varName,
                            "{% for item in " + varName + " %}",
                            SourceFileReader.getLine(usage.getFilePath(), usage.getLine())   // ← مو symbolTable
                    ));
                }
            }
        }
    }

    private boolean isCompatible(String declared, String actual) {
        if (declared.equals(actual)) return true;
        if (("string".equals(declared) || "str".equals(declared)) &&
                ("string".equals(actual) || "str".equals(actual))) return true;
        if ("int".equals(declared) && "bool".equals(actual)) return true;
        if ("float".equals(declared) && ("int".equals(actual) || "bool".equals(actual))) return true;
        if ("iterable".equals(declared) && ITERABLE_TYPES.contains(actual)) return true;
        return false;
    }

    private boolean isCompatibleWithExpected(String expected, String actual) {
        if ("iterable".equals(expected)) return ITERABLE_TYPES.contains(actual);
        if ("string".equals(expected)) return "string".equals(actual) || "str".equals(actual);
        return expected.equals(actual);
    }
}
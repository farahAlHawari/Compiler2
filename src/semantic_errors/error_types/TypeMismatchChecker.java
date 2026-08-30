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
        checkJinjaSetUsages();
    }


    private void checkDeclaredVsInferredType() {
        for (SymbolEntry entry : symbolTable.getAllEntries()) {
            if (!"python".equals(entry.getSource())) continue;
            String declared = entry.getDeclaredType();
            if (declared == null) continue;

            String actual = entry.getType();

            if (("NoneType".equals(actual) || "none".equalsIgnoreCase(actual))
                    && !isOptionalType(declared)) {





                errors.add(new SemanticError(
                        SemanticErrorType.TYPE_MISMATCH,
                        "TypeMismatch",
                        "Type mismatch. Variable '" + entry.getName() + "' is declared as '"
                                + declared + "' but assigned a value of type 'NoneType'.",
                        entry.getLine(),
                        entry.getFileName(),
                        entry.getName(),
                        "",
                        SourceFileReader.getLine(entry.getFilePath(), entry.getLine())
                ));
            } else if (!isOptionalType(declared) && !isCompatible(declared, actual)) {

                errors.add(new SemanticError(
                        SemanticErrorType.TYPE_MISMATCH,
                        "TypeMismatch",
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


    private void checkJinjaBridgeUsages() {

        Map<String, Set<String>> flaskVarTypes = new HashMap<>();
        for (FlaskTemplateCall call : symbolTable.getRenderTemplateCalls()) {
            for (String varName : call.getPassedVariables()) {
                Set<String> types = call.getPassedVariableTypes(varName);
                if (types != null && !types.isEmpty()) {
                    flaskVarTypes
                            .computeIfAbsent(varName, k -> new HashSet<>())
                            .addAll(types);
                }
            }
        }

        for (JinjaFilterUsage usage : symbolTable.getJinjaFilterUsages()) {
            String varName = usage.getVariableName();


            String rootVar = varName.contains(".")
                    ? varName.split("\\.")[0].trim()
                    : varName;

            Set<String> flaskTypes = flaskVarTypes.get(rootVar);
            if (flaskTypes == null || flaskTypes.isEmpty()) continue;

            if ("filter".equals(usage.getUsageContext())) {
                String expected = FILTER_EXPECTED_TYPES.get(usage.getFilterName());
                if (expected == null || "any".equals(expected)) continue;


                boolean anyCompatible = false;
                for (String flaskType : flaskTypes) {
                    if (isCompatibleWithExpected(expected, flaskType)) {
                        anyCompatible = true;
                        break;
                    }
                }

                if (!anyCompatible) {
                    String expectedMsg = "iterable".equals(expected) ? "iterable" : "'" + expected + "'";

                    String typesStr = String.join(" or ", flaskTypes);
                    errors.add(new SemanticError(
                            SemanticErrorType.TYPE_MISMATCH,
                            "TypeMismatch",
                            "Filter '" + usage.getFilterName() + "' expects " + expectedMsg
                                    + " but variable '" + varName + "' is of type '" + typesStr + "'.",
                            usage.getLine(),
                            usage.getFileName(),
                            varName,
                            "{{ " + varName + "|" + usage.getFilterName() + " }}",
                            SourceFileReader.getLine(usage.getFilePath(), usage.getLine())
                    ));
                }

            } else if ("for_loop".equals(usage.getUsageContext())) {

                boolean anyIterable = false;
                for (String flaskType : flaskTypes) {
                    if (ITERABLE_TYPES.contains(flaskType)) {
                        anyIterable = true;
                        break;
                    }
                }

                if (!anyIterable) {
                    String typesStr = String.join(" or ", flaskTypes);
                    errors.add(new SemanticError(
                            SemanticErrorType.TYPE_MISMATCH,
                            "TypeMismatch",
                            "Variable '" + varName + "' of type '" + typesStr
                                    + "' is not iterable.",
                            usage.getLine(),
                            usage.getFileName(),
                            varName,
                            "{% for item in " + varName + " %}",
                            SourceFileReader.getLine(usage.getFilePath(), usage.getLine())
                    ));
                }
            }
        }
    }

    private void checkJinjaSetUsages() {

        Map<String, Set<String>> jinjaSetTypes = new HashMap<>();
        for (SymbolEntry entry : symbolTable.getAllEntries()) {
            if (!"template".equals(entry.getSource())) continue;
            if (!"jinja_set_var".equals(entry.getType())) continue;

            String varName = entry.getName();
            String inferredType = entry.getDeclaredType();
            if (inferredType == null || inferredType.isEmpty()) {
                inferredType = "unknown";
            }

            jinjaSetTypes
                    .computeIfAbsent(varName, k -> new HashSet<>())
                    .add(inferredType);
        }

        if (jinjaSetTypes.isEmpty()) return;


        for (JinjaFilterUsage usage : symbolTable.getJinjaFilterUsages()) {
            String varName = usage.getVariableName();


            String rootVar = varName.contains(".")
                    ? varName.split("\\.")[0].trim()
                    : varName;

            Set<String> localTypes = jinjaSetTypes.get(rootVar);
            if (localTypes == null || localTypes.isEmpty()) continue;

            if ("filter".equals(usage.getUsageContext())) {
                String expected = FILTER_EXPECTED_TYPES.get(usage.getFilterName());
                if (expected == null || "any".equals(expected)) continue;

                boolean anyCompatible = false;
                for (String localType : localTypes) {
                    if ("unknown".equals(localType)) {
                        anyCompatible = true;
                        break;
                    }
                    if (isCompatibleWithExpected(expected, localType)) {
                        anyCompatible = true;
                        break;
                    }
                }

                if (!anyCompatible) {
                    String expectedMsg = "iterable".equals(expected) ? "iterable" : "'" + expected + "'";
                    String typesStr = String.join(" or ", localTypes);
                    errors.add(new SemanticError(
                            SemanticErrorType.TYPE_MISMATCH,
                            "TypeMismatch",
                            "Filter '" + usage.getFilterName() + "' expects " + expectedMsg
                                    + " but variable '" + varName + "' (defined with {% set %}) is of type '"
                                    + typesStr + "'.",
                            usage.getLine(),
                            usage.getFileName(),
                            varName,
                            "{{ " + varName + "|" + usage.getFilterName() + " }}",
                            SourceFileReader.getLine(usage.getFilePath(), usage.getLine())
                    ));
                }


            } else if ("for_loop".equals(usage.getUsageContext())) {
                boolean anyIterable = false;
                for (String localType : localTypes) {
                    if ("unknown".equals(localType)) {
                        anyIterable = true;
                        break;
                    }
                    if (ITERABLE_TYPES.contains(localType)) {
                        anyIterable = true;
                        break;
                    }
                }

                if (!anyIterable) {
                    String typesStr = String.join(" or ", localTypes);
                    errors.add(new SemanticError(
                            SemanticErrorType.TYPE_MISMATCH,
                            "TypeMismatch",
                            "Variable '" + varName + "' (defined with {% set %}) of type '"
                                    + typesStr + "' is not iterable.",
                            usage.getLine(),
                            usage.getFileName(),
                            varName,
                            "{% for item in " + varName + " %}",
                            SourceFileReader.getLine(usage.getFilePath(), usage.getLine())
                    ));
                }
            }
        }
    }
    private boolean isCompatible(String declared, String actual) {
        if (declared == null) return true;


        if (declared.startsWith("Optional[")) {
            String inner = declared.substring(9, declared.length() - 1);
            if ("NoneType".equals(actual) || "none".equalsIgnoreCase(actual)) return true;
            return isCompatible(inner, actual);
        }
        if (declared.startsWith("Union[")) {
            String[] parts = declared.substring(6, declared.length() - 1).split(",");
            for (String p : parts) {
                if (isCompatible(p.trim(), actual)) return true;
            }
            return false;
        }
        if (declared.equals(actual)) return true;
        if (("string".equals(declared) || "str".equals(declared)) &&
                ("string".equals(actual) || "str".equals(actual))) return true;
        if ("int".equals(declared) && "bool".equals(actual)) return true;
        if ("float".equals(declared) && ("int".equals(actual) || "bool".equals(actual))) return true;
        if ("iterable".equals(declared) && ITERABLE_TYPES.contains(actual)) return true;
        if ("Any".equals(declared) || "any".equals(declared)) return true;
        if ("complex".equals(declared) && Set.of("int","float","bool").contains(actual)) return true;

        return false;
    }

    private boolean isCompatibleWithExpected(String expected, String actual) {
        if ("iterable".equals(expected)) return ITERABLE_TYPES.contains(actual);
        if ("string".equals(expected)) return "string".equals(actual) || "str".equals(actual);
        return expected.equals(actual);
    }

    private boolean isOptionalType(String declared) {
        if (declared == null) return true;
        if ("Any".equals(declared) || "any".equals(declared)) return true;
        if (declared.startsWith("Optional[")) return true;
        if (declared.startsWith("Union[")) {

            String inner = declared.substring(6, declared.length() - 1);
            String[] parts = inner.split(",");
            for (String p : parts) {
                String t = p.trim();
                if ("None".equals(t) || "NoneType".equals(t)) return true;
                if (isOptionalType(t)) return true;
            }
        }
        return false;
    }
}

package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.ReturnInfo;
import symbol_table.SourceFileReader;
import symbol_table.SymbolTable;
import symbol_table.SymbolEntry;

import java.util.*;


public class ReturnTypeMismatchChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;

    public ReturnTypeMismatchChecker(SymbolTable symbolTable, List<SemanticError> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    public void check() {
        for (ReturnInfo returnInfo : symbolTable.getReturnInfos()) {
            String funcName = returnInfo.getEnclosingFunctionName();
            String actualReturnType = returnInfo.getReturnExprType();
            int line = returnInfo.getLine();


            if (funcName == null || funcName.isEmpty()) continue;

            SymbolEntry funcEntry = symbolTable.lookup(funcName);
            if (funcEntry == null) continue;


            String declaredReturnType = funcEntry.getReturnType();
            if (declaredReturnType == null || declaredReturnType.isEmpty()) {
                continue;
            }

            if (actualReturnType == null || "unknown".equals(actualReturnType)) continue;

            String normalizedDeclared = normalizeType(declaredReturnType);
            String normalizedActual = normalizeType(actualReturnType);

            if (!isTypeCompatible(normalizedDeclared, normalizedActual)) {

                errors.add(new SemanticError(
                        SemanticErrorType.RETURN_TYPE_MISMATCH,
                        "TypeError(Return Type Mismatch)",
                        "Function '" + funcName + "' declares return type '"
                                + declaredReturnType + "' but returns '" + actualReturnType + "'",
                        line,
                        returnInfo.getFileName(),
                        funcName,
                        "return ...",
                        SourceFileReader.getLine(returnInfo.getFilePath(), line)
                ));
            }
        }
    }


    private String normalizeType(String type) {
        if (type == null) return "unknown";
        type = type.trim().toLowerCase();


        switch (type) {
            case "str":
                return "string";
            case "int":
                return "int";
            case "float":
                return "float";
            case "bool":
                return "bool";
            case "list":
                return "list";
            case "dict":
                return "dict";
            case "none":
            case "nonetype":
                return "none";
            default:
                return type;
        }
    }


    private boolean isTypeCompatible(String declared, String actual) {
        if (declared.equals(actual)) return true;


        if ("int".equals(declared) && "float".equals(actual)) return true;


        if ("none".equals(actual)) return false;

        return false;
    }
}
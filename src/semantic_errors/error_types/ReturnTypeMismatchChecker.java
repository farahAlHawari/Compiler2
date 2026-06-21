package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.ReturnInfo;
import symbol_table.SourceFileReader;
import symbol_table.SymbolTable;
import symbol_table.SymbolEntry;

import java.util.*;

/**
 * Checker for RETURN_TYPE_MISMATCH error.
 *
 * Definition: A function whose return type is specified with Type Hints
 *             but the actual return value is different.
 *
 * Case:
 *   def get_age() -> int:
 *       return "twenty"      // declared int, returned string

 * Algorithm:
 *   1. Get all return statement records from the symbol table
 *   2. For each return, find the enclosing function's declared return type
 *   3. If the declared type is not empty/null and doesn't match the actual return type → error
 *   4. Skip functions without type hints (no declared return type = no mismatch possible)
 */
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

            // Skip if no enclosing function (shouldn't happen but be safe)
            if (funcName == null || funcName.isEmpty()) continue;

            // Look up the function definition
            SymbolEntry funcEntry = symbolTable.lookup(funcName);
            if (funcEntry == null) continue;

            // Get the declared return type from type hint
            String declaredReturnType = funcEntry.getReturnType();
            if (declaredReturnType == null || declaredReturnType.isEmpty()) {
                continue; // No type hint declared → no mismatch possible
            }

            // If actual return type is unknown, skip (can't determine mismatch)
            if (actualReturnType == null || "unknown".equals(actualReturnType)) continue;

            // Normalize types for comparison
            String normalizedDeclared = normalizeType(declaredReturnType);
            String normalizedActual = normalizeType(actualReturnType);

            // Check for mismatch
            if (!isTypeCompatible(normalizedDeclared, normalizedActual)) {
//                errors.add(new SemanticError(
//                        SemanticErrorType.RETURN_TYPE_MISMATCH,
//                        "RETURN_TYPE_MISMATCH",
//                        "Function '" + funcName + "' declares return type '"
//                                + declaredReturnType + "' but returns '" + actualReturnType + "'",
//                        line
//                ));
                errors.add(new SemanticError(
                        SemanticErrorType.RETURN_TYPE_MISMATCH,
                        "TypeError",
                        "Function '" + funcName + "' declares return type '"
                                + declaredReturnType + "' but returns '" + actualReturnType + "'",
                        line,
                        returnInfo.getFileName(),
                        funcName,
                        "return ...",
                        SourceFileReader.getLine(returnInfo.getFilePath(), line)   // ← مو symbolTable
                ));
            }
        }
    }

    /**
     * Normalize type names for comparison.
     * Handles Python type aliases and common variations.
     */
    private String normalizeType(String type) {
        if (type == null) return "unknown";
        type = type.trim().toLowerCase();

        // Handle Python type aliases
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

    /**
     * Check if the actual return type is compatible with the declared type.
     * int is compatible with float (widening), but not vice versa.
     * None is never compatible with any declared type (except if declared as Optional).
     */
    private boolean isTypeCompatible(String declared, String actual) {
        if (declared.equals(actual)) return true;

        // int → float is allowed (widening)
        if ("int".equals(declared) && "float".equals(actual)) return true;

        // None is never compatible with a declared return type
        // (unless Optional is used, but we don't handle that for simplicity)
        if ("none".equals(actual)) return false;

        return false;
    }
}
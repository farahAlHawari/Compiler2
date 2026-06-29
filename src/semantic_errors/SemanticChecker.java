package semantic_errors;

import semantic_errors.error_types.*;
import symbol_table.SymbolTable;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class SemanticChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;

    public SemanticChecker(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.errors = new ArrayList<>();
    }

    /**
     * Run all semantic error checkers sequentially.
     * Each checker adds its found errors to the shared errors list.
     */
    public void checkErrors() {
        // 1. Missing Flask Variable Checker
        MissingFlaskVarChecker missingFlaskVarChecker = new MissingFlaskVarChecker(symbolTable, errors);
        missingFlaskVarChecker.check();

        // 2. Invalid Function Call Checker
        InvalidFuncCallChecker invalidFuncCallChecker = new InvalidFuncCallChecker(symbolTable, errors);
        invalidFuncCallChecker.check();

        // 3. Wrong Arguments Count Checker
        WrongArgsCountChecker wrongArgsCountChecker = new WrongArgsCountChecker(symbolTable, errors);
        wrongArgsCountChecker.check();

        // 4. Return Type Mismatch Checker
        ReturnTypeMismatchChecker returnTypeMismatchChecker = new ReturnTypeMismatchChecker(symbolTable, errors);
        returnTypeMismatchChecker.check();
        TypeMismatchChecker typeMismatchChecker = new TypeMismatchChecker(symbolTable, errors);
        typeMismatchChecker.check();

        DivisionByZeroChecker divisionByZeroChecker = new DivisionByZeroChecker(symbolTable, errors);
        divisionByZeroChecker.check();

        UnboundLocalErrorChecker unboundLocalErrorChecker = new UnboundLocalErrorChecker(symbolTable, errors);
        unboundLocalErrorChecker.check();

    }
    public List<SemanticError> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * Print all semantic errors to console.
     */
//    public void printErrors() {
//        System.out.println("\n" + "=".repeat(80));
//        System.out.println("                    SEMANTIC ERROR CHECK RESULTS");
//        System.out.println("=".repeat(80));
//
//        if (errors.isEmpty()) {
//            System.out.println("No Semantic Errors found.");
//        } else {
//            System.out.println("Semantic Check :");
//            for (SemanticError error : errors) {
//                System.out.println(error.toString());
//            }
//        }
//        System.out.println("=".repeat(80));
//    }
    public void printErrors() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                         SEMANTIC ERROR REPORT");
        System.out.println("=".repeat(80));

        if (errors.isEmpty()) {
            System.out.println("\nNo semantic errors found.\n");
        } else {
            System.out.println("\nFound " + errors.size() + " semantic error"
                    + (errors.size() == 1 ? "" : "s") + "\n");
            for (int i = 0; i < errors.size(); i++) {
                System.out.println(errors.get(i).toReportString(i + 1));
                if (i < errors.size() - 1) {
                    System.out.println("-".repeat(79) + "\n");
                }
            }
        }
        System.out.println("=".repeat(80));
    }
    /**
     * Write all semantic errors to a file.
     */
//    public void writeErrorsToFile(String filePath) {
//        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
//            writer.println("Semantic Check :");
//            if (errors.isEmpty()) {
//                writer.println("No Semantic Errors found.");
//            } else {
//                for (SemanticError error : errors) {
//                    writer.println(error.toString());
//                }
//            }
//        } catch (IOException e) {
//            System.err.println("Error writing semantic errors to file: " + e.getMessage());
//        }
//    }
    public void writeErrorsToFile(String filePath) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("=".repeat(80));
            writer.println("                         SEMANTIC ERROR REPORT");
            writer.println("=".repeat(80));
            if (errors.isEmpty()) {
                writer.println("\nNo semantic errors found.\n");
            } else {
                writer.println("\nFound " + errors.size() + " semantic error"
                        + (errors.size() == 1 ? "" : "s") + "\n");
                for (int i = 0; i < errors.size(); i++) {
                    writer.println(errors.get(i).toReportString(i + 1));
                    if (i < errors.size() - 1) {
                        writer.println("-".repeat(79) + "\n");
                    }
                }
            }
            writer.println("=".repeat(80));
        } catch (IOException e) {
            System.err.println("Error writing semantic errors to file: " + e.getMessage());
        }
    }

}

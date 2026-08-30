
package semantic_errors;

import semantic_errors.error_types.*;
import symbol_table.SymbolTable;
import symbol_table.SymbolEntry;
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


    public void checkErrors() {

        checkSymbolTableDuplicateDeclarations();


        MissingFlaskVarChecker missingFlaskVarChecker = new MissingFlaskVarChecker(symbolTable, errors);
        missingFlaskVarChecker.check();


        InvalidFuncCallChecker invalidFuncCallChecker = new InvalidFuncCallChecker(symbolTable, errors);
        invalidFuncCallChecker.check();


        WrongArgsCountChecker wrongArgsCountChecker = new WrongArgsCountChecker(symbolTable, errors);
        wrongArgsCountChecker.check();


        ReturnTypeMismatchChecker returnTypeMismatchChecker = new ReturnTypeMismatchChecker(symbolTable, errors);
        returnTypeMismatchChecker.check();
        TypeMismatchChecker typeMismatchChecker = new TypeMismatchChecker(symbolTable, errors);
        typeMismatchChecker.check();

        DivisionByZeroChecker divisionByZeroChecker = new DivisionByZeroChecker(symbolTable, errors);
        divisionByZeroChecker.check();

        UnboundLocalErrorChecker unboundLocalErrorChecker = new UnboundLocalErrorChecker(symbolTable, errors);
        unboundLocalErrorChecker.check();


        UseBeforeInitChecker useBeforeInitChecker = new UseBeforeInitChecker(symbolTable, errors);
        useBeforeInitChecker.check();


        InvalidAttributeAccessChecker invalidAttrChecker =
                new InvalidAttributeAccessChecker(symbolTable, errors);
        invalidAttrChecker.check();


        OperationOnNoneChecker operationOnNoneChecker =
                new OperationOnNoneChecker(symbolTable, errors);
        operationOnNoneChecker.check();


        OperationTypeErrorChecker operationTypeErrorChecker =
                new OperationTypeErrorChecker(symbolTable, errors);
        operationTypeErrorChecker.check();

    }

    private void checkSymbolTableDuplicateDeclarations() {
        for (SymbolEntry dup : symbolTable.getDuplicateErrors()) {
            errors.add(new SemanticError(
                    SemanticErrorType.DUPLICATE_DECLARATION,
                    "DuplicateDeclaration",
                    "Symbol '" + dup.getName() + "' is already declared in this scope",
                    dup.getLine(),
                    dup.getFileName(),
                    dup.getName(),
                    "",
                    ""
            ));
        }
    }

    public List<SemanticError> getErrors() {
        return errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }


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


    public void writeErrorsToFile(String filePath) {
        writeErrorsToFile(filePath, errors);
    }



    public static void writeErrorsToFile(String filePath, List<SemanticError> errorsToWrite) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            writer.println("=".repeat(80));
            writer.println("                         SEMANTIC ERROR REPORT");
            writer.println("=".repeat(80));
            if (errorsToWrite.isEmpty()) {
                writer.println("\nNo semantic errors found.\n");
            } else {
                writer.println("\nFound " + errorsToWrite.size() + " semantic error"
                        + (errorsToWrite.size() == 1 ? "" : "s") + "\n");
                for (int i = 0; i < errorsToWrite.size(); i++) {
                    writer.println(errorsToWrite.get(i).toReportString(i + 1));
                    if (i < errorsToWrite.size() - 1) {
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
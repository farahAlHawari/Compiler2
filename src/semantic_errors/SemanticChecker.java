//package semantic_errors;
//
//import semantic_errors.error_types.*;
//import symbol_table.SymbolTable;
//import symbol_table.SymbolEntry;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.ArrayList;
//import java.util.List;
//
//public class SemanticChecker {
//
//    private SymbolTable symbolTable;
//    private List<SemanticError> errors;
//
//    public SemanticChecker(SymbolTable symbolTable) {
//        this.symbolTable = symbolTable;
//        this.errors = new ArrayList<>();
//    }
//
//    /**
//     * Run all semantic error checkers sequentially.
//     * Each checker adds its found errors to the shared errors list.
//     */
//    public void checkErrors() {
//        // ★★★ إصلاح: هذا كان الفاحص المفقود. SymbolTable.insert() كانت
//        // تسجّل أخطاء "already declared" بقائمتها الخاصة (errorMessages /
//        // duplicateErrors) لوحدها، وMain.java كانت بوابته تشيك بس
//        // semanticChecker.getErrors() — يعني أخطاء التكرار الحقيقية
//        // (مو الزائفة اللي صلّحناها بـ SymbolTable.insert نفسها) كانت
//        // ممكن تفوت من البوابة وتخلي Generation يبدأ رغم وجود خطأ دلالي
//        // حقيقي. هذا الفاحص يوحّد القناتين.
//        checkSymbolTableDuplicateDeclarations();
//
//        // 1. Missing Flask Variable Checker
//        MissingFlaskVarChecker missingFlaskVarChecker = new MissingFlaskVarChecker(symbolTable, errors);
//        missingFlaskVarChecker.check();
//
//        // 2. Invalid Function Call Checker
//        InvalidFuncCallChecker invalidFuncCallChecker = new InvalidFuncCallChecker(symbolTable, errors);
//        invalidFuncCallChecker.check();
//
//        // 3. Wrong Arguments Count Checker
//        WrongArgsCountChecker wrongArgsCountChecker = new WrongArgsCountChecker(symbolTable, errors);
//        wrongArgsCountChecker.check();
//
//        // 4. Return Type Mismatch Checker
//        ReturnTypeMismatchChecker returnTypeMismatchChecker = new ReturnTypeMismatchChecker(symbolTable, errors);
//        returnTypeMismatchChecker.check();
//        TypeMismatchChecker typeMismatchChecker = new TypeMismatchChecker(symbolTable, errors);
//        typeMismatchChecker.check();
//
//        DivisionByZeroChecker divisionByZeroChecker = new DivisionByZeroChecker(symbolTable, errors);
//        divisionByZeroChecker.check();
//
//        UnboundLocalErrorChecker unboundLocalErrorChecker = new UnboundLocalErrorChecker(symbolTable, errors);
//        unboundLocalErrorChecker.check();
//
//        // 7. Use Before Initialization Checker
//        UseBeforeInitChecker useBeforeInitChecker = new UseBeforeInitChecker(symbolTable, errors);
//        useBeforeInitChecker.check();
//
//        // 8. Invalid Attribute Access Checker
//        InvalidAttributeAccessChecker invalidAttrChecker =
//                new InvalidAttributeAccessChecker(symbolTable, errors);
//        invalidAttrChecker.check();
//
//        // 9. Operation on None Checker
//        OperationOnNoneChecker operationOnNoneChecker =
//                new OperationOnNoneChecker(symbolTable, errors);
//        operationOnNoneChecker.check();
//
//        // 10. Operation Type Error Checker (Error Type — عملية بين نوعين غير متوافقين)
//        OperationTypeErrorChecker operationTypeErrorChecker =
//                new OperationTypeErrorChecker(symbolTable, errors);
//        operationTypeErrorChecker.check();
//
//    }
//
//    /**
//     * ★★★ إصلاح: يقرأ الأخطاء اللي سجّلها SymbolTable.insert() (تعريف
//     * مكرر لنفس الاسم بنفس الـ scope) ويحوّلها لكائنات SemanticError
//     * ضمن نفس القائمة الموحّدة اللي بوابة Main.java فعلياً تشيكها.
//     *
//     * بنستخدم symbolTable.getDuplicateErrors() (List<SymbolEntry>) وليس
//     * getErrorMessages() (List<String>) لأن الأخيرة فيها أيضاً تحذيرات
//     * CSS property المكررة ("Warning [Line ...]")، وهاي مش أخطاء دلالية
//     * حقيقية ولا يصح توقف Generation بسببها. duplicateErrors فيها فقط
//     * التكرارات الحقيقية (متغيرات/دوال/رموز)، لأن insertCssProperty ما
//     * بتضيف إلها شي أصلاً.
//     */
//    private void checkSymbolTableDuplicateDeclarations() {
//        for (SymbolEntry dup : symbolTable.getDuplicateErrors()) {
//            errors.add(new SemanticError(
//                    SemanticErrorType.DUPLICATE_DECLARATION,
//                    "DuplicateDeclaration",
//                    "Symbol '" + dup.getName() + "' is already declared in this scope",
//                    dup.getLine(),
//                    dup.getFileName(),
//                    dup.getName(),
//                    "",
//                    ""
//            ));
//        }
//    }
//
//    public List<SemanticError> getErrors() {
//        return errors;
//    }
//
//    public boolean hasErrors() {
//        return !errors.isEmpty();
//    }
//
//    /**
//     * Print all semantic errors to console.
//     */
//    public void printErrors() {
//        System.out.println("\n" + "=".repeat(80));
//        System.out.println("                         SEMANTIC ERROR REPORT");
//        System.out.println("=".repeat(80));
//
//        if (errors.isEmpty()) {
//            System.out.println("\nNo semantic errors found.\n");
//        } else {
//            System.out.println("\nFound " + errors.size() + " semantic error"
//                    + (errors.size() == 1 ? "" : "s") + "\n");
//            for (int i = 0; i < errors.size(); i++) {
//                System.out.println(errors.get(i).toReportString(i + 1));
//                if (i < errors.size() - 1) {
//                    System.out.println("-".repeat(79) + "\n");
//                }
//            }
//        }
//        System.out.println("=".repeat(80));
//    }
//
//    /**
//     * Write all semantic errors to a file.
//     */
//    public void writeErrorsToFile(String filePath) {
//        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
//            writer.println("=".repeat(80));
//            writer.println("                         SEMANTIC ERROR REPORT");
//            writer.println("=".repeat(80));
//            if (errors.isEmpty()) {
//                writer.println("\nNo semantic errors found.\n");
//            } else {
//                writer.println("\nFound " + errors.size() + " semantic error"
//                        + (errors.size() == 1 ? "" : "s") + "\n");
//                for (int i = 0; i < errors.size(); i++) {
//                    writer.println(errors.get(i).toReportString(i + 1));
//                    if (i < errors.size() - 1) {
//                        writer.println("-".repeat(79) + "\n");
//                    }
//                }
//            }
//            writer.println("=".repeat(80));
//        } catch (IOException e) {
//            System.err.println("Error writing semantic errors to file: " + e.getMessage());
//        }
//    }
//
//}
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

    /**
     * Run all semantic error checkers sequentially.
     * Each checker adds its found errors to the shared errors list.
     */
    public void checkErrors() {
        // ★★★ إصلاح: هذا كان الفاحص المفقود. SymbolTable.insert() كانت
        // تسجّل أخطاء "already declared" بقائمتها الخاصة (errorMessages /
        // duplicateErrors) لوحدها، وMain.java كانت بوابته تشيك بس
        // semanticChecker.getErrors() — يعني أخطاء التكرار الحقيقية
        // (مو الزائفة اللي صلّحناها بـ SymbolTable.insert نفسها) كانت
        // ممكن تفوت من البوابة وتخلي Generation يبدأ رغم وجود خطأ دلالي
        // حقيقي. هذا الفاحص يوحّد القناتين.
        checkSymbolTableDuplicateDeclarations();

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

        // 7. Use Before Initialization Checker
        UseBeforeInitChecker useBeforeInitChecker = new UseBeforeInitChecker(symbolTable, errors);
        useBeforeInitChecker.check();

        // 8. Invalid Attribute Access Checker
        InvalidAttributeAccessChecker invalidAttrChecker =
                new InvalidAttributeAccessChecker(symbolTable, errors);
        invalidAttrChecker.check();

        // 9. Operation on None Checker
        OperationOnNoneChecker operationOnNoneChecker =
                new OperationOnNoneChecker(symbolTable, errors);
        operationOnNoneChecker.check();

        // 10. Operation Type Error Checker (Error Type — عملية بين نوعين غير متوافقين)
        OperationTypeErrorChecker operationTypeErrorChecker =
                new OperationTypeErrorChecker(symbolTable, errors);
        operationTypeErrorChecker.check();

    }

    /**
     * ★★★ إصلاح: يقرأ الأخطاء اللي سجّلها SymbolTable.insert() (تعريف
     * مكرر لنفس الاسم بنفس الـ scope) ويحوّلها لكائنات SemanticError
     * ضمن نفس القائمة الموحّدة اللي بوابة Main.java فعلياً تشيكها.
     *
     * بنستخدم symbolTable.getDuplicateErrors() (List<SymbolEntry>) وليس
     * getErrorMessages() (List<String>) لأن الأخيرة فيها أيضاً تحذيرات
     * CSS property المكررة ("Warning [Line ...]")، وهاي مش أخطاء دلالية
     * حقيقية ولا يصح توقف Generation بسببها. duplicateErrors فيها فقط
     * التكرارات الحقيقية (متغيرات/دوال/رموز)، لأن insertCssProperty ما
     * بتضيف إلها شي أصلاً.
     */
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

    /**
     * Print all semantic errors to console.
     */
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
    public void writeErrorsToFile(String filePath) {
        writeErrorsToFile(filePath, errors);
    }

    /**
     * Write an arbitrary list of semantic errors to a file. Used by Main to
     * build a single combined report across multiple test runs (e.g. "Run
     * All Semantic Tests"), in addition to the normal single-run case above.
     */
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
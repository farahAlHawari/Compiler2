package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.UnboundLocalInfo;
import symbol_table.SymbolTable;
import symbol_table.SourceFileReader;

import java.util.List;

/**
 * Checker for UNBOUND_LOCAL error (Scope Error).
 *
 * يكشف الحالة التالية:
 *   متغير معرّف في scope خارجي (global) ومحاولة تعديله داخل function
 *   باستخدام augmented assignment (+=, -=, *=, /=, %=, إلخ)
 *   بدون استخدام كلمة global.
 *
 * مثال:
 *   x = 10
 *   def foo():
 *       x += 1    # ← UnboundLocalError: cannot access local variable 'x' before assignment
 *
 * بايثون تعتبر أي assignment داخل دالة يجعل المتغير local للدالة كلها.
 * لذلك x += 1 يعني x = x + 1، وبما أن x أصبح local ولم يأخذ قيمة بعد،
 * تحاول بايثون قراءة x قبل إسناد قيمة إليه → UnboundLocalError.
 */
public class UnboundLocalErrorChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;

    public UnboundLocalErrorChecker(SymbolTable symbolTable, List<SemanticError> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    public void check() {
        for (UnboundLocalInfo info : symbolTable.getUnboundLocalInfos()) {

            // لا نكشف إلا إذا كنا داخل دالة
            if (!info.isInsideFunction()) {
                continue;
            }

            // إذا المتغير موجود في السكوب الحالي (local) → لا مشكلة
            if (info.isVariableInCurrentScope()) {
                continue;
            }

            // إذا المتغير موجود في سكوب خارجي + لم يُستخدم global → خطأ!
            if (info.isVariableInOuterScope() && !info.isDeclaredGlobal()) {
                errors.add(new SemanticError(
                        SemanticErrorType.UNBOUND_LOCAL,
                        "UnboundLocalError",
                        "cannot access local variable '" + info.getVariableName()
                                + "' before assignment. Variable '" + info.getVariableName()
                                + "' is referenced inside " + info.getScopeType()
                                + " '" + info.getScopeContextName()
                                + "' but is defined in an outer scope without 'global' declaration",
                        info.getLine(),
                        info.getFileName(),
                        info.getVariableName(),
                        info.getVariableName() + " " + info.getOperator(),
                        SourceFileReader.getLine(info.getFilePath(), info.getLine())
                ));
            }
        }

    }
}
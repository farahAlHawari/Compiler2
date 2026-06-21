
package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.DivisionInfo;
import symbol_table.FlaskTemplateCall;
import symbol_table.JinjaFilterUsage;
import symbol_table.SymbolEntry;
import symbol_table.SymbolTable;
import symbol_table.SourceFileReader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Checker for DIVISION_BY_ZERO error.
 *
 * Covers exactly 4 cases, mirroring Python's real ZeroDivisionError:
 *
 *   1. Direct literal division by zero.
 *        x = 10 / 0
 *
 *   2. Division by a variable whose value is zero.
 *        y = 0
 *        x = 10 / y
 *
 *   3. Modulo by zero (literal or variable) — same rule as division,
 *      Python raises ZeroDivisionError for "%" too.
 *        x = 10 % 0
 *        x = 10 % y   (y = 0)
 *
 *   4. Flask -> Jinja Bridge: a Flask-passed variable used as a divisor
 *      inside a Jinja expression, whose value is zero.
 *        Flask: price = 0
 *        Jinja: {{ count / price }}
 *
 * Algorithm:
 *   - Cases 1, 2 & 3 walk every DivisionInfo recorded while visiting the
 *     Python AST (SymbolTableVisitor.visitBinaryOp). For a literal divisor we
 *     check the literal text directly; for a variable divisor we look up its
 *     current value in the SymbolTable.
 *   - Case 4 walks every JinjaFilterUsage recorded with usageContext
 *     "division_literal" or "division_variable" (see
 *     TemplateSymbolTableVisitor.extractVariablesFromExpression), resolving
 *     the variable's value the same way TypeMismatchChecker resolves types
 *     for the Bridge: from the render_template() call's captured values,
 *     NOT from symbolTable.lookup() directly (which could find an unrelated
 *     same-named Jinja "context_var" placeholder instead of the real
 *     Flask-side value).
 */
public class DivisionByZeroChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;

    public DivisionByZeroChecker(SymbolTable symbolTable, List<SemanticError> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    public void check() {
        checkPythonDivisions();   // Cases 1, 2 & 3 (Python side)
        checkJinjaBridgeDivisions(); // Case 4 (Flask -> Jinja Bridge)
    }

    // ==================== Cases 1, 2 & 3 (Python side) ====================

    private void checkPythonDivisions() {
        for (DivisionInfo info : symbolTable.getDivisionInfos()) {
            if (info.isDivisorLiteral()) {
                if (isZero(info.getDivisorLiteralValue())) {
                    reportPythonError(info, info.getDivisorLiteralValue());
                }
            } else {
                String varName = info.getDivisorVariableName();
                SymbolEntry varEntry = findClosestPythonEntry(varName, info.getLine());
                if (varEntry != null && isZero(varEntry.getValue())) {
                    reportPythonError(info, varName);
                }
            }
        }
    }

    private SymbolEntry findClosestPythonEntry(String varName, int beforeOrAtLine) {
        SymbolEntry best = null;
        for (SymbolEntry entry : symbolTable.getAllEntries()) {
            if (!"python".equals(entry.getSource())) continue;
            if (!varName.equals(entry.getName())) continue;
            if (entry.getLine() > beforeOrAtLine) continue;
            if (best == null || entry.getLine() > best.getLine()) {
                best = entry;
            }
        }
        return best;
    }

    private void reportPythonError(DivisionInfo info, String divisorDisplay) {
        String opWord = "%".equals(info.getOperator()) ? "modulo" : "division";
        errors.add(new SemanticError(
                SemanticErrorType.DIVISION_BY_ZERO,
                "ZeroDivisionError",
                "division by zero - " + opWord + " operation ('" + info.getOperator()
                        + "') with divisor '" + divisorDisplay + "' evaluates to 0",
                info.getLine(),
                info.getFileName(),
                info.getDivisorVariableName() != null ? info.getDivisorVariableName() : "",
                "",
                SourceFileReader.getLine(info.getFilePath(), info.getLine())
        ));
    }

    // ==================== Case 4 (Flask -> Jinja Bridge) ====================

    private void checkJinjaBridgeDivisions() {
        // Build varName -> Python-side value map from every render_template()
        // call, exactly like TypeMismatchChecker does for types. This is the
        // value Flask actually passed, captured before the template-side
        // visitor could insert a same-named "context_var" placeholder that
        // would otherwise shadow it on a generic lookup.
        Map<String, String> flaskVarValues = new HashMap<>();
        for (FlaskTemplateCall call : symbolTable.getRenderTemplateCalls()) {
            for (String varName : call.getPassedVariables()) {
                String value = call.getPassedVariableValue(varName);
                if (value != null) {
                    flaskVarValues.put(varName, value);
                }
            }
        }

        for (JinjaFilterUsage usage : symbolTable.getJinjaFilterUsages()) {
            String context = usage.getUsageContext();
            if (!"division_literal".equals(context) && !"division_variable".equals(context)) {
                continue; // Not a division/modulo usage -> not our concern here
            }

            String divisorName = usage.getVariableName(); // literal text OR variable name, depending on context
            String operatorToken = usage.getFilterName().replace("__division__", "");
            int line = usage.getLine();

            boolean divisorIsZero;
            if ("division_literal".equals(context)) {
                divisorIsZero = isZero(divisorName);
            } else {
                String flaskValue = flaskVarValues.get(divisorName);
                divisorIsZero = flaskValue != null && isZero(flaskValue);
            }

            if (divisorIsZero) {
                String opWord = "%".equals(operatorToken) ? "modulo" : "division";
                errors.add(new SemanticError(
                        SemanticErrorType.DIVISION_BY_ZERO,
                        "ZeroDivisionError",
                        "division by zero - " + opWord + " operation ('" + operatorToken
                                + "') with divisor '" + divisorName + "' evaluates to 0 in template",
                        line,
                        usage.getFileName(),
                        divisorName,
                        "",
                        SourceFileReader.getLine(usage.getFilePath(), line)
                ));
            }
        }
    }

    // ==================== Shared helpers ====================

    /**
     * True if the given text represents the numeric value zero, covering
     * both integer ("0") and float ("0.0") literal forms, matching Python's
     * own behavior: 10 / 0 and 10 / 0.0 both raise ZeroDivisionError.
     */
    private boolean isZero(String text) {
        if (text == null) return false;
        try {
            return Double.parseDouble(text.trim()) == 0.0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.SymbolEntry;
import symbol_table.SymbolTable;
import symbol_table.UseBeforeInitInfo;
import symbol_table.SourceFileReader;

import java.util.List;

/**
 * Checker for USE_BEFORE_INIT error (ScopeError: Use Before Init).
 *
 * Covers 3 cases:
 *
 *   1. Variable used before declaration (declared at a later line or not at all).
 *        print(x)
 *        x = 5
 *
 *   2. Variable has a type annotation but no value, used before real assignment.
 *        x: int
 *        print(x)
 *        x = 10
 *
 *   3. Variable only assigned inside a conditional block, used outside it.
 *        if cond:
 *            y = 5
 *        print(y)    // y may not be initialized if cond is False
 *
 * Algorithm:
 *   - Walks every UseBeforeInitInfo recorded by SymbolTableVisitor.visitIdentifier.
 *   - Skips forward references to non-variable symbols (functions, classes, imports)
 *     because Python allows forward references for those.
 *   - Generates an appropriate SemanticError for each case.
 */
public class UseBeforeInitChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;

    public UseBeforeInitChecker(SymbolTable symbolTable, List<SemanticError> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    public void check() {
        for (UseBeforeInitInfo info : symbolTable.getUseBeforeInitInfos()) {

            // Skip if this is a forward reference to a non-variable
            // (functions, classes, imports can be referenced before their definition in Python)
            if (!info.isConditionalAssignment() && !info.hasTypeAnnotationOnly() && info.isDeclaredLater()) {
                boolean isLaterNonVariable = false;
                for (SymbolEntry e : symbolTable.getAllEntries()) {
                    if (e.getName().equals(info.getVariableName())
                            && e.getLine() > info.getUsageLine()
                            && !"variable".equals(e.getKind())) {
                        isLaterNonVariable = true;
                        break;
                    }
                }
                if (isLaterNonVariable) continue;
            }

            String message;
            if (info.isConditionalAssignment()) {
                message = "Variable '" + info.getVariableName()
                        + "' may be used before initialization. "
                        + "It is only assigned inside a conditional block.";
            } else if (info.hasTypeAnnotationOnly()) {
                message = "Variable '" + info.getVariableName()
                        + "' has a type annotation but is used before being assigned a value.";
            } else if (info.isDeclaredLater()) {
                message = "Variable '" + info.getVariableName()
                        + "' is used before initialization (declared at a later line).";
            } else {
                message = "Variable '" + info.getVariableName()
                        + "' is used before initialization (not declared in the current scope).";
            }

            errors.add(new SemanticError(
                    SemanticErrorType.USE_BEFORE_INIT,
                    "ScopeError",
                    message,
                    info.getUsageLine(),
                    info.getFileName(),
                    info.getVariableName(),
                    "",
                    SourceFileReader.getLine(info.getFilePath(), info.getUsageLine())
            ));
        }
    }
}
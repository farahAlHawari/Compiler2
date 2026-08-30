package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.SymbolEntry;
import symbol_table.SymbolTable;
import symbol_table.UseBeforeInitInfo;
import symbol_table.SourceFileReader;

import java.util.List;


public class UseBeforeInitChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;

    public UseBeforeInitChecker(SymbolTable symbolTable, List<SemanticError> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    public void check() {
        for (UseBeforeInitInfo info : symbolTable.getUseBeforeInitInfos()) {


            if (!info.isConditionalAssignment() && !info.hasTypeAnnotationOnly()
                    && info.isDeclaredLater()) {
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


            String errorName;
            String message;

            if (info.isConditionalAssignment()) {
                errorName = "ScopeError";
                message = "Variable '" + info.getVariableName()
                        + "' may be used before initialization. "
                        + "It is only assigned inside a conditional block.";
            } else if (info.hasTypeAnnotationOnly()) {
                errorName = "ScopeError";
                message = "Variable '" + info.getVariableName()
                        + "' has a type annotation but is used before being assigned a value.";
            } else if (info.isDeclaredLater()) {
                errorName = "NameError";
                message = "name '" + info.getVariableName()
                        + "' is not defined. It is declared at a later line.";
            } else {
                errorName = "NameError";
                message = "name '" + info.getVariableName()
                        + "' is not defined";
            }

            errors.add(new SemanticError(
                    SemanticErrorType.USE_BEFORE_INIT,
                    errorName,
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
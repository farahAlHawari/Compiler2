package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.UnboundLocalInfo;
import symbol_table.SymbolTable;
import symbol_table.SourceFileReader;

import java.util.List;


public class UnboundLocalErrorChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;

    public UnboundLocalErrorChecker(SymbolTable symbolTable, List<SemanticError> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    public void check() {
        for (UnboundLocalInfo info : symbolTable.getUnboundLocalInfos()) {


            if (!info.isInsideFunction()) {
                continue;
            }


            if (info.isVariableInCurrentScope()) {
                continue;
            }


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
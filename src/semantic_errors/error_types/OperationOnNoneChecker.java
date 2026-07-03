package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.OperationOnNoneInfo;
import symbol_table.SourceFileReader;
import symbol_table.SymbolTable;

import java.util.List;

public class OperationOnNoneChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;

    public OperationOnNoneChecker(SymbolTable symbolTable, List<SemanticError> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    public void check() {
        for (OperationOnNoneInfo info : symbolTable.getOperationOnNoneInfos()) {

            String noneSide = info.isLeftIsNone()
                    ? info.getLeftOperandName()
                    : info.getRightOperandName();

            String otherType = info.getOtherOperandType();
            if (otherType == null || otherType.isEmpty()) {
                otherType = "unknown";
            }

            String message = "unsupported operand type(s) for " + info.getOperator()
                    + ": 'NoneType' and '" + otherType + "'";

            errors.add(new SemanticError(
                    SemanticErrorType.OPERATION_ON_NONE,
                    "TypeError",
                    message,
                    info.getLine(),
                    info.getFileName(),
                    noneSide,
                    noneSide + " " + info.getOperator() + " ...",
                    SourceFileReader.getLine(info.getFilePath(), info.getLine())
            ));
        }
    }
}
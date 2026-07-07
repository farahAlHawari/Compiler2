package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.*;

import java.util.List;
import java.util.Set;

public class OperationTypeErrorChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;

    private static final Set<String> NUMERIC_TYPES =
            Set.of("int", "float", "bool");
    private static final Set<String> SUBSCRIPTABLE_TYPES =
            Set.of("string", "list", "dict");
    private static final Set<String> SIZED_TYPES =
            Set.of("string", "list", "dict", "tuple", "set");

    // ===== جديد: تحويل عامل augmented (//=) لعامله الأساسي (//) =====
    private static final java.util.Map<String, String> AUGMENTED_TO_BASE = java.util.Map.of(
            "+=", "+", "-=", "-", "*=", "*", "/=", "/",
            "%=", "%", "**=", "**", "//=", "//"
    );

    private String stripAugmented(String operator) {
        return AUGMENTED_TO_BASE.getOrDefault(operator, operator);
    }

    public OperationTypeErrorChecker(SymbolTable symbolTable, List<SemanticError> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    public void check() {
        checkOperationTypes();
        checkUnaryOperations();
        checkIndexTypes();
        checkLenCalls();
        checkBuiltinFunctionCalls();
    }

    // ===== 1. العمليات الثنائية (حالات 1-11) =====
    private void checkOperationTypes() {
        for (OperationTypeInfo info : symbolTable.getOperationTypeInfos()) {
            String operator = info.getOperator();
            String baseOperator = stripAugmented(operator);
            String leftType = normalize(info.getLeftType());
            String rightType = normalize(info.getRightType());

            if ("unknown".equals(leftType) || "unknown".equals(rightType)) continue;

            // العمليات الحسابية مع NoneType مسؤولية OperationOnNoneChecker
            if (isArithmeticOperator(baseOperator)
                    && ("NoneType".equals(leftType) || "NoneType".equals(rightType))) {
                continue;
            }

            if (!isCompatible(baseOperator, leftType, rightType)) {
                String message = buildOperationErrorMessage(operator, leftType, rightType);
                String expr = info.getLeftOperand() + " " + operator + " " + info.getRightOperand();

                errors.add(new SemanticError(
                        SemanticErrorType.OPERATION_TYPE_ERROR,
                        "TypeError",
                        message,
                        info.getLine(),
                        info.getFileName(),
                        expr,
                        expr,
                        SourceFileReader.getLine(info.getFilePath(), info.getLine())
                ));
            }
        }
    }

    // ===== 2. العمليات الأحادية — -x +x (حالات 25-26) =====
    private void checkUnaryOperations() {
        for (UnaryOpTypeInfo info : symbolTable.getUnaryOpTypeInfos()) {
            String operandType = normalize(info.getOperandType());

            if ("unknown".equals(operandType)) continue;
            if (isNumeric(operandType)) continue; // int, float, bool — صحيح

            String message = "bad operand type for unary "
                    + info.getOperator() + ": '" + operandType + "'";

            errors.add(new SemanticError(
                    SemanticErrorType.OPERATION_TYPE_ERROR,
                    "TypeError",
                    message,
                    info.getLine(),
                    info.getFileName(),
                    info.getOperandDisplay(),
                    info.getOperator() + info.getOperandDisplay(),
                    SourceFileReader.getLine(info.getFilePath(), info.getLine())
            ));
        }
    }

    // ===== 3. الفهرسة (حالات 14-16) =====
    private void checkIndexTypes() {
        for (IndexTypeInfo info : symbolTable.getIndexTypeInfos()) {
            String containerType = normalize(info.getContainerType());
            String indexType = normalize(info.getIndexType());

            if ("unknown".equals(containerType)) continue;

            String expr = info.getContainerDisplay() + "[" + info.getIndexDisplay() + "]";

            if (!SUBSCRIPTABLE_TYPES.contains(containerType)) {
                errors.add(new SemanticError(
                        SemanticErrorType.OPERATION_TYPE_ERROR,
                        "TypeError",
                        "'" + containerType + "' object is not subscriptable",
                        info.getLine(),
                        info.getFileName(),
                        info.getContainerDisplay(),
                        expr,
                        SourceFileReader.getLine(info.getFilePath(), info.getLine())
                ));
                continue;
            }

            boolean isDict = "dict".equals(containerType);
            if (!isDict && !"unknown".equals(indexType) && !"int".equals(indexType)) {
                errors.add(new SemanticError(
                        SemanticErrorType.OPERATION_TYPE_ERROR,
                        "TypeError",
                        containerType + " indices must be integers, not '" + indexType + "'",
                        info.getLine(),
                        info.getFileName(),
                        info.getContainerDisplay(),
                        expr,
                        SourceFileReader.getLine(info.getFilePath(), info.getLine())
                ));
            }
        }
    }

    // ===== 4. len() على نوع مش قابل للقياس (حالة 17) =====
    private void checkLenCalls() {
        for (FunctionArgTypeInfo info : symbolTable.getFunctionArgTypeInfos()) {
            if (!"len".equals(info.getFunctionName())) continue;

            String argType = normalize(info.getArgType());
            if ("unknown".equals(argType)) continue;

            if (!SIZED_TYPES.contains(argType)) {
                errors.add(new SemanticError(
                        SemanticErrorType.OPERATION_TYPE_ERROR,
                        "TypeError",
                        "object of type '" + argType + "' has no len()",
                        info.getLine(),
                        info.getFileName(),
                        info.getArgDisplay(),
                        "len(" + info.getArgDisplay() + ")",
                        SourceFileReader.getLine(info.getFilePath(), info.getLine())
                ));
            }
        }
    }

    // ===== 5. دوال built-in أخرى (حالات 28-32) =====
    private void checkBuiltinFunctionCalls() {
        for (FunctionArgTypeInfo info : symbolTable.getFunctionArgTypeInfos()) {
            String funcName = info.getFunctionName();
            if ("len".equals(funcName)) continue; // مفحوص فوق

            String argType = normalize(info.getArgType());
            if ("unknown".equals(argType)) continue;

            switch (funcName) {
                case "sum":
                case "sorted":
                case "max":
                case "min":
                    // هالدول بتحتاج iterable (list, string, dict, tuple, set)
                    if (!SIZED_TYPES.contains(argType)) {
                        errors.add(new SemanticError(
                                SemanticErrorType.OPERATION_TYPE_ERROR,
                                "TypeError",
                                "'" + argType + "' object is not iterable",
                                info.getLine(),
                                info.getFileName(),
                                info.getArgDisplay(),
                                funcName + "(" + info.getArgDisplay() + ")",
                                SourceFileReader.getLine(info.getFilePath(), info.getLine())
                        ));
                    }
                    break;

                case "abs":
                    if (!isNumeric(argType)) {
                        errors.add(new SemanticError(
                                SemanticErrorType.OPERATION_TYPE_ERROR,
                                "TypeError",
                                "bad operand type for abs(): '" + argType + "'",
                                info.getLine(),
                                info.getFileName(),
                                info.getArgDisplay(),
                                "abs(" + info.getArgDisplay() + ")",
                                SourceFileReader.getLine(info.getFilePath(), info.getLine())
                        ));
                    }
                    break;

                case "round":
                    if (!isNumeric(argType)) {
                        errors.add(new SemanticError(
                                SemanticErrorType.OPERATION_TYPE_ERROR,
                                "TypeError",
                                "type " + argType + " doesn't define __round__ method",
                                info.getLine(),
                                info.getFileName(),
                                info.getArgDisplay(),
                                "round(" + info.getArgDisplay() + ")",
                                SourceFileReader.getLine(info.getFilePath(), info.getLine())
                        ));
                    }
                    break;
            }
        }
    }

    // ==================== Type Compatibility ====================

    private boolean isCompatible(String operator, String leftType, String rightType) {
        String op = stripAugmented(operator);
        switch (op) {
            case "+":
                if (isNumeric(leftType) && isNumeric(rightType)) return true;
                if ("string".equals(leftType) && "string".equals(rightType)) return true;
                if ("list".equals(leftType) && "list".equals(rightType)) return true;
                return false;

            case "-":
            case "/":
            case "//":
            case "%":
            case "**":
                return isNumeric(leftType) && isNumeric(rightType);

            case "*":
                if (isNumeric(leftType) && isNumeric(rightType)) return true;
                if (isRepeatable(leftType) && isRepeatCount(rightType)) return true;
                if (isRepeatable(rightType) && isRepeatCount(leftType)) return true;
                return false;

            case "<":
            case ">":
            case "<=":
            case ">=":
                if (isNumeric(leftType) && isNumeric(rightType)) return true;
                if ("string".equals(leftType) && "string".equals(rightType)) return true;
                if ("list".equals(leftType) && "list".equals(rightType)) return true;
                return false;

            default:
                return true;
        }
    }

    private boolean isNumeric(String type) {
        return NUMERIC_TYPES.contains(type);
    }

    private boolean isRepeatable(String type) {
        return "string".equals(type) || "list".equals(type);
    }

    private boolean isRepeatCount(String type) {
        return "int".equals(type) || "bool".equals(type);
    }

    private String normalize(String type) {
        if (type == null) return "unknown";
        if ("str".equals(type)) return "string";
        if ("none".equals(type)) return "NoneType";
        return type;
    }

    private String buildOperationErrorMessage(String operator, String leftType, String rightType) {
        String baseOperator = stripAugmented(operator);

        // خاص: str % anything → رسالة formatting
        if ("%".equals(baseOperator) && "string".equals(leftType)) {
            return "not all arguments converted during string formatting";
        }

        // خاص جديد: + مع str/list بالطرف الأيسر → رسالة concatenate
        if ("+".equals(baseOperator) && ("string".equals(leftType) || "list".equals(leftType))) {
            String leftDisplayType = leftType.equals("string") ? "str" : leftType;
            String rightDisplayType = rightType.equals("string") ? "str" : rightType;
            return "can only concatenate " + leftDisplayType
                    + " (not \"" + rightDisplayType + "\") to " + leftDisplayType;
        }

        // خاص: * مع sequence و non-int
        if ("*".equals(baseOperator)) {
            boolean leftSeq = isRepeatable(leftType);
            boolean rightSeq = isRepeatable(rightType);
            if (leftSeq && !isRepeatCount(rightType)) {
                return "can't multiply sequence by non-int of type '" + rightType + "'";
            }
            if (rightSeq && !isRepeatCount(leftType)) {
                return "can't multiply sequence by non-int of type '" + leftType + "'";
            }
        }

        if (isComparisonOperator(baseOperator)) {
            return "'" + operator + "' not supported between instances of '"
                    + leftType + "' and '" + rightType + "'";
        }

        // خاص جديد: ** بيحتاج "or pow()" زيادة عن العامل
        String displayOperator = "**".equals(baseOperator) ? "** or pow()" : operator;
        return "unsupported operand type(s) for " + displayOperator + ": '"
                + leftType + "' and '" + rightType + "'";
    }

    private boolean isComparisonOperator(String operator) {
        return "<".equals(operator) || ">".equals(operator)
                || "<=".equals(operator) || ">=".equals(operator);
    }

    private boolean isArithmeticOperator(String operator) {
        return "+".equals(operator) || "-".equals(operator) || "*".equals(operator)
                || "/".equals(operator) || "%".equals(operator)
                || "**".equals(operator) || "//".equals(operator);
    }


}
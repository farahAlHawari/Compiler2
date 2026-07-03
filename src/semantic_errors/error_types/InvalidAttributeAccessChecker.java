package semantic_errors.error_types;

import semantic_errors.SemanticError;
import semantic_errors.SemanticErrorType;
import symbol_table.AttributeAccessInfo;
import symbol_table.SourceFileReader;
import symbol_table.SymbolTable;

import java.util.List;

public class InvalidAttributeAccessChecker {

    private SymbolTable symbolTable;
    private List<SemanticError> errors;

    // Attributes المعروفة لأنواع بايثون المدمجة
    private static final java.util.Set<String> STRING_ATTRS = java.util.Set.of(
            "upper", "lower", "strip", "lstrip", "rstrip", "split", "replace",
            "find", "startswith", "endswith", "count", "format", "join",
            "isdigit", "isalpha", "isalnum", "encode", "decode"
    );
    private static final java.util.Set<String> LIST_ATTRS = java.util.Set.of(
            "append", "extend", "insert", "remove", "pop", "clear",
            "index", "count", "sort", "reverse", "copy"
    );
    private static final java.util.Set<String> DICT_ATTRS = java.util.Set.of(
            "keys", "values", "items", "get", "pop", "update", "clear",
            "setdefault", "copy"
    );
    private static final java.util.Set<String> INT_ATTRS = java.util.Set.of(
            "real", "imag", "numerator", "denominator", "bit_length"
    );
    private static final java.util.Set<String> FLOAT_ATTRS = java.util.Set.of(
            "real", "imag", "is_integer"
    );

    public InvalidAttributeAccessChecker(SymbolTable symbolTable, List<SemanticError> errors) {
        this.symbolTable = symbolTable;
        this.errors = errors;
    }

    public void check() {
        for (AttributeAccessInfo info : symbolTable.getAttributeAccessInfos()) {

            String objType = info.getObjectType();
            String objValue = info.getObjectValue();
            String attr = info.getAttributeName();

            // ===== الحالة 1: AttributeError on NoneType =====
            // إذا الكائن قيمته None أو نوعه none/NoneType
            if ("none".equalsIgnoreCase(objType)
                    || "NoneType".equalsIgnoreCase(objType)
                    || "None".equals(objValue)) {
                errors.add(new SemanticError(
                        SemanticErrorType.INVALID_ATTRIBUTE_ACCESS,
                        "AttributeError",
                        "'" + info.getObjectName() + "' is NoneType. "
                                + "NoneType object has no attribute '" + attr + "'",
                        info.getLine(),
                        info.getFileName(),
                        info.getObjectName() + "." + attr,
                        info.getObjectName() + "." + attr,
                        SourceFileReader.getLine(info.getFilePath(), info.getLine())
                ));
                continue;
            }

            // ===== الحالة 2: invalid attribute access =====
            // إذا عرفنا نوع الكائن والـ attribute غير موجود فيه
            if (objType != null && !"unknown".equals(objType)) {
                java.util.Set<String> validAttrs = getValidAttributes(objType);
                if (validAttrs != null && !validAttrs.contains(attr)) {
                    errors.add(new SemanticError(
                            SemanticErrorType.INVALID_ATTRIBUTE_ACCESS,
                            "AttributeError",
                            "type object '" + objType + "' has no attribute '"
                                    + attr + "'",
                            info.getLine(),
                            info.getFileName(),
                            info.getObjectName() + "." + attr,
                            info.getObjectName() + "." + attr,
                            SourceFileReader.getLine(info.getFilePath(), info.getLine())
                    ));
                }
            }
        }
    }

    private java.util.Set<String> getValidAttributes(String type) {
        switch (type.toLowerCase()) {
            case "string":
            case "str":
                return STRING_ATTRS;
            case "list":
                return LIST_ATTRS;
            case "dict":
                return DICT_ATTRS;
            case "int":
                return INT_ATTRS;
            case "float":
                return FLOAT_ATTRS;
            default:
                return null; // غير معروف → لا نتحقق
        }
    }
}
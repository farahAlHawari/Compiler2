package semantic_errors;

public enum SemanticErrorType {
    MISSING_FLASK_VARIABLE,
    INVALID_FUNC_CALL,
    WRONG_ARGS_COUNT,
    RETURN_TYPE_MISMATCH,
    TYPE_MISMATCH,
    DIVISION_BY_ZERO,
    UNBOUND_LOCAL,
    USE_BEFORE_INIT,
    INVALID_ATTRIBUTE_ACCESS,
    OPERATION_ON_NONE,
    OPERATION_TYPE_ERROR,
    // ★ إضافة: كان SymbolTable.errorMessages منفصل تماماً عن هذا الـ enum،
    // فبوابة Main.java (اللي بتشيك semanticChecker.getErrors()) ما كانت
    // ترى أخطاء "already declared" إطلاقاً مهما كانت حقيقية.
    DUPLICATE_DECLARATION,
}

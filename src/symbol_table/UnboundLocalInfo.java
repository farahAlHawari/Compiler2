package symbol_table;

/**
 * يخزّن معلومات عن عملية augmented assignment محتملة أن تسبب UnboundLocalError.
 * يُجمع أثناء المشي على الـ AST بواسطة SymbolTableVisitor.
 * يُفحص لاحقاً بواسطة UnboundLocalErrorChecker.
 */
public class UnboundLocalInfo {

    private String variableName;      // اسم المتغير (مثلاً "x")
    private String operator;          // نوع العملية (مثلاً "+=", "-=", "*=")
    private int line;                 // رقم السطر
    private String fileName;          // اسم الملف
    private String filePath;          // المسار الكامل للملف
    private String scopeType;         // نوع السكوب الحالي (مثلاً "route_function")
    private String scopeContextName;  // اسم السكوب (مثلاً "foo")
    private boolean isInsideFunction; // هل نحن داخل دالة؟
    private boolean variableInCurrentScope;  // هل المتغير موجود في السكوب الحالي؟
    private boolean variableInOuterScope;   // هل المتغير موجود في سكوب خارجي؟
    private boolean declaredGlobal;          // هل استُخدمت كلمة global؟

    public UnboundLocalInfo(String variableName, String operator, int line,
                            String fileName, String filePath,
                            String scopeType, String scopeContextName,
                            boolean isInsideFunction,
                            boolean variableInCurrentScope,
                            boolean variableInOuterScope,
                            boolean declaredGlobal) {
        this.variableName = variableName;
        this.operator = operator;
        this.line = line;
        this.fileName = fileName;
        this.filePath = filePath;
        this.scopeType = scopeType;
        this.scopeContextName = scopeContextName;
        this.isInsideFunction = isInsideFunction;
        this.variableInCurrentScope = variableInCurrentScope;
        this.variableInOuterScope = variableInOuterScope;
        this.declaredGlobal = declaredGlobal;
    }

    // Getters
    public String getVariableName() { return variableName; }
    public String getOperator() { return operator; }
    public int getLine() { return line; }
    public String getFileName() { return fileName; }
    public String getFilePath() { return filePath; }
    public String getScopeType() { return scopeType; }
    public String getScopeContextName() { return scopeContextName; }
    public boolean isInsideFunction() { return isInsideFunction; }
    public boolean isVariableInCurrentScope() { return variableInCurrentScope; }
    public boolean isVariableInOuterScope() { return variableInOuterScope; }
    public boolean isDeclaredGlobal() { return declaredGlobal; }
}
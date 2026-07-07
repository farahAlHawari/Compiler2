package symbol_table;

/**
 * Represents a unary operation (-x or +x) found in Python code,
 * recorded so OperationTypeErrorChecker can verify the operand type
 * is numeric (int, float, bool).
 *
 * Example:
 *   s = "hello"
 *   -s    -> operator="-", operandType="string" -> ERROR
 */
public class UnaryOpTypeInfo {

    private String operator;       // "-" أو "+"
    private String operandType;    // نوع الـ operand
    private String operandDisplay; // اسم المتغير أو القيمة
    private int line;
    private String fileName = "";
    private String filePath = "";

    public UnaryOpTypeInfo(String operator, String operandType,
                           String operandDisplay, int line) {
        this.operator = operator;
        this.operandType = operandType;
        this.operandDisplay = operandDisplay;
        this.line = line;
    }

    public String getOperator() { return operator; }
    public String getOperandType() { return operandType; }
    public String getOperandDisplay() { return operandDisplay; }
    public int getLine() { return line; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
}
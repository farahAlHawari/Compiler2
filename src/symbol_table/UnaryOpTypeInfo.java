package symbol_table;


public class UnaryOpTypeInfo {

    private String operator;
    private String operandType;
    private String operandDisplay;
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
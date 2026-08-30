package symbol_table;


public class OperationTypeInfo {

    private String operator;
    private String leftType;
    private String rightType;
    private String leftOperand;
    private String rightOperand;
    private int line;
    private String fileName = "";
    private String filePath = "";

    public OperationTypeInfo(String operator, String leftType, String rightType,
                             String leftOperand, String rightOperand, int line) {
        this.operator = operator;
        this.leftType = leftType;
        this.rightType = rightType;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.line = line;
    }

    public String getOperator() { return operator; }
    public String getLeftType() { return leftType; }
    public String getRightType() { return rightType; }
    public String getLeftOperand() { return leftOperand; }
    public String getRightOperand() { return rightOperand; }
    public int getLine() { return line; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
}
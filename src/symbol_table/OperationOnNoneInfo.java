package symbol_table;

public class OperationOnNoneInfo {
    private String operator;
    private String leftOperandName;
    private String rightOperandName;
    private boolean leftIsNone;
    private boolean rightIsNone;
    private int line;
    private String fileName = "";
    private String filePath = "";
    private String otherOperandType;

    public OperationOnNoneInfo(String operator, String leftOperandName,
                               String rightOperandName, boolean leftIsNone,
                               boolean rightIsNone, int line) {
        this.operator = operator;
        this.leftOperandName = leftOperandName;
        this.rightOperandName = rightOperandName;
        this.leftIsNone = leftIsNone;
        this.rightIsNone = rightIsNone;
        this.line = line;
    }

    public String getOperator() { return operator; }
    public String getLeftOperandName() { return leftOperandName; }
    public String getRightOperandName() { return rightOperandName; }
    public boolean isLeftIsNone() { return leftIsNone; }
    public boolean isRightIsNone() { return rightIsNone; }
    public int getLine() { return line; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getOtherOperandType() { return otherOperandType; }
    public void setOtherOperandType(String otherOperandType) { this.otherOperandType = otherOperandType; }
}
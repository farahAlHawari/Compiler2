
package symbol_table;


public class DivisionInfo {

    private String operator;
    private boolean divisorIsLiteral;
    private String divisorLiteralValue;
    private String divisorVariableName;
    private int line;
    private String fileName = "";
    private String filePath = "";

    public DivisionInfo(String operator, boolean divisorIsLiteral, String divisorLiteralValue,
                        String divisorVariableName, int line) {
        this.operator = operator;
        this.divisorIsLiteral = divisorIsLiteral;
        this.divisorLiteralValue = divisorLiteralValue;
        this.divisorVariableName = divisorVariableName;
        this.line = line;
    }

    public String getOperator() {
        return operator;
    }

    public boolean isDivisorLiteral() {
        return divisorIsLiteral;
    }

    public String getDivisorLiteralValue() {
        return divisorLiteralValue;
    }

    public String getDivisorVariableName() {
        return divisorVariableName;
    }

    public int getLine() {
        return line;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
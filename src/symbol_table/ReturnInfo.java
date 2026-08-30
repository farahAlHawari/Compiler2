package symbol_table;


public class ReturnInfo {

    private String enclosingFunctionName;
    private String returnExprType;
    private int line;
    private String fileName = "";
    private String filePath = "";
    public ReturnInfo(String enclosingFunctionName, String returnExprType, int line) {
        this.enclosingFunctionName = enclosingFunctionName;
        this.returnExprType = returnExprType;
        this.line = line;
    }

    public String getEnclosingFunctionName() {
        return enclosingFunctionName;
    }

    public void setEnclosingFunctionName(String enclosingFunctionName) {
        this.enclosingFunctionName = enclosingFunctionName;
    }

    public String getReturnExprType() {
        return returnExprType;
    }

    public void setReturnExprType(String returnExprType) {
        this.returnExprType = returnExprType;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    @Override
    public String toString() {
        return "ReturnInfo{" +
                "enclosingFunctionName='" + enclosingFunctionName + '\'' +
                ", returnExprType='" + returnExprType + '\'' +
                ", line=" + line +
                '}';
    }
}
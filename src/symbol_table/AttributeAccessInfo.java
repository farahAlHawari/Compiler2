package symbol_table;

public class AttributeAccessInfo {
    private String objectName;       // اسم الكائن (مثلاً "user")
    private String attributeName;    // اسم الـ attribute (مثلاً "age")
    private int line;
    private String fileName = "";
    private String filePath = "";
    private String objectType;       // نوع الكائن من الـ SymbolTable (مثلاً "none", "string")
    private String objectValue;      // قيمة الكائن (مثلاً "None")

    public AttributeAccessInfo(String objectName, String attributeName, int line) {
        this.objectName = objectName;
        this.attributeName = attributeName;
        this.line = line;
    }

    // Getters and Setters
    public String getObjectName() { return objectName; }
    public String getAttributeName() { return attributeName; }
    public int getLine() { return line; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
    public String getObjectType() { return objectType; }
    public void setObjectType(String objectType) { this.objectType = objectType; }
    public String getObjectValue() { return objectValue; }
    public void setObjectValue(String objectValue) { this.objectValue = objectValue; }
}
package symbol_table;

public class JinjaFilterUsage {
    private String variableName;
    private String filterName;      // اسم الفلتر (upper, join...) أو null للـ for-loop
    private String usageContext;    // "filter" أو "for_loop"
    private int line;
    private String fileName = "";
    private String filePath = "";
    public JinjaFilterUsage(String variableName, String filterName, String usageContext, int line) {
        this.variableName = variableName;
        this.filterName = filterName;
        this.usageContext = usageContext;
        this.line = line;
    }

    public String getVariableName() { return variableName; }
    public String getFilterName() { return filterName; }
    public String getUsageContext() { return usageContext; }
    public int getLine() { return line; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
}
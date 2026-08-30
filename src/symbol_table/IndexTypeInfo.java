package symbol_table;


public class IndexTypeInfo {

    private String containerType;
    private String indexType;
    private String containerDisplay;
    private String indexDisplay;
    private int line;
    private String fileName = "";
    private String filePath = "";

    public IndexTypeInfo(String containerType, String indexType,
                         String containerDisplay, String indexDisplay, int line) {
        this.containerType = containerType;
        this.indexType = indexType;
        this.containerDisplay = containerDisplay;
        this.indexDisplay = indexDisplay;
        this.line = line;
    }

    public String getContainerType() { return containerType; }
    public String getIndexType() { return indexType; }
    public String getContainerDisplay() { return containerDisplay; }
    public String getIndexDisplay() { return indexDisplay; }
    public int getLine() { return line; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }
}
package symbol_table;

/**
 * Represents a single indexing operation (x[i]) found in Python code,
 * recorded so OperationTypeErrorChecker can verify:
 *   1. The container type actually supports indexing (int/float/bool/None don't)
 *   2. The index type matches what the container expects
 *      (list/string need int; dict accepts any hashable key)
 *
 * Example:
 *   x = 5
 *   x[0]   -> containerType="int", indexType="int" -> ERROR (not subscriptable)
 *
 *   products = [1,2,3]
 *   products["a"]  -> containerType="list", indexType="string" -> ERROR (wrong index type)
 */
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
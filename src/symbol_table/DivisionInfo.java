package symbol_table;

/**
 * Represents a single division or modulo operation found in Python code,
 * recorded so DivisionByZeroChecker can later decide whether the divisor
 * is zero (or definitely zero), mirroring Python's real ZeroDivisionError.
 *
 * Covers cases 1, 2 and 3 from the spec (the Python side):
 *   1. x / 0            -> direct literal zero divisor
 *   2. y = 0; x / y      -> divisor is a variable whose value is 0
 *   3. x % 0             -> modulo, same rules as division
 *
 * (Case 4, the Jinja Bridge case "{{ count / price }}" with price=0 passed
 *  from Flask, is tracked separately via JinjaFilterUsage-style records
 *  because it comes from the template's raw text, not this Python AST.)
 *
 * Example:
 *   x = 10 / 0
 *   -> operator = "/"
 *   -> divisorIsLiteral = true
 *   -> divisorLiteralValue = "0"
 *   -> divisorVariableName = null
 *   -> line = 4
 *
 *   y = 0
 *   x = 10 / y
 *   -> operator = "/"
 *   -> divisorIsLiteral = false
 *   -> divisorLiteralValue = null
 *   -> divisorVariableName = "y"
 *   -> line = 6
 */
public class DivisionInfo {

    private String operator;             // "/" or "%"
    private boolean divisorIsLiteral;    // true if divisor is a literal number (e.g. 0), false if it's a variable
    private String divisorLiteralValue;  // the literal text of the divisor, e.g. "0" (null if divisorIsLiteral is false)
    private String divisorVariableName;  // the variable name used as divisor, e.g. "y" (null if divisorIsLiteral is true)
    private int line;
    private String fileName = "";
    private String filePath = "";

    public DivisionInfo(String operator, String divisorLiteralValue,
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
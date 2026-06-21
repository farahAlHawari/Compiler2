//package semantic_errors;
//
//public class SemanticError {
//        private SemanticErrorType errorType;
//        private String errorName;
//        private String errorDescription;
//        private int errorLine;
//
//        public SemanticError(SemanticErrorType errorType, String errorName, String errorDescription, int errorLine) {
//            this.errorType = errorType;
//            this.errorName = errorName;
//            this.errorDescription = errorDescription;
//            this.errorLine = errorLine;
//        }
//
//        public SemanticErrorType getErrorType() {
//            return errorType;
//        }
//
//        public String getErrorName() {
//            return errorName;
//        }
//
//        public String getErrorDescription() {
//            return errorDescription;
//        }
//
//        public int getErrorLine() {
//            return errorLine;
//        }
//
//        @Override
//        public String toString() {
//            return "Error Name : " + errorName +
//                    " , Error Description : " + errorDescription +
//                    " , Error Location : " + errorLine;
//        }
//}
package semantic_errors;

public class SemanticError {
    private SemanticErrorType errorType;
    private String errorName;
    private String message;
    private int errorLine;
    private String fileName = "";
    private String variable = "";
    private String expression = "";
    private String codeSnippet = "";

    // Constructor قديم (للتوافق مع الكود الموجود)
    public SemanticError(SemanticErrorType errorType, String errorName,
                         String errorDescription, int errorLine) {
        this.errorType = errorType;
        this.errorName = errorName;
        this.message = errorDescription;
        this.errorLine = errorLine;
    }

    // Constructor جديد (كل الحقول)
    public SemanticError(SemanticErrorType errorType, String errorName,
                         String message, int errorLine, String fileName,
                         String variable, String expression, String codeSnippet) {
        this.errorType = errorType;
        this.errorName = errorName;
        this.message = message;
        this.errorLine = errorLine;
        this.fileName = fileName != null ? fileName : "";
        this.variable = variable != null ? variable : "";
        this.expression = expression != null ? expression : "";
        this.codeSnippet = codeSnippet != null ? codeSnippet : "";
    }

    // Getters
    public SemanticErrorType getErrorType() { return errorType; }
    public String getErrorName() { return errorName; }
    public String getMessage() { return message; }
    public int getErrorLine() { return errorLine; }
    public String getFileName() { return fileName; }
    public String getVariable() { return variable; }
    public String getExpression() { return expression; }
    public String getCodeSnippet() { return codeSnippet; }

    /**
     * يطبع سطر واحد للموضع القديم (إن لزم)
     */
    public String toSingleLine() {
        return "Error Name : " + errorName +
                " , Message : " + message +
                " , Error Location : " + errorLine;
    }

    /**
     * يطبع الخطأ بالشكل الجديد المتعدد الأسطر مع index
     */
    public String toReportString(int index) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(index).append("] ").append(errorName).append("\n");
        if (!fileName.isEmpty()) {
            sb.append("File      : ").append(fileName).append("\n");
        }
        sb.append("Line      : ").append(errorLine).append("\n");
        if (!variable.isEmpty()) {
            sb.append("Variable  : ").append(variable).append("\n");
        }
        if (!expression.isEmpty()) {
            sb.append("Expression: ").append(expression).append("\n");
        }
        if (!codeSnippet.isEmpty()) {
            sb.append("Code      : ").append(codeSnippet).append("\n");
        }
        sb.append("Message   : ").append(message).append("\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return toSingleLine();
    }
}
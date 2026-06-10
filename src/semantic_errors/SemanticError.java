package semantic_errors;

public class SemanticError {
        private SemanticErrorType errorType;
        private String errorName;
        private String errorDescription;
        private int errorLine;

        public SemanticError(SemanticErrorType errorType, String errorName, String errorDescription, int errorLine) {
            this.errorType = errorType;
            this.errorName = errorName;
            this.errorDescription = errorDescription;
            this.errorLine = errorLine;
        }

        public SemanticErrorType getErrorType() {
            return errorType;
        }

        public String getErrorName() {
            return errorName;
        }

        public String getErrorDescription() {
            return errorDescription;
        }

        public int getErrorLine() {
            return errorLine;
        }

        @Override
        public String toString() {
            return "Error Name : " + errorName +
                    " , Error Description : " + errorDescription +
                    " , Error Location : " + errorLine;
        }
}

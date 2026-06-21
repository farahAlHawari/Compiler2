//package main.pythoncompiler.ast;
//
//
//public class AssignNode extends ASTNode {
//    public String variableName;
//    public String operator;
//
//    public AssignNode(String variableName, String operator) {
//        super("Assignment");
//        this.variableName = variableName;
//        this.operator = operator;
//    }
//
//    @Override
//    public String getDetails() {
//        return " (" + variableName + " " + operator + " .. )";
//    }
//}
package main.pythoncompiler.ast;


public class AssignNode extends ASTNode {
    public String variableName;
    public String operator;
    public String declaredType ;  // NEW: type hint (مثلاً "int" من `c: int = "hello"`)

    public AssignNode(String variableName, String operator) {
        super("Assignment");
        this.variableName = variableName;
        this.operator = operator;
        this.declaredType = null;
    }

    @Override
    public String getDetails() {
        return " (" + variableName + " " + operator + " .. )";
    }
}
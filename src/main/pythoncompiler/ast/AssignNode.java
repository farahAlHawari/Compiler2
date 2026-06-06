package main.pythoncompiler.ast;


public class AssignNode extends ASTNode {
    public String variableName;
    public String operator;

    public AssignNode(String variableName, String operator) {
        super("Assignment");
        this.variableName = variableName;
        this.operator = operator;
    }

    @Override
    public String getDetails() {
        return " (" + variableName + " " + operator + " .. )";
    }
}

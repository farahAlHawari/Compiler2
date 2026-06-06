package main.pythoncompiler.ast;

public class ClassDefNode  extends ASTNode{
    public String className;

    public ClassDefNode(String className) {
        super("ClassDef");
        this.className = className;
    }

    @Override
    public String getDetails() {
        return " (Name: " + className + ")";
    }
}

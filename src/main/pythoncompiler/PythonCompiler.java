package main.pythoncompiler;


import main.pythoncompiler.grammer.main.pythoncompiler.grammer.PythonLexer;
import main.pythoncompiler.grammer.main.pythoncompiler.grammer.PythonParser;
import main.pythoncompiler.visitor.ASTBuilderVisitor;
import main.pythoncompiler.visitor.SymbolTableVisitor;
import symbol_table.SymbolTable;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import main.pythoncompiler.ast.ASTNode;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PythonCompiler {

    private SymbolTable symbolTable;
    private ASTNode astRoot;

    public PythonCompiler(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
    }

    public ASTNode getAST() {
        return this.astRoot;
    }

    public void compile(String filePath) throws Exception {

        String code = Files.readString(Path.of(filePath));
        CharStream input = CharStreams.fromString(code);

        PythonLexer lexer = new PythonLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        PythonParser parser = new PythonParser(tokens);
        ParseTree tree = parser.program();

        System.out.println(" Parsing successful");

        System.out.println(" Building AST...");
        ASTBuilderVisitor visitor = new ASTBuilderVisitor();
        ASTNode root = visitor.visit(tree);

        this.astRoot = root;

        if (root != null) {
            System.out.println(" AST Structure");
            System.out.println(root.toStringTree(""));

            // Build Symbol Table for Python
            symbolTable.setSource("python");
            SymbolTableVisitor symTableVisitor = new SymbolTableVisitor(symbolTable);
            symTableVisitor.visit(root);

            List<String> errors = symTableVisitor.getErrors();
            if (!errors.isEmpty()) {
                System.out.println("\n Python Semantic Warnings:");
                for (String err : errors) {
                    System.out.println(err);
                }
            } else {
                System.out.println("\nNo Python Semantic Errors found");
            }

        } else {
            System.out.println(" AST Generation failed");
        }

        //System.out.println(tree.toStringTree(parser));
    }
}
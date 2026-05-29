//import AST.Core.PageNode;
//import Visitor.HtmlCssJinjaVisitor;
//import Visitor.SymbolTableVisitor;
//import antlr.TemplateLexer;
//import antlr.TemplateParser;
//import main.pythoncompiler.PythonCompiler;
//import org.antlr.v4.runtime.CharStream;
//import org.antlr.v4.runtime.CharStreams;
//import org.antlr.v4.runtime.CommonTokenStream;
//import org.antlr.v4.runtime.tree.ParseTree;
//import org.antlr.v4.runtime.tree.Trees;
//
//import java.io.IOException;
//
//public class Main {
//    public static void main(String[] args) throws IOException, Exception {
//
//        //  Python Compiler
//        PythonCompiler compiler = new PythonCompiler();
//        compiler.compile("src/tests/app1.py");
//
//        //  HTML/CSS/Jinja Parser
//        //
//        String source = "tests/index.html";
//
//        CharStream charStream = CharStreams.fromFileName(source);
//
//        // Lexer
//        TemplateLexer lexer = new TemplateLexer(charStream);
//
//        // Tokens
//        CommonTokenStream tokens = new CommonTokenStream(lexer);
//
//        // Parser
//        TemplateParser parser = new TemplateParser(tokens);
//
//        ParseTree tree = parser.page();
//
//        // Parse Tree
//        System.out.println("---------- PARSE TREE ---------- ");
//        System.out.println(Trees.toStringTree(tree, parser));
//
//        // AST
//        HtmlCssJinjaVisitor visitor = new HtmlCssJinjaVisitor();
//        PageNode ast = (PageNode) visitor.visit(tree);
//
//        System.out.println("\n ----------- AST ----------");
//        ast.print("");
//
//        //  Semantic Analysis
//        SymbolTableVisitor semanticAnalyzer = new SymbolTableVisitor();
//        semanticAnalyzer.visit(tree);
//        semanticAnalyzer.getSymbolTable().printSymbolTable();
//    }
//}

import AST.Core.PageNode;
import Visitor.HtmlCssJinjaVisitor;
import Visitor.TemplateSymbolTableVisitor;
import antlr.TemplateLexer;
import antlr.TemplateParser;
import main.pythoncompiler.PythonCompiler;
import symbol_table.SymbolTable;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Trees;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, Exception {

        // Create ONE shared Symbol Table
        SymbolTable symbolTable = new SymbolTable();

        // ===== Python Compiler =====
        PythonCompiler compiler = new PythonCompiler(symbolTable);
        compiler.compile("src/tests/app2.py");

        // ===== HTML/CSS/Jinja Parser =====
        String source = "tests/base.html";

        CharStream charStream = CharStreams.fromFileName(source);

        // Lexer
        TemplateLexer lexer = new TemplateLexer(charStream);

        // Tokens
        CommonTokenStream tokens = new CommonTokenStream(lexer);

        // Parser
        TemplateParser parser = new TemplateParser(tokens);

        ParseTree tree = parser.page();

        // Parse Tree
        System.out.println("\n---------- PARSE TREE ---------- ");
        System.out.println(Trees.toStringTree(tree, parser));

        // AST
        HtmlCssJinjaVisitor visitor = new HtmlCssJinjaVisitor();
        PageNode ast = (PageNode) visitor.visit(tree);

        System.out.println("\n ----------- AST ----------");
        ast.print("");

        // Build Symbol Table for Template
        symbolTable.setSource("template");
        TemplateSymbolTableVisitor templateVisitor = new TemplateSymbolTableVisitor(symbolTable);
        templateVisitor.visit(ast);

        // [تعديل 7] طباعة الجدول + طباعة هيكل السكوبات
        // Print the Unified Symbol Table (one table after both trees)
        symbolTable.printSymbolTable();

        // [إضافة جديدة] طباعة هيكل السكوبات بشكل شجري - مفيد للعرض
        symbolTable.printScopeStructure();
    }
}

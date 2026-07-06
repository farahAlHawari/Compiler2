import AST.Core.PageNode;
import Visitor.HtmlCssJinjaVisitor;
import Visitor.TemplateSymbolTableVisitor;
import antlr.TemplateLexer;
import antlr.TemplateParser;
import main.pythoncompiler.PythonCompiler;
import symbol_table.SymbolTable;
import semantic_errors.SemanticChecker;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.util.List;

public class Main {

    /**
     * يشغّل الـ compiler على ملف Python + HTML واحد
     * ويطبع الـ Symbol Table + الأخطاء الدلالية
     */
    public static void compileAndCheck(String pythonFile, String htmlFile,
                                       String outputTitle) throws Exception {
        System.out.println("\n");
        System.out.println("*".repeat(80));
        System.out.println("  " + outputTitle);
        System.out.println("  Python: " + pythonFile);
        System.out.println("  HTML  : " + htmlFile);
        System.out.println("*".repeat(80));

        // Create ONE shared Symbol Table
        SymbolTable symbolTable = new SymbolTable();

        // ===== Python Compiler =====
        String pythonFileName = new java.io.File(pythonFile).getName();
        symbolTable.setCurrentFileName(pythonFileName);
        symbolTable.setCurrentFilePath(pythonFile);

        try {
            PythonCompiler compiler = new PythonCompiler(symbolTable);
            compiler.compile(pythonFile);
        } catch (Exception e) {
            System.out.println("  [Python] Compilation error: " + e.getMessage());
        }

        // ===== HTML/CSS/Jinja Parser =====
        String templateFileName = new java.io.File(htmlFile).getName();
        symbolTable.setSource("template");
        symbolTable.setCurrentFileName(templateFileName);
        symbolTable.setCurrentFilePath(htmlFile);

        try {
            CharStream charStream = CharStreams.fromFileName(htmlFile);
            TemplateLexer lexer = new TemplateLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            TemplateParser parser = new TemplateParser(tokens);
            ParseTree tree = parser.page();

            // AST
            HtmlCssJinjaVisitor visitor = new HtmlCssJinjaVisitor();
            PageNode ast = (PageNode) visitor.visit(tree);

            // Build Symbol Table for Template
            symbolTable.setSource("template");
            TemplateSymbolTableVisitor templateVisitor =
                    new TemplateSymbolTableVisitor(symbolTable);
            templateVisitor.visit(ast);
        } catch (Exception e) {
            System.out.println("  [Template] Parsing error: " + e.getMessage());
        }

        // ===== Print Symbol Table =====
        symbolTable.printSymbolTable();
        symbolTable.printScopeStructure();

        // ===== Semantic Error Checking =====
        SemanticChecker semanticChecker = new SemanticChecker(symbolTable);
        semanticChecker.checkErrors();
        semanticChecker.printErrors();

        // ===== Write to file =====
        String outputFile = "Result/" + outputTitle
                .replace(" ", "_").replace(":", "") + ".txt";
        semanticChecker.writeErrorsToFile(outputFile);

        // ===== Summary =====
        List<semantic_errors.SemanticError> errors = semanticChecker.getErrors();
        System.out.println("\n  Total errors found: " + errors.size());
        if (!errors.isEmpty()) {
            System.out.println("  Error breakdown:");
            java.util.Map<String, Integer> breakdown = new java.util.LinkedHashMap<>();
            for (semantic_errors.SemanticError err : errors) {
                String name = err.getErrorName();
                breakdown.merge(name, 1, Integer::sum);
            }
            for (var entry : breakdown.entrySet()) {
                System.out.println("    - " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    /**
     * نسخة مبسّطة: تشغّل الـ compiler على ملف Python فقط
     * بدون HTML (تستخدم HTML فارغ مؤقت)
     */
    public static void compilePythonOnly(String pythonFile,
                                         String outputTitle) throws Exception {
        // ننشئ ملف HTML مؤقت بسيط بنفس اسم الملف
        String htmlFile = pythonFile.replace(".py", ".html");

        System.out.println("\n");
        System.out.println("*".repeat(80));
        System.out.println("  " + outputTitle);
        System.out.println("  Python: " + pythonFile);
        System.out.println("  HTML  : " + htmlFile + " (companion)");
        System.out.println("*".repeat(80));

        SymbolTable symbolTable = new SymbolTable();

        // ===== Python Compiler =====
        String pythonFileName = new java.io.File(pythonFile).getName();
        symbolTable.setCurrentFileName(pythonFileName);
        symbolTable.setCurrentFilePath(pythonFile);

        try {
            PythonCompiler compiler = new PythonCompiler(symbolTable);
            compiler.compile(pythonFile);
        } catch (Exception e) {
            System.out.println("  [Python] Compilation error: " + e.getMessage());
        }

        // ===== HTML/CSS/Jinja Parser =====
        try {
            CharStream charStream = CharStreams.fromFileName(htmlFile);
            TemplateLexer lexer = new TemplateLexer(charStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            TemplateParser parser = new TemplateParser(tokens);
            ParseTree tree = parser.page();

            HtmlCssJinjaVisitor visitor = new HtmlCssJinjaVisitor();
            PageNode ast = (PageNode) visitor.visit(tree);

            symbolTable.setSource("template");
            TemplateSymbolTableVisitor templateVisitor =
                    new TemplateSymbolTableVisitor(symbolTable);
            templateVisitor.visit(ast);
        } catch (Exception e) {
            System.out.println("  [Template] Parsing error: " + e.getMessage());
        }

        // ===== Print Symbol Table =====
        symbolTable.printSymbolTable();
        symbolTable.printScopeStructure();

        // ===== Semantic Error Checking =====
        SemanticChecker semanticChecker = new SemanticChecker(symbolTable);
        semanticChecker.checkErrors();
        semanticChecker.printErrors();

        // ===== Summary =====
        List<semantic_errors.SemanticError> errors = semanticChecker.getErrors();
        System.out.println("\n  Total errors found: " + errors.size());
        if (!errors.isEmpty()) {
            System.out.println("  Error breakdown:");
            java.util.Map<String, Integer> breakdown = new java.util.LinkedHashMap<>();
            for (semantic_errors.SemanticError err : errors) {
                String name = err.getErrorName();
                breakdown.merge(name, 1, Integer::sum);
            }
            for (var entry : breakdown.entrySet()) {
                System.out.println("    - " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    public static void main(String[] args) throws Exception {

        // ==============================================================
        //  TEST 1: NameError — متغير غير معرّف
        //  يتوقع: NameError لـ x, z, age
        // ==============================================================
        compilePythonOnly(
                "src/tests/test_undefined.py",
                "TEST 1: NameError (undefined variable)"
        );

        // ==============================================================
        //  TEST 2: AttributeError — وصول لـ attribute غير موجود
        //  يتوقع: AttributeError لـ text.append, items.split, user.upper, num.keys
        // ==============================================================
        compilePythonOnly(
                "src/tests/test_invalid_attr.py",
                "TEST 2: AttributeError (invalid attribute access)"
        );

        // ==============================================================
        //  TEST 3: TypeError — عملية حسابية على None
        //  يتوقع: TypeError لـ x+5, 10*x, x-y, x/2, x**2
        // ==============================================================
        compilePythonOnly(
                "src/tests/test_operation_on_none.py",
                "TEST 3: TypeError (operation on None)"
        );

        // ==============================================================
        //  TEST 4: AttributeError على NoneType
        //  يتوقع: AttributeError لـ user.name, data.get, items.append, text.upper
        // ==============================================================
        compilePythonOnly(
                "src/tests/test_none_attr.py",
                "TEST 4: AttributeError on NoneType"
        );

        // ==============================================================
        //  TEST: Type Error
        //  يتوقع: TypeError لكل الحالات الـ 24
        // ==============================================================
        compilePythonOnly(
                "src/tests/test_type_error.py",
                "TEST: Type Error"
        );

        // ==============================================================
        //  TEST 5: Error Type — عملية بين نوعين غير متوافقين
        //  يتوقع: 12 TypeError (عمليات حسابية/مقارنة/فهرسة/len)
        //  + 3 حالات صحيحة ما لازم تنكشف كأخطاء
        // ==============================================================
        compilePythonOnly(
                "src/tests/test_operation_type_error.py",
                "TEST 5: Error Type (incompatible operand types)"
        );

    }
}
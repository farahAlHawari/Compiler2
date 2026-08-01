//import AST.Core.PageNode;
//import Visitor.HtmlCssJinjaVisitor;
//import Visitor.TemplateSymbolTableVisitor;
//import antlr.TemplateLexer;
//import antlr.TemplateParser;
//import main.pythoncompiler.PythonCompiler;
//import symbol_table.SymbolTable;
//import semantic_errors.SemanticChecker;
//import generation.GenerationContext;
//import generation.ContextBuilder;
//import org.antlr.v4.runtime.CharStream;
//import org.antlr.v4.runtime.CharStreams;
//import org.antlr.v4.runtime.CommonTokenStream;
//import org.antlr.v4.runtime.tree.ParseTree;
//
//import java.util.List;
//
//public class Main {
//
//    /**
//     * يشغّل الـ compiler على ملف Python + HTML واحد
//     * ويطبع الـ Symbol Table + الأخطاء الدلالية
//     */
//    public static void compileAndCheck(String pythonFile, String htmlFile,
//                                       String outputTitle) throws Exception {
//        System.out.println("\n");
//        System.out.println("*".repeat(80));
//        System.out.println("  " + outputTitle);
//        System.out.println("  Python: " + pythonFile);
//        System.out.println("  HTML  : " + htmlFile);
//        System.out.println("*".repeat(80));
//
//        // Create ONE shared Symbol Table
//        SymbolTable symbolTable = new SymbolTable();
//
//        // ===== Python Compiler =====
//        String pythonFileName = new java.io.File(pythonFile).getName();
//        symbolTable.setCurrentFileName(pythonFileName);
//        symbolTable.setCurrentFilePath(pythonFile);
//
//        try {
//            PythonCompiler compiler = new PythonCompiler(symbolTable);
//            compiler.compile(pythonFile);
//        } catch (Exception e) {
//            System.out.println("  [Python] Compilation error: " + e.getMessage());
//        }
//
//        // ===== HTML/CSS/Jinja Parser =====
//        String templateFileName = new java.io.File(htmlFile).getName();
//        symbolTable.setSource("template");
//        symbolTable.setCurrentFileName(templateFileName);
//        symbolTable.setCurrentFilePath(htmlFile);
//
//        try {
//            CharStream charStream = CharStreams.fromFileName(htmlFile);
//            TemplateLexer lexer = new TemplateLexer(charStream);
//            CommonTokenStream tokens = new CommonTokenStream(lexer);
//            TemplateParser parser = new TemplateParser(tokens);
//            ParseTree tree = parser.page();
//
//            // AST
//            HtmlCssJinjaVisitor visitor = new HtmlCssJinjaVisitor();
//            PageNode ast = (PageNode) visitor.visit(tree);
//
//            // Build Symbol Table for Template
//            symbolTable.setSource("template");
//            TemplateSymbolTableVisitor templateVisitor =
//                    new TemplateSymbolTableVisitor(symbolTable);
//            templateVisitor.visit(ast);
//        } catch (Exception e) {
//            System.out.println("  [Template] Parsing error: " + e.getMessage());
//        }
//
//        // ===== Print Symbol Table =====
//        symbolTable.printSymbolTable();
//        symbolTable.printScopeStructure();
//
//        // ===== Semantic Error Checking =====
//        SemanticChecker semanticChecker = new SemanticChecker(symbolTable);
//        semanticChecker.checkErrors();
//        semanticChecker.printErrors();
//
//        // ===== Write to file =====
//        String outputFile = "Result/" + outputTitle
//                .replace(" ", "_").replace(":", "") + ".txt";
//        semanticChecker.writeErrorsToFile(outputFile);
//
//        // ===== Summary =====
//        List<semantic_errors.SemanticError> errors = semanticChecker.getErrors();
//        System.out.println("\n  Total errors found: " + errors.size());
//        if (!errors.isEmpty()) {
//            System.out.println("  Error breakdown:");
//            java.util.Map<String, Integer> breakdown = new java.util.LinkedHashMap<>();
//            for (semantic_errors.SemanticError err : errors) {
//                String name = err.getErrorName();
//                breakdown.merge(name, 1, Integer::sum);
//            }
//            for (var entry : breakdown.entrySet()) {
//                System.out.println("    - " + entry.getKey() + ": " + entry.getValue());
//            }
//        }
//    }
//
//    /**
//     * نسخة مبسّطة: تشغّل الـ compiler على ملف Python فقط
//     * بدون HTML (تستخدم HTML فارغ مؤقت)
//     */
//    /**
//     * يشغّل الـ compiler على ملف Python + HTML واحد
//     * ثم يشغّل مرحلة Code Generation إذا لم توجد أخطاء دلالية
//     */
//    public static void compileAndGenerate(String pythonFile, String htmlFile,
//                                          String outputTitle) throws Exception {
//        System.out.println("\n");
//        System.out.println("*".repeat(80));
//        System.out.println("  " + outputTitle);
//        System.out.println("  Python: " + pythonFile);
//        System.out.println("  HTML  : " + htmlFile);
//        System.out.println("*".repeat(80));
//
//        SymbolTable symbolTable = new SymbolTable();
//
//        // ===== Python Compiler =====
//        String pythonFileName = new java.io.File(pythonFile).getName();
//        symbolTable.setCurrentFileName(pythonFileName);
//        symbolTable.setCurrentFilePath(pythonFile);
//
//        try {
//            PythonCompiler compiler = new PythonCompiler(symbolTable);
//            compiler.compile(pythonFile);
//        } catch (Exception e) {
//            System.out.println("  [Python] Compilation error: " + e.getMessage());
//        }
//
//        // ===== HTML/CSS/Jinja Parser =====
//        String templateFileName = new java.io.File(htmlFile).getName();
//        symbolTable.setSource("template");
//        symbolTable.setCurrentFileName(templateFileName);
//        symbolTable.setCurrentFilePath(htmlFile);
//
//        PageNode ast = null;
//        try {
//            CharStream charStream = CharStreams.fromFileName(htmlFile);
//            TemplateLexer lexer = new TemplateLexer(charStream);
//            CommonTokenStream tokens = new CommonTokenStream(lexer);
//            TemplateParser parser = new TemplateParser(tokens);
//            ParseTree tree = parser.page();
//
//            HtmlCssJinjaVisitor visitor = new HtmlCssJinjaVisitor();
//            ast = (PageNode) visitor.visit(tree);
//
//            symbolTable.setSource("template");
//            TemplateSymbolTableVisitor templateVisitor =
//                    new TemplateSymbolTableVisitor(symbolTable);
//            templateVisitor.visit(ast);
//        } catch (Exception e) {
//            System.out.println("  [Template] Parsing error: " + e.getMessage());
//        }
//
//        // ===== Print Symbol Table =====
//        symbolTable.printSymbolTable();
//        symbolTable.printScopeStructure();
//
//        // ===== Semantic Error Checking =====
//        SemanticChecker semanticChecker = new SemanticChecker(symbolTable);
//        semanticChecker.checkErrors();
//        semanticChecker.printErrors();
//
//        // ===== Write to file =====
//        String outputFile = "Result/" + outputTitle
//                .replace(" ", "_").replace(":", "") + ".txt";
//        semanticChecker.writeErrorsToFile(outputFile);
//
//        // ===== Summary =====
//        List<semantic_errors.SemanticError> errors = semanticChecker.getErrors();
//        System.out.println("\n  Total errors found: " + errors.size());
//
//        // ===== ★ Code Generation Phase ★ =====
//        if (errors.isEmpty()) {
//            System.out.println("\n  No semantic errors — Starting Code Generation phase...");
//
//            // ★ بناء GenerationContext من SymbolTable ★
//            // ContextBuilder يأخذ SymbolTable في الكونستراكتور و build() بدون args
//            // وي parsing كل التمبلات تلقائياً من مجلد tests/
//            ContextBuilder contextBuilder = new ContextBuilder(symbolTable);
//            GenerationContext generationContext = contextBuilder.build();
//
//            // ★ طباعة Context لtesting ★
//            generationContext.printContext();
//
//            System.out.println("\n  Generation phase setup completed.");
//            System.out.println("  NOTE: Full Generation requires Person 2, 3, 4 classes.");
//            System.out.println("  Current output: GenerationContext with extracted data.");
//
//        } else {
//            System.out.println("\n  Cannot start Generation — Semantic errors exist.");
//            System.out.println("  Fix semantic errors first, then retry generation.");
//        }
//    }
//
//    public static void compilePythonOnly(String pythonFile,
//                                         String outputTitle) throws Exception {
//        // ننشئ ملف HTML مؤقت بسيط بنفس اسم الملف
//        String htmlFile = pythonFile.replace(".py", ".html");
//
//        System.out.println("\n");
//        System.out.println("*".repeat(80));
//        System.out.println("  " + outputTitle);
//        System.out.println("  Python: " + pythonFile);
//        System.out.println("  HTML  : " + htmlFile + " (companion)");
//        System.out.println("*".repeat(80));
//
//        SymbolTable symbolTable = new SymbolTable();
//
//        // ===== Python Compiler =====
//        String pythonFileName = new java.io.File(pythonFile).getName();
//        symbolTable.setCurrentFileName(pythonFileName);
//        symbolTable.setCurrentFilePath(pythonFile);
//
//        try {
//            PythonCompiler compiler = new PythonCompiler(symbolTable);
//            compiler.compile(pythonFile);
//        } catch (Exception e) {
//            System.out.println("  [Python] Compilation error: " + e.getMessage());
//        }
//
//        // ===== HTML/CSS/Jinja Parser =====
//        try {
//            CharStream charStream = CharStreams.fromFileName(htmlFile);
//            TemplateLexer lexer = new TemplateLexer(charStream);
//            CommonTokenStream tokens = new CommonTokenStream(lexer);
//            TemplateParser parser = new TemplateParser(tokens);
//            ParseTree tree = parser.page();
//
//            HtmlCssJinjaVisitor visitor = new HtmlCssJinjaVisitor();
//            PageNode ast = (PageNode) visitor.visit(tree);
//
//            symbolTable.setSource("template");
//            TemplateSymbolTableVisitor templateVisitor =
//                    new TemplateSymbolTableVisitor(symbolTable);
//            templateVisitor.visit(ast);
//        } catch (Exception e) {
//            System.out.println("  [Template] Parsing error: " + e.getMessage());
//        }
//
//        // ===== Print Symbol Table =====
//        symbolTable.printSymbolTable();
//        symbolTable.printScopeStructure();
//
//        // ===== Semantic Error Checking =====
//        SemanticChecker semanticChecker = new SemanticChecker(symbolTable);
//        semanticChecker.checkErrors();
//        semanticChecker.printErrors();
//
//        // ===== Summary =====
//        List<semantic_errors.SemanticError> errors = semanticChecker.getErrors();
//        System.out.println("\n  Total errors found: " + errors.size());
//        if (!errors.isEmpty()) {
//            System.out.println("  Error breakdown:");
//            java.util.Map<String, Integer> breakdown = new java.util.LinkedHashMap<>();
//            for (semantic_errors.SemanticError err : errors) {
//                String name = err.getErrorName();
//                breakdown.merge(name, 1, Integer::sum);
//            }
//            for (var entry : breakdown.entrySet()) {
//                System.out.println("    - " + entry.getKey() + ": " + entry.getValue());
//            }
//        }
//    }
//
//    public static void main(String[] args) throws Exception {
//        compileAndGenerate(
//                "src/tests/app1.py",
//                "tests",
//                "TEST 6: Code Generation (app1.py)"
//        );
////        // ==============================================================
////        //  TEST 1: NameError — متغير غير معرّف
////        //  يتوقع: NameError لـ x, z, age
////        // ==============================================================
////        compilePythonOnly(
////                "src/tests/test_undefined.py",
////                "TEST 1: NameError (undefined variable)"
////        );
////
////        // ==============================================================
////        //  TEST 2: AttributeError — وصول لـ attribute غير موجود
////        //  يتوقع: AttributeError لـ text.append, items.split, user.upper, num.keys
////        // ==============================================================
////        compilePythonOnly(
////                "src/tests/test_invalid_attr.py",
////                "TEST 2: AttributeError (invalid attribute access)"
////        );
////
////        // ==============================================================
////        //  TEST 3: TypeError — عملية حسابية على None
////        //  يتوقع: TypeError لـ x+5, 10*x, x-y, x/2, x**2
////        // ==============================================================
////        compilePythonOnly(
////                "src/tests/test_operation_on_none.py",
////                "TEST 3: TypeError (operation on None)"
////        );
////
////        // ==============================================================
////        //  TEST 4: AttributeError على NoneType
////        //  يتوقع: AttributeError لـ user.name, data.get, items.append, text.upper
////        // ==============================================================
////        compilePythonOnly(
////                "src/tests/test_none_attr.py",
////                "TEST 4: AttributeError on NoneType"
////        );
////
////        // ==============================================================
////        //  TEST: Type Error
////        //  يتوقع: TypeError لكل الحالات الـ 24
////        // ==============================================================
////        compileAndCheck(
////                "src/tests/test_type_error.py",
////                "tests/test_type_error.html",
////                "TEST: Type Error"
////        );
////
////        // ==============================================================
////        //  TEST 5: Error Type — عملية بين نوعين غير متوافقين
////        //  يتوقع: 12 TypeError (عمليات حسابية/مقارنة/فهرسة/len)
////        //  + 3 حالات صحيحة ما لازم تنكشف كأخطاء
////        // ==============================================================
////        compilePythonOnly(
////                "src/tests/test_operation_type_error.py",
////                "TEST 5: Error Type (incompatible operand types)"
////        );
//
//    }
//}
import AST.Core.ASTNode;
import AST.Core.PageNode;
import Visitor.HtmlCssJinjaVisitor;
import Visitor.TemplateSymbolTableVisitor;
import antlr.TemplateLexer;
import antlr.TemplateParser;
import generation.*;
import main.pythoncompiler.PythonCompiler;
import symbol_table.SymbolTable;
import semantic_errors.SemanticChecker;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import generation.ASTJsonSerializer;

public class Main {

    // ==================== compileAndCheck (Tests 1-5) ====================

    /**
     * يشغّل الـ compiler على ملف Python + HTML واحد
     * ويطبع الـ Symbol Table + الأخطاء الدلالية
     *
     * لا يتضمن مرحلة Generation — فقط Parsing + Semantic.
     */
    public static void compileAndCheck(String pythonFile, String htmlFile,
                                       String outputTitle) throws Exception {
        System.out.println("\n");
        System.out.println("*".repeat(80));
        System.out.println("  " + outputTitle);
        System.out.println("  Python: " + pythonFile);
        System.out.println("  HTML  : " + htmlFile);
        System.out.println("*".repeat(80));

        SymbolTable symbolTable = new SymbolTable();

        // ===== Python Compiler =====
        String pythonFileName = new File(pythonFile).getName();
        symbolTable.setCurrentFileName(pythonFileName);
        symbolTable.setCurrentFilePath(pythonFile);

        try {
            PythonCompiler compiler = new PythonCompiler(symbolTable);
            compiler.compile(pythonFile);
        } catch (Exception e) {
            System.out.println("  [Python] Compilation error: " + e.getMessage());
        }

        // ===== HTML/CSS/Jinja Parser =====
        File companionFile = new File(htmlFile);
        if (!companionFile.exists() || !companionFile.isFile()) {
            System.out.println("\n  Companion HTML file '" + htmlFile
                    + "' not found — proceeding with Python-only analysis.");
        } else {
            String templateFileName = companionFile.getName();
            symbolTable.setSource("template");
            symbolTable.setCurrentFileName(templateFileName);
            symbolTable.setCurrentFilePath(htmlFile);

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
        Files.createDirectories(Path.of("Result"));
        semanticChecker.writeErrorsToFile(outputFile);

        // ===== Summary =====
        List<semantic_errors.SemanticError> errors = semanticChecker.getErrors();
        System.out.println("\n  Total errors found: " + errors.size());
        if (!errors.isEmpty()) {
            System.out.println("  Error breakdown:");
            Map<String, Integer> breakdown = new LinkedHashMap<>();
            for (semantic_errors.SemanticError err : errors) {
                breakdown.merge(err.getErrorName(), 1, Integer::sum);
            }
            for (var entry : breakdown.entrySet()) {
                System.out.println("    - " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    // ==================== compilePythonOnly ====================

    /**
     * نسخة مبسّطة: تشغّل الـ compiler على ملف Python فقط
     * بدون HTML.
     */
    public static void compilePythonOnly(String pythonFile,
                                         String outputTitle) throws Exception {
        System.out.println("\n");
        System.out.println("*".repeat(80));
        System.out.println("  " + outputTitle);
        System.out.println("  Python: " + pythonFile);
        System.out.println("  Mode  : Python-only (no HTML)");
        System.out.println("*".repeat(80));

        SymbolTable symbolTable = new SymbolTable();

        String pythonFileName = new File(pythonFile).getName();
        symbolTable.setCurrentFileName(pythonFileName);
        symbolTable.setCurrentFilePath(pythonFile);

        try {
            PythonCompiler compiler = new PythonCompiler(symbolTable);
            compiler.compile(pythonFile);
        } catch (Exception e) {
            System.out.println("  [Python] Compilation error: " + e.getMessage());
        }

        symbolTable.printSymbolTable();
        symbolTable.printScopeStructure();

        SemanticChecker semanticChecker = new SemanticChecker(symbolTable);
        semanticChecker.checkErrors();
        semanticChecker.printErrors();

        String outputFile = "Result/" + outputTitle
                .replace(" ", "_").replace(":", "") + ".txt";
        Files.createDirectories(Path.of("Result"));
        semanticChecker.writeErrorsToFile(outputFile);

        List<semantic_errors.SemanticError> errors = semanticChecker.getErrors();
        System.out.println("\n  Total errors found: " + errors.size());
        if (!errors.isEmpty()) {
            System.out.println("  Error breakdown:");
            Map<String, Integer> breakdown = new LinkedHashMap<>();
            for (semantic_errors.SemanticError err : errors) {
                breakdown.merge(err.getErrorName(), 1, Integer::sum);
            }
            for (var entry : breakdown.entrySet()) {
                System.out.println("    - " + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    // ==================== compileAndGenerate (Test 6 — Generation) ====================

    /**
     * Python + كل templates → Semantic gate → Person 1 ContextBuilder.
     * <p>
     * عند عدم وجود أخطاء دلالية: لا يُطبع تقرير Semantic الفارغ
     * ولا يُكتب ملف Result — فقط سطر تأكيد ثم Generation.
     * كتابة generation_log.txt تُترك لشخص 4.
     * </p>
     */
    public static void compileAndGenerate(String pythonFile, String templatesDir,
                                          String outputTitle) throws Exception {
        System.out.println("\n");
        System.out.println("*".repeat(80));
        System.out.println("  " + outputTitle);
        System.out.println("  Python: " + pythonFile);
        System.out.println("  Templates: " + templatesDir);
        System.out.println("*".repeat(80));

        SymbolTable symbolTable = new SymbolTable();

        // ===== 1. Python Compiler =====
        String pythonFileName = new File(pythonFile).getName();
        symbolTable.setCurrentFileName(pythonFileName);
        symbolTable.setCurrentFilePath(pythonFile);

        main.pythoncompiler.ast.ASTNode pythonRoot = null;  // ★ نعرّفها قبل try ★

        try {
            PythonCompiler compiler = new PythonCompiler(symbolTable);
            compiler.compile(pythonFile);
            pythonRoot = compiler.getAST();                   // ★ نجيب الـ root ★
        } catch (Exception e) {
            System.out.println("  [Python] Compilation error: " + e.getMessage());
            System.out.println("\n  Stopping — Python compilation failed.");
            return;
        }

        // ===== 2. Parse + visit ALL templates =====
        File templatesDirFile = new File(templatesDir);
        File[] htmlFiles = templatesDirFile.isDirectory()
                ? templatesDirFile.listFiles((d, n) -> n.endsWith(".html"))
                : null;

        if (!templatesDirFile.isDirectory()) {
            System.out.println("  [Template] Templates directory '" + templatesDir
                    + "' not found — proceeding with Python-only symbol table.");
        } else if (htmlFiles == null || htmlFiles.length == 0) {
            System.out.println("  [Template] No .html files found in '" + templatesDir + "'.");
        } else {
            Arrays.sort(htmlFiles, Comparator.comparing(File::getName));

            for (File htmlFile : htmlFiles) {
                String templateFileName = htmlFile.getName();
                symbolTable.setSource("template");
                symbolTable.setCurrentFileName(templateFileName);
                symbolTable.setCurrentFilePath(htmlFile.getPath());

                try {
                    CharStream charStream = CharStreams.fromFileName(htmlFile.getPath());
                    TemplateLexer lexer = new TemplateLexer(charStream);
                    CommonTokenStream tokens = new CommonTokenStream(lexer);
                    TemplateParser parser = new TemplateParser(tokens);
                    ParseTree tree = parser.page();

                    HtmlCssJinjaVisitor visitor = new HtmlCssJinjaVisitor();
                    PageNode ast = (PageNode) visitor.visit(tree);

                    System.out.println("\n  ---- Template AST: " + templateFileName + " ----");
                    ast.print("");

                    TemplateSymbolTableVisitor templateVisitor =
                            new TemplateSymbolTableVisitor(symbolTable);
                    templateVisitor.visit(ast);

                    System.out.println("  [Template] Visited: " + templateFileName);
                } catch (Exception e) {
                    System.out.println("  [Template] Parsing error in '"
                            + templateFileName + "': " + e.getMessage());
                }
            }
        }

        // ===== 3. Print Symbol Table + Scope Structure =====
        symbolTable.printSymbolTable();
        symbolTable.printScopeStructure();

        // ===== 4. Semantic gate =====
        SemanticChecker semanticChecker = new SemanticChecker(symbolTable);
        semanticChecker.checkErrors();

        List<semantic_errors.SemanticError> errors = semanticChecker.getErrors();

        if (!errors.isEmpty()) {
            semanticChecker.printErrors();

            String outputFile = "Result/" + outputTitle
                    .replace(" ", "_").replace(":", "") + ".txt";
            Files.createDirectories(Path.of("Result"));
            semanticChecker.writeErrorsToFile(outputFile);

            System.out.println("\n  Total errors found: " + errors.size());
            System.out.println("  Error breakdown:");
            Map<String, Integer> breakdown = new LinkedHashMap<>();
            for (semantic_errors.SemanticError err : errors) {
                breakdown.merge(err.getErrorName(), 1, Integer::sum);
            }
            for (var entry : breakdown.entrySet()) {
                System.out.println("    - " + entry.getKey() + ": " + entry.getValue());
            }

            System.out.println("\n  Cannot start Generation — Semantic errors exist.");
            System.out.println("  Fix semantic errors first, then retry generation.");
            return;
        }

        System.out.println("\n  Semantic check: OK (0 errors)");
        System.out.println("  Starting Generation ...");

        // ===== 5. ContextBuilder =====
        ContextBuilder contextBuilder = new ContextBuilder();
        contextBuilder.setPythonFilePath(pythonFile);
        contextBuilder.setTemplatesDirectory(templatesDir);
        contextBuilder.setOutputDirectory("output");

        GenerationContext generationContext = contextBuilder.build();

        if (generationContext == null) {
            System.out.println("\n  Stopping — ContextBuilder.build() returned null.");
            return;
        }

        // ★ نسلسل Python AST ونخزّن JSON بالـ context ★
        if (pythonRoot != null) {
            ASTJsonSerializer serializer = new ASTJsonSerializer();
            String pythonAstJson = serializer.serializePythonAST(pythonRoot);
            generationContext.setPythonAstJson(pythonAstJson);
       //     System.out.println("  [Main] Python AST serialized successfully.");
        } else {
        //    System.out.println("  [Main] Python AST root is null — skipping ast_python.json.");
        }

        generationContext.setSemanticPassed(true);

        // ===== 6. Generation (Person 4 — Generator) =====
        Generator generator = new Generator(generationContext);
        generator.generate();

        // ===== 7. Print Generation Logs =====
      //  printGenerationLogs(generationContext);

        System.out.println("\n  Generation phase completed.");
    }
    // ==================== printGenerationLogs ====================

    /**
     * يطبع logEntries + warnings على الـ console.
     * كتابة generation_log.txt = شخص 4 لاحقاً.
     */
    private static void printGenerationLogs(GenerationContext context) {
        System.out.println();
        System.out.println("=".repeat(60));
        System.out.println(" Generation Logs");
        System.out.println("=".repeat(60));

        List<String> entries = context.getLogEntries();
        if (entries.isEmpty()) {
            System.out.println("  (no log entries)");
        } else {
            for (int i = 0; i < entries.size(); i++) {
                System.out.printf("  [%02d] %s%n", i + 1, entries.get(i));
            }
        }

        if (!context.getWarnings().isEmpty()) {
            System.out.println();
            System.out.println("--- Warnings ---");
            for (String w : context.getWarnings()) {
                System.out.println("  ⚠ " + w);
            }
        }

        System.out.println("=".repeat(60));
        System.out.println(" Total log entries: " + entries.size());
        System.out.println("=".repeat(60));
    }

    // ==================== main ====================

    public static void main(String[] args) throws Exception {

        // ==============================================================
        //  TEST 6: Code Generation — Flask app + Jinja templates
        // ==============================================================
        compileAndGenerate(
                "src/tests/app1.py",
                "src/templets",
                "TEST 6: Code Generation (app1.py)"
        );

        // ==============================================================
        //  TEST 1: NameError
        // ==============================================================
//        compilePythonOnly(
//                "src/tests/test_undefined.py",
//                "TEST 1: NameError (undefined variable)"
//        );

        // ==============================================================
        //  TEST 2: AttributeError
        // ==============================================================
//        compilePythonOnly(
//                "src/tests/test_invalid_attr.py",
//                "TEST 2: AttributeError (invalid attribute access)"
//        );

        // ==============================================================
        //  TEST 3: TypeError (operation on None)
        // ==============================================================
//        compilePythonOnly(
//                "src/tests/test_operation_on_none.py",
//                "TEST 3: TypeError (operation on None)"
//        );

        // ==============================================================
        //  TEST 4: AttributeError on NoneType
        // ==============================================================
//        compilePythonOnly(
//                "src/tests/test_none_attr.py",
//                "TEST 4: AttributeError on NoneType"
//        );

        // ==============================================================
        //  TEST: Type Error
        // ==============================================================
//        compileAndCheck(
//                "src/tests/test_type_error.py",
//                "tests/test_type_error.html",
//                "TEST: Type Error"
//        );

        // ==============================================================
        //  TEST 5: Error Type
        // ==============================================================
//        compilePythonOnly(
//                "src/tests/test_operation_type_error.py",
//                "TEST 5: Error Type (incompatible operand types)"
//        );
    }
}
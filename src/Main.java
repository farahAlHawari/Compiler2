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
import java.io.OutputStream;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.io.*;

public class Main {

    private static final String OUTPUT_DIR = "output";
    private static final int SERVER_PORT = 8080;
    private static final AtomicBoolean skipNextWatch = new AtomicBoolean(false);

    // Semantic Report

    private static final String SEMANTIC_REPORT_DIR = "compiler_output";
    private static final String SEMANTIC_REPORT_FILE = SEMANTIC_REPORT_DIR + "/Semantic_report.txt";


    private static String companionHtmlFor(String pythonFile) {
        String baseName = new File(pythonFile).getName().replace(".py", ".html");
        return "tests/" + baseName;
    }

    // Semantic Test Files

    private static final String[][] SEMANTIC_TESTS = {
            {"src/tests/test_type_error.py",          "TypeError (binary op)"},
            {"src/tests/test_operation_type_error.py","Operation Type Error"},

            {"src/tests/test_undefined.py",           "NameError (undefined variable)"},
            {"src/tests/test_type_mismatch.py",       "Type Mismatch (declared type / Bridge)"},
            {"src/tests/test_missing_flask_var.py",   "Missing Flask Variable"},
            {"src/tests/test_unbound_local.py",       "UnboundLocalError(Scope Error)"},


            {"src/tests/test_invalid_attr.py",        "AttributeError (invalid attribute)"},
            {"src/tests/test_operation_on_none.py",   "TypeError (operation on None)"},
            {"src/tests/test_none_attr.py",            "AttributeError on NoneType"},
            {"src/tests/test_division.py",             "Division By Zero"},
            {"src/tests/test_return_type_mismatch.py","Return Type Mismatch"},
            {"src/tests/test_use_before_init.py",     "Use Before Initialization"},
            {"src/tests/test_invalid_func_call.py",   "Invalid Function Call"},
            {"src/tests/test_wrong_args_count.py",    "Wrong Arguments Count"},


    };

    private static final String GENERATION_PYTHON  = "src/tests/app1.py";
    private static final String GENERATION_TEMPLATES = "src/templets";



    public static List<semantic_errors.SemanticError> compileAndCheck(
            String pythonFile, String htmlFile, String outputTitle) throws Exception {
        System.out.println("\n");
        System.out.println("*".repeat(80));
        System.out.println("  " + outputTitle);
        System.out.println("  Python: " + pythonFile);
        System.out.println("  HTML  : " + htmlFile);
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

        File companionFile = new File(htmlFile);
        if (!companionFile.exists() || !companionFile.isFile()) {
            System.out.println("\n  Companion HTML file '" + htmlFile
                    + "' not found \u2014 proceeding with Python-only analysis.");
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

        symbolTable.printSymbolTable();
        symbolTable.printScopeStructure();

        SemanticChecker semanticChecker = new SemanticChecker(symbolTable);
        semanticChecker.checkErrors();
        semanticChecker.printErrors();

        Files.createDirectories(Path.of(SEMANTIC_REPORT_DIR));
        semanticChecker.writeErrorsToFile(SEMANTIC_REPORT_FILE);

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

        return errors;
    }



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

        Files.createDirectories(Path.of(SEMANTIC_REPORT_DIR));
        semanticChecker.writeErrorsToFile(SEMANTIC_REPORT_FILE);

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



    public static GenerationContext compileAndGenerate(String pythonFile, String templatesDir,
                                                       String outputTitle) throws Exception {
        System.out.println("\n");
        System.out.println("*".repeat(80));
        System.out.println("  " + outputTitle);
        System.out.println("  Python: " + pythonFile);
        System.out.println("  Templates: " + templatesDir);
        System.out.println("*".repeat(80));

        SymbolTable symbolTable = new SymbolTable();

        //  1. Python Compiler
        String pythonFileName = new File(pythonFile).getName();
        symbolTable.setCurrentFileName(pythonFileName);
        symbolTable.setCurrentFilePath(pythonFile);

        main.pythoncompiler.ast.ASTNode pythonRoot = null;

        try {
            PythonCompiler compiler = new PythonCompiler(symbolTable);
            compiler.compile(pythonFile);
            pythonRoot = compiler.getAST();
        } catch (Exception e) {
            System.out.println("  [Python] Compilation error: " + e.getMessage());
            System.out.println("\n  Stopping \u2014 Python compilation failed.");
            return null;
        }

        // 2. Parse + visit ALL templates
        File templatesDirFile = new File(templatesDir);
        File[] htmlFiles = templatesDirFile.isDirectory()
                ? templatesDirFile.listFiles((d, n) -> n.endsWith(".html"))
                : null;

        if (!templatesDirFile.isDirectory()) {
            System.out.println("  [Template] Templates directory '" + templatesDir
                    + "' not found \u2014 proceeding with Python-only symbol table.");
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


        symbolTable.printSymbolTable();
        symbolTable.printScopeStructure();


        SemanticChecker semanticChecker = new SemanticChecker(symbolTable);
        semanticChecker.checkErrors();


        semanticChecker.printErrors();

        List<semantic_errors.SemanticError> errors = semanticChecker.getErrors();


        Files.createDirectories(Path.of(SEMANTIC_REPORT_DIR));
        semanticChecker.writeErrorsToFile(SEMANTIC_REPORT_FILE);

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

            System.out.println("\n  Cannot start Generation \u2014 Semantic errors exist.");
            System.out.println("  Fix semantic errors first, then retry generation.");
            return null;
        }

        System.out.println("\n  Semantic check: OK (0 errors)");
        System.out.println("  Starting Generation ...");

        //  5. ContextBuilder
        ContextBuilder contextBuilder = new ContextBuilder();
        contextBuilder.setPythonFilePath(pythonFile);
        contextBuilder.setTemplatesDirectory(templatesDir);
        contextBuilder.setOutputDirectory("output");

        GenerationContext generationContext = contextBuilder.build();

        if (generationContext == null) {
            System.out.println("\n  Stopping \u2014 ContextBuilder.build() returned null.");
            return null;
        }

        if (pythonRoot != null) {
            ASTJsonSerializer serializer = new ASTJsonSerializer();
            String pythonAstJson = serializer.serializePythonAST(pythonRoot);
            generationContext.setPythonAstJson(pythonAstJson);
        }

        generationContext.setSemanticPassed(true);

        //  6. Generation
        Generator generator = new Generator(generationContext);
        generator.generate();


        printGenerationLogs(generationContext);

        System.out.println("\n  Generation phase completed.");

        return generationContext;
    }



    private static void printGenerationLogs(GenerationContext context) {
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("                    GENERATION LOG");
        System.out.println("=".repeat(80));

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
                System.out.println("  Warning: " + w);
            }
        }

        System.out.println();
        System.out.println("--- Generated Pages ---");
        for (String fileName : context.getOutputHtml().keySet()) {
            System.out.println("  " + fileName);
        }

        System.out.println("=".repeat(80));
        System.out.println(" Total log entries: " + entries.size());
        System.out.println(" Generated pages: " + context.getOutputHtml().size());
        System.out.println("=".repeat(80));
    }



    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println();
            System.out.println("=".repeat(60));
            System.out.println("        COMPILER 2 \u2014 Interactive Menu");
            System.out.println("=".repeat(60));
            System.out.println("  1. Semantic Analysis  (choose a test)");
            System.out.println("  2. Code Generation   (Full pipeline + HTTP Server)");
            System.out.println("  3. Run All Semantic Tests");
            System.out.println("  0. Exit");
            System.out.println("=".repeat(60));
            System.out.print("  Choose [0-3]: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> runSemanticMenu(scanner);
                case "2" -> runCodeGeneration(scanner);
                case "3" -> runAllSemanticTests();
                case "0" -> {
                    System.out.println("\n  Goodbye!");
                    return;
                }
                default -> System.out.println("  Invalid choice. Please enter 0-3.");
            }
        }
    }

    //  Semantic Analysis Menu

    private static void runSemanticMenu(Scanner scanner) throws Exception {
        System.out.println("\n  Available Semantic Tests:");
        System.out.println("  " + "-".repeat(56));
        for (int i = 0; i < SEMANTIC_TESTS.length; i++) {
            System.out.printf("  %2d. %-40s [%s]%n",
                    (i + 1), SEMANTIC_TESTS[i][1], SEMANTIC_TESTS[i][0]);
        }
        System.out.println("  " + "-".repeat(56));
        System.out.print("  Enter test number (or 0 to go back): ");

        String input = scanner.nextLine().trim();
        if (input.equals("0")) return;

        try {
            int idx = Integer.parseInt(input) - 1;
            if (idx < 0 || idx >= SEMANTIC_TESTS.length) {
                System.out.println("  Invalid test number.");
                return;
            }
            String pythonFile = SEMANTIC_TESTS[idx][0];
            String htmlFile = companionHtmlFor(pythonFile);
            compileAndCheck(pythonFile, htmlFile, "Semantic: " + SEMANTIC_TESTS[idx][1]);
            System.out.println("\n  Report written to: " + SEMANTIC_REPORT_FILE);
        } catch (NumberFormatException e) {
            System.out.println("  Please enter a valid number.");
        }
    }

    //  Code Generation + HTTP Server

    private static void runCodeGeneration(Scanner scanner) throws Exception {
        System.out.println();
        System.out.println("  Available generation files:");
        System.out.println("    1. app1.py  +  src/templets");
        System.out.print("  Choose [1] (or 0 to go back): ");

        String input = scanner.nextLine().trim();
        if (input.equals("0")) return;

        String pythonFile = GENERATION_PYTHON;
        String templatesDir = GENERATION_TEMPLATES;

        switch (input) {
            case "1" -> { /* defaults */ }
            default -> {
                System.out.println("  Invalid choice.");
                return;
            }
        }

        String outputTitle = "Code Generation (" + new File(pythonFile).getName() + ")";

        GenerationContext ctx = compileAndGenerate(pythonFile, templatesDir, outputTitle);
        if (ctx == null) return;

        System.out.println("\n  Starting HTTP Server on http://localhost:" + SERVER_PORT);
        System.out.println("  Press Ctrl+C to stop.\n");

        startHttpServer(pythonFile, templatesDir, outputTitle);
    }

    //  Run All Semantic Tests

    private static void runAllSemanticTests() throws Exception {
        System.out.println();
        System.out.println("=".repeat(80));
        System.out.println("        RUNNING ALL SEMANTIC TESTS");
        System.out.println("=".repeat(80));

        List<semantic_errors.SemanticError> allErrors = new ArrayList<>();
        for (int i = 0; i < SEMANTIC_TESTS.length; i++) {
            System.out.println("\n  >>> TEST " + (i + 1) + "/" + SEMANTIC_TESTS.length + " <<<");
            try {
                String pythonFile = SEMANTIC_TESTS[i][0];
                String htmlFile = companionHtmlFor(pythonFile);
                List<semantic_errors.SemanticError> testErrors =
                        compileAndCheck(pythonFile, htmlFile, "All Tests: " + SEMANTIC_TESTS[i][1]);
                allErrors.addAll(testErrors);
            } catch (Exception e) {
                System.out.println("  [ERROR] Test failed: " + e.getMessage());
            }
        }

        Files.createDirectories(Path.of(SEMANTIC_REPORT_DIR));
        semantic_errors.SemanticChecker.writeErrorsToFile(SEMANTIC_REPORT_FILE, allErrors);

        System.out.println("\n" + "=".repeat(80));
        System.out.println("  All tests completed (" + allErrors.size() + " total errors).");
        System.out.println("  Combined report written to: " + SEMANTIC_REPORT_FILE);
        System.out.println("=".repeat(80));
    }

    //  HTTP Server

    private static void startHttpServer(String pythonFile, String templatesDir,
                                        String outputTitle) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);

        server.createContext("/", (HttpExchange exchange) -> {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            try {
                if (method.equals("GET") && (path.equals("/") || path.equals("/index.html"))) {
                    serveIndexWithDeleteScript(exchange);
                    return;
                }

                if (method.equals("GET") && path.equals("/add.html")) {
                    serveStaticFile(exchange, "add.html");
                    return;
                }

                if (method.equals("POST") && path.equals("/add.html")) {
                    String body = new String(exchange.getRequestBody().readAllBytes());
                    Map<String, String> params = parseFormData(body);

                    String name    = params.getOrDefault("name", "");
                    String price   = params.getOrDefault("price", "0");
                    String image   = params.getOrDefault("image", "");
                    String details = params.getOrDefault("details", "");

                    if (name.isEmpty()) {
                        sendJson(exchange, 400, "{\"success\":false,\"error\":\"Product name is required\"}");
                        return;
                    }

                    addProductToSource(pythonFile, name, price, image, details);
                    skipNextWatch.set(true);
                    try { compileAndGenerate(pythonFile, templatesDir, outputTitle); } catch (Exception ignored) {}
                    System.out.println("  [API] Product added: " + name);
                    sendRedirect(exchange, "/index.html");
                    return;
                }

                if (method.equals("GET") && path.startsWith("/delete/")) {
                    String indexStr = path.substring("/delete/".length());
                    try {
                        int index = Integer.parseInt(indexStr);
                        deleteProductFromSource(pythonFile, index);
                        skipNextWatch.set(true);
                        try { compileAndGenerate(pythonFile, templatesDir, outputTitle); } catch (Exception ignored) {}
                        System.out.println("  [API] Product deleted at index: " + index);
                    } catch (NumberFormatException e) {
                        System.out.println("  [API] Invalid delete index: " + indexStr);
                    }
                    sendRedirect(exchange, "/index.html");
                    return;
                }

                if (method.equals("GET") && path.startsWith("/details/")) {
                    String indexStr = path.substring("/details/".length());
                    sendRedirect(exchange, "/product_details_" + indexStr + ".html");
                    return;
                }

                serveStaticFile(exchange, path);

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    sendJson(exchange, 500, "{\"error\":\"" + e.getMessage().replace("\"", "'") + "\"}");
                } catch (IOException ignored) {}
            }
        });

        server.setExecutor(null);
        server.start();

        // WatchService
        System.out.println("  Server  ->  http://localhost:" + SERVER_PORT);
        System.out.println("  Watching for changes... (Ctrl+C to stop)");
        System.out.println("  You can now add/delete products from the web UI!");

        try {
            Path pythonDir = Paths.get(pythonFile).getParent();
            String pythonFileName = Paths.get(pythonFile).getFileName().toString();
            Path templatesDirPath = Paths.get(templatesDir);

            java.nio.file.WatchService watchService = FileSystems.getDefault().newWatchService();
            pythonDir.register(watchService, java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY);
            templatesDirPath.register(watchService, java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                java.nio.file.WatchKey key = watchService.take();

                boolean pythonChanged = false;
                boolean templateChanged = false;

                for (java.nio.file.WatchEvent<?> event : key.pollEvents()) {
                    Path changed = (Path) event.context();
                    String changedName = changed.toString();

                    if (changedName.equals(pythonFileName)) pythonChanged = true;
                    else if (changedName.endsWith(".html")) templateChanged = true;
                }

                if ((pythonChanged || templateChanged) && !skipNextWatch.getAndSet(false)) {
                    Thread.sleep(500);
                    System.out.println("\n  File changed -- regenerating...");
                    try { compileAndGenerate(pythonFile, templatesDir, outputTitle); } catch (Exception ignored) {}
                    System.out.println("  Done. Waiting for next change...");
                }

                boolean valid = key.reset();
                if (!valid) break;
            }
        } catch (InterruptedException e) {
            System.out.println("\n  Watcher stopped.");
        }
    }

    // HTTP Helper Method

    private static void serveStaticFile(HttpExchange exchange, String filePath) throws IOException {
        if (filePath.startsWith("/")) filePath = filePath.substring(1);
        if (filePath.isEmpty()) filePath = "index.html";

        File file = new File(OUTPUT_DIR, filePath);
        if (file.exists() && file.isFile()) {
            byte[] bytes = Files.readAllBytes(file.toPath());
            exchange.getResponseHeaders().set("Content-Type", getContentType(filePath));
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
        } else {
            String msg = "404 Not Found: " + filePath;
            exchange.sendResponseHeaders(404, msg.length());
            try (OutputStream os = exchange.getResponseBody()) { os.write(msg.getBytes()); }
        }
    }

    private static void serveIndexWithDeleteScript(HttpExchange exchange) throws IOException {
        File file = new File(OUTPUT_DIR, "index.html");
        if (!file.exists() || !file.isFile()) {
            serveStaticFile(exchange, "index.html");
            return;
        }

        String html = new String(Files.readAllBytes(file.toPath()), "UTF-8");

        String deleteScript =
                "<script>\n" +
                        "  document.querySelectorAll('form[action=\"#\"]').forEach(function(form, i) {\n" +
                        "    if (form.querySelector('.btn-danger')) {\n" +
                        "      form.action = '/delete/' + i;\n" +
                        "    }\n" +
                        "  });\n" +
                        "</script>\n";

        html = html.replace("</body>", deleteScript + "</body>");

        byte[] bytes = html.getBytes("UTF-8");
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) { os.write(bytes); }
    }

    private static void sendJson(HttpExchange exchange, int code, String json) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(code, json.length());
        try (OutputStream os = exchange.getResponseBody()) { os.write(json.getBytes()); }
    }

    private static void sendRedirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(302, -1);
        exchange.getResponseBody().close();
    }

    private static Map<String, String> parseFormData(String body) {
        Map<String, String> params = new HashMap<>();
        if (body == null || body.isEmpty()) return params;
        for (String pair : body.split("&")) {
            String[] kv = pair.split("=", 2);
            if (kv.length == 2) {
                try {
                    params.put(URLDecoder.decode(kv[0], "UTF-8"),
                            URLDecoder.decode(kv[1], "UTF-8"));
                } catch (Exception e) {
                    params.put(kv[0], kv[1]);
                }
            }
        }
        return params;
    }

    private static String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html; charset=UTF-8";
        if (path.endsWith(".css"))  return "text/css; charset=UTF-8";
        if (path.endsWith(".js"))   return "application/javascript; charset=UTF-8";
        if (path.endsWith(".png"))  return "image/png";
        if (path.endsWith(".jpg") || path.endsWith(".jpeg")) return "image/jpeg";
        if (path.endsWith(".gif"))  return "image/gif";
        if (path.endsWith(".svg"))  return "image/svg+xml";
        return "application/octet-stream";
    }
    private static void stripTrailingComma(List<String> lines) {
        int closeIdx = -1;
        int depth = 0;
        boolean inProducts = false;

        for (int idx = 0; idx < lines.size(); idx++) {
            String t = lines.get(idx).trim();
            if (!inProducts && t.startsWith("products") && t.contains("[")) {
                inProducts = true;
            }
            if (inProducts) {
                for (char c : t.toCharArray()) {
                    if (c == '[') depth++;
                    if (c == ']') depth--;
                }
                if (depth <= 0) { closeIdx = idx; break; }
            }
        }
        if (closeIdx == -1) return;

        for (int idx = closeIdx - 1; idx >= 0; idx--) {
            String line = lines.get(idx);
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;
            if (trimmed.endsWith(",")) {
                int lastComma = line.lastIndexOf(',');
                lines.set(idx, line.substring(0, lastComma) + line.substring(lastComma + 1));
            }
            break;
        }
    }


    private static void addProductToSource(String pythonFile, String name, String price,
                                           String image, String details) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(pythonFile));
        List<String> newLines = new ArrayList<>();
        boolean insideProducts = false;
        int bracketDepth = 0;
        boolean inserted = false;

        for (String line : lines) {
            String trimmed = line.trim();

            if (!insideProducts && trimmed.startsWith("products") && trimmed.contains("[")) {
                insideProducts = true;
                newLines.add(line);
                for (char c : trimmed.toCharArray()) {
                    if (c == '[') bracketDepth++;
                    if (c == ']') bracketDepth--;
                }
                if (bracketDepth <= 0) {
                    newLines.add("    {\"name\": \"" + name + "\", \"price\": " + price
                            + ", \"image\": \"" + image + "\", \"details\": \"" + details + "\"}");
                    inserted = true;
                    insideProducts = false;
                }
                continue;
            }

            if (insideProducts) {
                for (char c : trimmed.toCharArray()) {
                    if (c == '[') bracketDepth++;
                    if (c == ']') bracketDepth--;
                }

                if (bracketDepth <= 0) {
                    //  الإصلاح: تأكد إنو آخر منتج موجود منتهي بفاصلة قبل ما نضيف الجديد
                    for (int idx = newLines.size() - 1; idx >= 0; idx--) {
                        String prevLine = newLines.get(idx);
                        String prevTrimmed = prevLine.trim();
                        if (prevTrimmed.isEmpty()) continue; // تخطي السطور الفاضية
                        if (!prevTrimmed.endsWith(",") && !prevTrimmed.endsWith("[")) {
                            newLines.set(idx, prevLine + ",");
                        }
                        break;
                    }

                    newLines.add("    {\"name\": \"" + name + "\", \"price\": " + price
                            + ", \"image\": \"" + image + "\", \"details\": \"" + details + "\"},");
                    newLines.add(line);
                    inserted = true;
                    insideProducts = false;
                    continue;
                }
            }

            newLines.add(line);
        }

        if (!inserted) {
            throw new IOException("Could not find 'products' list in " + pythonFile);
        }

        stripTrailingComma(newLines);
        Files.write(Paths.get(pythonFile), newLines);
    }

    private static void deleteProductFromSource(String pythonFile, int index) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(pythonFile));
        List<String> newLines = new ArrayList<>();
        boolean insideProducts = false;
        int listDepth = 0;
        int productCount = 0;
        boolean skipping = false;
        int dictBraceDepth = 0;
        boolean deleted = false;

        for (String line : lines) {
            String trimmed = line.trim();

            if (!insideProducts) {
                newLines.add(line);
                if (trimmed.startsWith("products") && trimmed.contains("[")) {
                    insideProducts = true;
                    listDepth = 0;
                    for (char c : trimmed.toCharArray()) {
                        if (c == '[') listDepth++;
                        if (c == ']') listDepth--;
                    }
                }
                continue;
            }

            int openBrackets = 0, closeBrackets = 0;
            int openBraces = 0, closeBraces = 0;
            for (char c : trimmed.toCharArray()) {
                if (c == '[') openBrackets++;
                if (c == ']') closeBrackets++;
                if (c == '{') openBraces++;
                if (c == '}') closeBraces++;
            }
            listDepth += openBrackets - closeBrackets;

            if (listDepth == 1 && openBraces > 0 && !skipping) {
                if (productCount == index) {
                    skipping = true;
                    dictBraceDepth = 0;
                }
                productCount++;
            }

            if (skipping) {
                for (char c : trimmed.toCharArray()) {
                    if (c == '{') dictBraceDepth++;
                    if (c == '}') dictBraceDepth--;
                }
                if (dictBraceDepth <= 0 && closeBraces > 0) {
                    skipping = false;
                    deleted = true;
                }
                continue;
            }

            newLines.add(line);

            if (listDepth <= 0) {
                insideProducts = false;
            }
        }

        if (!deleted) {
            throw new IOException("Could not delete product at index " + index
                    + " (found " + productCount + " products)");
        }

        stripTrailingComma(newLines);
        Files.write(Paths.get(pythonFile), newLines);
    }
}
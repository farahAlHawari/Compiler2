package generation;

import AST.Core.PageNode;
import Visitor.HtmlCssJinjaVisitor;
import antlr.TemplateLexer;
import antlr.TemplateParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContextBuilder {

    private final GenerationContext context = new GenerationContext();
    private String templatesDirectory = "tests";
    private String pythonFilePath = "";
    private String outputDirectory = "output";

    public void setTemplatesDirectory(String dir) {
        this.templatesDirectory = dir;
        context.setTemplatesDirectory(dir);
    }


    public void setOutputDirectory(String dir) {
        this.outputDirectory = dir;
        context.setOutputDirectory(dir);
    }


    public void setPythonFilePath(String path) {
        this.pythonFilePath = path;
        context.setPythonFilePath(path);
    }


    public GenerationContext getContext() {
        return context;
    }

    private void log(String msg) {
        context.addLog("[ContextBuilder] " + msg);
    }


    public GenerationContext build() {
        context.reset();
        log("Starting source-based extraction...");
        context.setPythonFilePath(pythonFilePath);
        context.setTemplatesDirectory(templatesDirectory);
        context.setOutputDirectory(outputDirectory);

        safeRun("products", this::extractProductsFromSource);
        safeRun("routes", this::extractRoutesFromSource);
        safeRun("templateVars", this::extractTemplateVariablesFromSource);
        safeRun("globals", this::extractGlobalAssignmentsFromSource);
        safeRun("templateNames", this::collectTemplateNames);
        safeRun("staticFiles", this::collectStaticFiles);
        safeRun("parseASTs", this::parseTemplateASTs);
        safeRun("extends/includes", this::discoverExtendsAndIncludes);
        safeRun("populateGlobals", this::populateGlobalVariables);

        context.validate();
        context.freeze();

        log("Done. Products=" + context.getProducts().size()
                + " Routes=" + context.getRoutes().size()
                + " Templates=" + context.getTemplateNames().size()
                + " ASTs=" + context.getTemplateASTs().size()
                + " Route->Template=" + context.getTemplateToRoute().size());
        return context;
    }

    private void safeRun(String step, Runnable action) {
        try {
            action.run();
        } catch (Exception e) {
            context.addWarning("Step '" + step + "' failed: " + e);
            log("ERROR in " + step + ": " + e);
        }
    }


    private void extractProductsFromSource() {
        log("extractProductsFromSource...");
        if (isBlank(pythonFilePath)) {
            context.addWarning("Python file path not set");
            return;
        }

        String value = extractAssignmentValue("products");
        if (value != null && !value.isEmpty()
                && !"[]".equals(value) && !"list()".equals(value)) {
            parseAndAddProducts(value);
        } else {
            log("products empty or list()");
        }

        extractProductsAppends();
        log("Products: " + context.getProducts().size());
    }

    private void parseAndAddProducts(String value) {
        Object parsed = parsePythonValue(value);
        if (!(parsed instanceof List)) {
            context.addWarning("products is not a list");
            return;
        }
        for (Object item : (List<?>) parsed) {
            if (item == null) {
                continue;
            }
            if (item instanceof Map) {
                Map<String, Object> product = new LinkedHashMap<>();
                for (var e : ((Map<?, ?>) item).entrySet()) {
                    product.put(String.valueOf(e.getKey()), e.getValue());
                }
                context.addProduct(product);
            }
        }
    }


    private void extractProductsAppends() {
        try {
            String source = Files.readString(Path.of(pythonFilePath));
            boolean[] insideFunctionByLine = computeInsideFunctionLines(source);

            Matcher m = Pattern.compile(
                            "products\\s*\\.\\s*append\\s*\\(\\s*(\\{[\\s\\S]*?})\\s*\\)")
                    .matcher(source);
            while (m.find()) {
                int line = lineNumberOf(source, m.start());
                if (isInsideFunction(insideFunctionByLine, line)) {
                    log("Skipped products.append at line " + (line + 1)
                            + " — inside a function/route body (runtime data, not static)");
                    continue;
                }
                Object parsed = parsePythonValue(m.group(1));
                if (parsed instanceof Map) {
                    Map<String, Object> product = new LinkedHashMap<>();
                    for (var e : ((Map<?, ?>) parsed).entrySet()) {
                        product.put(String.valueOf(e.getKey()), e.getValue());
                    }
                    context.addProduct(product);
                }
            }

            Matcher m2 = Pattern.compile("products\\s*\\+=\\s*(\\[[\\s\\S]*?])")
                    .matcher(source);
            while (m2.find()) {
                int line = lineNumberOf(source, m2.start());
                if (isInsideFunction(insideFunctionByLine, line)) {
                    log("Skipped products += at line " + (line + 1)
                            + " — inside a function/route body (runtime data, not static)");
                    continue;
                }
                parseAndAddProducts(m2.group(1));
            }
        } catch (Exception e) {
            context.addWarning("products.append extraction failed: " + e);
        }
    }


    private boolean[] computeInsideFunctionLines(String source) {
        String[] lines = source.split("\n", -1);
        boolean[] result = new boolean[lines.length];
        boolean insideFunction = false;
        int defIndent = -1;

        for (int i = 0; i < lines.length; i++) {
            String raw = lines[i];
            String line = raw.trim();
            int indent = countIndent(raw);

            if (insideFunction && !line.isEmpty() && !line.startsWith("#")
                    && indent <= defIndent) {
                insideFunction = false;
            }

            if (line.startsWith("def ")) {
                result[i] = insideFunction;
                insideFunction = true;
                defIndent = indent;
                continue;
            }

            result[i] = insideFunction;
        }
        return result;
    }

    /** @return 0-based line number containing character offset {@code charIndex} */
    private int lineNumberOf(String source, int charIndex) {
        int line = 0;
        for (int i = 0; i < charIndex && i < source.length(); i++) {
            if (source.charAt(i) == '\n') {
                line++;
            }
        }
        return line;
    }

    private boolean isInsideFunction(boolean[] flags, int line) {
        return line >= 0 && line < flags.length && flags[line];
    }


    private void extractRoutesFromSource() {
        log("extractRoutesFromSource...");
        if (isBlank(pythonFilePath)) {
            return;
        }

        try {
            String source = Files.readString(Path.of(pythonFilePath));
            String[] lines = source.split("\n", -1);

            String currentUrl = null;
            int decoratorLine = -1;
            boolean multi = false;
            StringBuilder buf = new StringBuilder();

            Pattern decoPat = Pattern.compile(
                    "^@app\\.(route|get|post|put|delete)\\b");

            for (int i = 0; i < lines.length; i++) {
                String line = lines[i].trim();

                if (decoPat.matcher(line).find()) {
                    buf.setLength(0);
                    buf.append(line);
                    decoratorLine = i + 1;
                    if (line.contains(")")) {
                        currentUrl = extractRouteUrl(line);
                        multi = false;
                        log("Decorator " + currentUrl + " @ line " + decoratorLine);
                    } else {
                        multi = true;
                    }
                    continue;
                }

                if (multi) {
                    buf.append(" ").append(line);
                    if (line.contains(")")) {
                        currentUrl = extractRouteUrl(buf.toString());
                        multi = false;
                        log("Multi-line decorator " + currentUrl);
                    }
                    continue;
                }

                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                if (currentUrl != null && line.startsWith("def ")
                        && (i + 1 - decoratorLine) <= 8) {
                    String func = extractFunctionName(line);
                    if (func != null) {
                        context.addRoute(currentUrl, func);
                        log("Route: " + currentUrl + " → " + func);
                    }
                    currentUrl = null;
                    continue;
                }

                if (currentUrl != null
                        && !line.startsWith("@app.")
                        && !line.startsWith("def ")) {
                    currentUrl = null;
                }
            }
        } catch (Exception e) {
            context.addWarning("Route extraction failed: " + e);
        }
        log("Routes: " + context.getRoutes().size());
    }

    private String extractRouteUrl(String decoratorLine) {
        int open = decoratorLine.indexOf('(');
        if (open < 0) {
            return "/";
        }


        int close = findMatchingParen(decoratorLine, open);
        String args = (close > open)
                ? decoratorLine.substring(open + 1, close).trim()
                : decoratorLine.substring(open + 1).trim();


        int comma = findTopLevelComma(args);
        String first = comma > 0 ? args.substring(0, comma).trim() : args.trim();
        first = stripQuotes(first);
        return first.isEmpty() ? "/" : first;
    }

    private String extractFunctionName(String line) {
        if (!line.startsWith("def ")) {
            return null;
        }
        String after = line.substring(4).trim();
        int p = after.indexOf('(');
        return p > 0 ? after.substring(0, p).trim() : null;
    }


    private void extractTemplateVariablesFromSource() {
        log("extractTemplateVariablesFromSource...");
        if (isBlank(pythonFilePath)) {
            return;
        }

        try {
            String source = Files.readString(Path.of(pythonFilePath));
            int idx = 0;
            while ((idx = source.indexOf("render_template(", idx)) >= 0) {
                int open = idx + "render_template(".length();
                int close = findMatchingParen(source, open - 1);
                if (close < 0) {
                    idx = open;
                    continue;
                }
                String templateName = parseRenderTemplateArgs(
                        source.substring(open, close).trim());


                String enclosingFunc = findEnclosingFunction(source, idx);
                if (templateName != null && enclosingFunc != null) {
                    context.setTemplateForRoute(enclosingFunc, templateName);
                    log("Route→Template: " + enclosingFunc + "() → " + templateName);
                }

                idx = close + 1;
            }
        } catch (Exception e) {
            context.addWarning("Template variable extraction failed: " + e);
        }
    }


    private String parseRenderTemplateArgs(String args) {
        List<String> parts = splitTopLevel(args);
        if (parts.isEmpty()) {
            return null;
        }

        String templateName = stripQuotes(parts.get(0).trim());
        List<String> vars = new ArrayList<>();
        for (int i = 1; i < parts.size(); i++) {
            String p = parts.get(i).trim();
            int eq = findTopLevelEquals(p);
            if (eq > 0) {
                vars.add(p.substring(0, eq).trim());
            }
        }
        context.addTemplateVariables(templateName, vars);
        log("render_template → " + templateName + " " + vars);
        return templateName;
    }


    private String findEnclosingFunction(String source, int fromIdx) {
        String before = source.substring(0, fromIdx);
        Matcher m = Pattern.compile("def\\s+(\\w+)\\s*\\(").matcher(before);
        String last = null;
        while (m.find()) {
            last = m.group(1);
        }
        return last;
    }



    private void extractGlobalAssignmentsFromSource() {
        log("extractGlobalAssignmentsFromSource...");
        if (isBlank(pythonFilePath)) {
            return;
        }

        try {
            String source = Files.readString(Path.of(pythonFilePath));
            String[] lines = source.split("\n", -1);

            boolean insideFunction = false;
            int defIndent = -1;

            for (int i = 0; i < lines.length; i++) {
                String raw = lines[i];
                String line = raw.trim();
                int indent = countIndent(raw);

                if (line.startsWith("def ")) {
                    insideFunction = true;
                    defIndent = indent;
                    continue;
                }
                if (line.startsWith("@")) {
                    continue;
                }

                if (insideFunction && !line.isEmpty() && !line.startsWith("#")
                        && indent <= defIndent) {
                    insideFunction = false;
                }
                if (insideFunction) {
                    continue;
                }

                if (line.isEmpty() || line.startsWith("#")
                        || line.startsWith("from ") || line.startsWith("import ")
                        || line.startsWith("class ") || line.startsWith("def ")
                        || line.startsWith("@")) {
                    continue;
                }

                int eq = findTopLevelEquals(line);
                if (eq <= 0) {
                    continue;
                }

                String name = line.substring(0, eq).trim();
                if (name.contains(":")) {
                    name = name.substring(0, name.indexOf(':')).trim();
                }
                if (!isValidIdentifier(name)) {
                    continue;
                }
                if ("products".equals(name) || "app".equals(name)) {
                    continue;
                }

                String valuePart = line.substring(eq + 1).trim();
                if (valuePart.startsWith("[") || valuePart.startsWith("{")
                        || valuePart.startsWith("(")) {
                    StringBuilder sb = new StringBuilder(valuePart);
                    int depth = bracketDepth(valuePart);
                    int j = i + 1;
                    while (depth > 0 && j < lines.length) {
                        String next = lines[j].trim();
                        sb.append(' ').append(next);
                        depth += bracketDepth(next);
                        j++;
                    }
                    valuePart = normalizeWhitespace(sb.toString());
                }

                Object parsed = parsePythonValue(valuePart);
                context.addGlobalVariable(name, parsed);
                log("Global: " + name + " = " + parsed);
            }
        } catch (Exception e) {
            context.addWarning("Global assignment extraction failed: " + e);
        }
    }



    private void collectTemplateNames() {
        log("collectTemplateNames...");
        Set<String> set = new LinkedHashSet<>();

        File dir = new File(templatesDirectory);
        if (dir.isDirectory()) {
            File[] files = dir.listFiles((d, n) ->
                    n.endsWith(".html") || n.endsWith(".jinja"));
            if (files != null) {
                for (File f : files) {
                    if (f.isFile()) {
                        set.add(f.getName());
                    }
                }
            }
        } else {
            context.addWarning("Templates directory not found: " + templatesDirectory);
        }

        set.addAll(context.getTemplateVariables().keySet());


        File base = new File(templatesDirectory, "base.html");
        if (base.isFile()) {
            set.add("base.html");
        }

        for (String t : set) {
            context.addTemplateName(t);
        }
        log("Templates: " + context.getTemplateNames());
    }

    private void collectStaticFiles() {
        String[] candidates = {
                templatesDirectory + "/style.css",
                templatesDirectory + "/script.js",
                "static/style.css", "static/script.js",
                "src/static/style.css", "src/static/script.js"
        };
        for (String c : candidates) {
            if (new File(c).isFile()) {
                context.addStaticFile(c);
                log("Static: " + c);
            }
        }
    }



    private void parseTemplateASTs() {
        context.clearTemplateASTs();
        log("parseTemplateASTs...");

        for (String name : context.getTemplateNames()) {
            String path = templatesDirectory + "/" + name;
            File file = new File(path);
            if (!file.isFile()) {
                context.addWarning("Template not found: " + path);
                continue;
            }
            try {
                CharStream cs = CharStreams.fromFileName(path);
                TemplateLexer lexer = new TemplateLexer(cs);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                TemplateParser parser = new TemplateParser(tokens);
                ParseTree tree = parser.page();

                int errs = parser.getNumberOfSyntaxErrors();
                if (errs > 0) {
                    context.addWarning("Template '" + name + "' has " + errs
                            + " syntax errors — partial AST");
                }

                HtmlCssJinjaVisitor visitor = new HtmlCssJinjaVisitor();
                PageNode ast = (PageNode) visitor.visit(tree);
                if (ast != null) {
                    context.addTemplateAST(name, ast);
                    log("Parsed: " + name);
                } else {
                    context.addWarning("AST null for: " + name);
                }
            } catch (Exception e) {
                context.addWarning("Parse error '" + name + "': " + e);
            }
        }
        log("ASTs: " + context.getTemplateASTs().size());
    }



    private void discoverExtendsAndIncludes() {
        log("discoverExtendsAndIncludes...");
        Pattern extPat = Pattern.compile(
                "\\{\\%\\s*extends\\s+[\"']([^\"']+)[\"']\\s*\\%\\}");
        Pattern incPat = Pattern.compile(
                "\\{\\%\\s*include\\s+[\"']([^\"']+)[\"']\\s*\\%\\}");
        Pattern blkPat = Pattern.compile(
                "\\{\\%\\s*block\\s+(\\w+)\\s*\\%\\}");

        for (String name : context.getTemplateNames()) {
            String path = templatesDirectory + "/" + name;
            try {
                String content = Files.readString(Path.of(path));

                Matcher ext = extPat.matcher(content);
                if (ext.find()) {
                    context.setTemplateParent(name, ext.group(1));
                    log(name + " extends " + ext.group(1));
                }

                Matcher inc = incPat.matcher(content);
                while (inc.find()) {
                    context.addTemplateInclude(name, inc.group(1));
                }

                Matcher blk = blkPat.matcher(content);
                while (blk.find()) {
                    context.addTemplateBlock(name, blk.group(1));
                }
            } catch (Exception ignored) {
                // skip unreadable template
            }
        }
    }



    private void populateGlobalVariables() {
        context.addGlobalVariable("products", context.getProducts());
        log("Globals: " + context.getGlobalVariables().keySet());
    }



    private String extractAssignmentValue(String varName) {
        try {
            String source = Files.readString(Path.of(pythonFilePath));
            String[] lines = source.split("\n", -1);

            int start = -1;
            for (int i = 0; i < lines.length; i++) {
                String t = lines[i].trim();
                if (t.startsWith(varName + " =") || t.startsWith(varName + "=")
                        || t.matches(varName + "\\s*:\\s*\\w+\\s*=")) {
                    start = i;
                    break;
                }
            }
            if (start < 0) {
                return null;
            }

            String line = lines[start].trim();
            int eq = line.lastIndexOf('=');
            String valuePart = line.substring(eq + 1).trim();

            if (valuePart.startsWith("[") || valuePart.startsWith("{")
                    || valuePart.startsWith("(")) {
                StringBuilder sb = new StringBuilder(valuePart);
                int depth = bracketDepth(valuePart);
                int i = start + 1;
                while (depth > 0 && i < lines.length) {
                    String next = lines[i].trim();
                    sb.append(' ').append(next);
                    depth += bracketDepth(next);
                    i++;
                }
                valuePart = sb.toString();
            }
            return normalizeWhitespace(valuePart);
        } catch (Exception e) {
            context.addWarning("Cannot extract " + varName + ": " + e);
            return null;
        }
    }



    private Object parsePythonValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        String v = value.trim();

        if ("None".equals(v)) {
            return null;
        }
        if ("True".equals(v)) {
            return true;
        }
        if ("False".equals(v)) {
            return false;
        }

        if (v.startsWith("[") && v.endsWith("]")) {
            return parsePythonList(v);
        }
        if (v.startsWith("{") && v.endsWith("}")) {
            return parsePythonDict(v);
        }
        if (v.startsWith("(") && v.endsWith(")")) {
            return parsePythonList("[" + v.substring(1, v.length() - 1) + "]");
        }

        if ((v.startsWith("\"") && v.endsWith("\""))
                || (v.startsWith("'") && v.endsWith("'"))) {
            return v.substring(1, v.length() - 1);
        }

        if ((v.startsWith("f\"") && v.endsWith("\""))
                || (v.startsWith("f'") && v.endsWith("'"))) {
            return v.substring(2, v.length() - 1);
        }

        try {
            if (!v.contains(".") && !v.toLowerCase().contains("e")) {
                return Long.parseLong(v);
            }
            return Double.parseDouble(v);
        } catch (NumberFormatException e) {
            return v;
        }
    }

    private List<Object> parsePythonList(String listStr) {
        List<Object> result = new ArrayList<>();
        String inner = listStr.substring(1, listStr.length() - 1).trim();
        if (inner.isEmpty()) {
            return result;
        }

        for (String item : splitTopLevel(inner)) {
            String t = item.trim();
            if (t.isEmpty()) {
                continue;
            }
            if ("None".equals(t)) {
                result.add(null);
            } else {
                result.add(parsePythonValue(t));
            }
        }
        return result;
    }

    private Map<String, Object> parsePythonDict(String dictStr) {
        Map<String, Object> result = new LinkedHashMap<>();
        String inner = dictStr.substring(1, dictStr.length() - 1).trim();
        if (inner.isEmpty()) {
            return result;
        }

        for (String pair : splitTopLevel(inner)) {
            String t = pair.trim();
            if (t.isEmpty()) {
                continue;
            }
            int colon = findTopLevelColon(t);
            if (colon > 0) {
                String key = stripQuotes(t.substring(0, colon).trim());
                String val = t.substring(colon + 1).trim();
                result.put(key, parsePythonValue(val));
            }
        }
        return result;
    }


    private List<String> splitTopLevel(String s) {
        List<String> parts = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        int depth = 0;
        boolean inQuote = false;
        char qc = 0;

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' || c == '\'') {
                if (!inQuote) {
                    inQuote = true;
                    qc = c;
                } else if (c == qc && (i == 0 || s.charAt(i - 1) != '\\')) {
                    inQuote = false;
                }
            }
            if (!inQuote) {
                if (c == '[' || c == '{' || c == '(') {
                    depth++;
                } else if (c == ']' || c == '}' || c == ')') {
                    depth--;
                }
            }
            if (c == ',' && depth == 0 && !inQuote) {
                parts.add(cur.toString().trim());
                cur = new StringBuilder();
            } else {
                cur.append(c);
            }
        }
        String last = cur.toString().trim();
        if (!last.isEmpty()) {
            parts.add(last);
        }
        return parts;
    }

    private int findTopLevelColon(String s) {
        int depth = 0;
        boolean inQuote = false;
        char qc = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' || c == '\'') {
                if (!inQuote) {
                    inQuote = true;
                    qc = c;
                } else if (c == qc && (i == 0 || s.charAt(i - 1) != '\\')) {
                    inQuote = false;
                }
            }
            if (!inQuote) {
                if (c == '[' || c == '{' || c == '(') {
                    depth++;
                } else if (c == ']' || c == '}' || c == ')') {
                    depth--;
                } else if (c == ':' && depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int findTopLevelComma(String s) {
        int depth = 0;
        boolean inQuote = false;
        char qc = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' || c == '\'') {
                if (!inQuote) {
                    inQuote = true;
                    qc = c;
                } else if (c == qc && (i == 0 || s.charAt(i - 1) != '\\')) {
                    inQuote = false;
                }
            }
            if (!inQuote) {
                if (c == '[' || c == '{' || c == '(') {
                    depth++;
                } else if (c == ']' || c == '}' || c == ')') {
                    depth--;
                } else if (c == ',' && depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int findTopLevelEquals(String s) {
        int depth = 0;
        boolean inQuote = false;
        char qc = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' || c == '\'') {
                if (!inQuote) {
                    inQuote = true;
                    qc = c;
                } else if (c == qc && (i == 0 || s.charAt(i - 1) != '\\')) {
                    inQuote = false;
                }
            }
            if (!inQuote) {
                if (c == '[' || c == '{' || c == '(') {
                    depth++;
                } else if (c == ']' || c == '}' || c == ')') {
                    depth--;
                } else if (c == '=' && depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int findMatchingParen(String s, int openIndex) {
        if (openIndex < 0 || openIndex >= s.length()) {
            return -1;
        }

        int depth = 0;
        boolean inQuote = false;
        char qc = 0;

        for (int i = openIndex; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '"' || c == '\'') {
                if (!inQuote) {
                    inQuote = true;
                    qc = c;
                } else if (c == qc && (i == 0 || s.charAt(i - 1) != '\\')) {
                    inQuote = false;
                }
                continue;
            }

            if (inQuote) {
                continue;
            }

            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
                if (depth == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private int bracketDepth(String s) {
        int depth = 0;
        boolean inQuote = false;
        char qc = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"' || c == '\'') {
                if (!inQuote) {
                    inQuote = true;
                    qc = c;
                } else if (c == qc && (i == 0 || s.charAt(i - 1) != '\\')) {
                    inQuote = false;
                }
            } else if (!inQuote) {
                if (c == '[' || c == '{' || c == '(') {
                    depth++;
                } else if (c == ']' || c == '}' || c == ')') {
                    depth--;
                }
            }
        }
        return depth;
    }

    private String normalizeWhitespace(String value) {
        StringBuilder r = new StringBuilder();
        boolean inQuote = false;
        char qc = 0;
        boolean lastSpace = false;
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (c == '"' || c == '\'') {
                if (!inQuote) {
                    inQuote = true;
                    qc = c;
                    lastSpace = false;
                    r.append(c);
                } else if (c == qc && (i == 0 || value.charAt(i - 1) != '\\')) {
                    inQuote = false;
                    lastSpace = false;
                    r.append(c);
                } else {
                    lastSpace = false;
                    r.append(c);
                }
            } else if (inQuote) {
                lastSpace = false;
                r.append(c);
            } else if (Character.isWhitespace(c)) {
                if (!lastSpace) {
                    r.append(' ');
                    lastSpace = true;
                }
            } else {
                lastSpace = false;
                r.append(c);
            }
        }
        return r.toString().trim();
    }

    private int countIndent(String raw) {
        int n = 0;
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            if (c == ' ') {
                n++;
            } else if (c == '\t') {
                n += 4;
            } else {
                break;
            }
        }
        return n;
    }

    private boolean isValidIdentifier(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        if (!Character.isJavaIdentifierStart(name.charAt(0))) {
            return false;
        }
        for (int i = 1; i < name.length(); i++) {
            if (!Character.isJavaIdentifierPart(name.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    private String stripQuotes(String s) {
        if (s == null) {
            return "";
        }
        s = s.trim();
        if ((s.startsWith("\"") && s.endsWith("\""))
                || (s.startsWith("'") && s.endsWith("'"))) {
            return s.substring(1, s.length() - 1);
        }
        return s;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
package generation;

import AST.Core.PageNode;
import main.pythoncompiler.ast.*;
import java.util.*;


public class GenerationContext {

    private static final String DEFAULT_PYTHON_FILE_PATH = "";
    private static final String DEFAULT_TEMPLATES_DIRECTORY = "tests";
    private static final String DEFAULT_OUTPUT_DIRECTORY = "output";

    private final List<Map<String, Object>> products = new ArrayList<>();
    private final Map<String, String> routes = new LinkedHashMap<>();
    private final Map<String, List<String>> templateVariables = new LinkedHashMap<>();
    // route function name -> template file name, e.g. "index" -> "index.html"
    private final Map<String, String> templateToRoute = new LinkedHashMap<>();


    private final LinkedHashSet<String> templateNames = new LinkedHashSet<>();
    private final Map<String, String> templateParents = new LinkedHashMap<>();
    private final Map<String, List<String>> templateIncludes = new LinkedHashMap<>();
    private final Map<String, List<String>> templateBlocks = new LinkedHashMap<>();
    private final Map<String, PageNode> templateASTs = new LinkedHashMap<>();
    private final List<PageNode> pages = new ArrayList<>();


    private final Map<String, Object> globalVariables = new LinkedHashMap<>();
    private final Deque<Map<String, Object>> variableScopes = new ArrayDeque<>();
    private final Deque<Integer> loopIndices = new ArrayDeque<>();
    private String currentTemplate;


    private String pythonFilePath = DEFAULT_PYTHON_FILE_PATH;
    private String templatesDirectory = DEFAULT_TEMPLATES_DIRECTORY;
    private String outputDirectory = DEFAULT_OUTPUT_DIRECTORY;


    private final Map<String, String> generatedPages = new LinkedHashMap<>();
    private final LinkedHashSet<String> generatedTemplates = new LinkedHashSet<>();
    private final LinkedHashSet<String> staticFiles = new LinkedHashSet<>();
    private final LinkedHashSet<String> copiedFiles = new LinkedHashSet<>();


    private boolean semanticPassed = true;
    private boolean frozen = false;
    private final List<String> warnings = new ArrayList<>();
    private final List<String> logEntries = new ArrayList<>();

    private ASTNode pythonASTRoot;
    private String pythonAstJson;

    public ASTNode getPythonASTRoot() { return pythonASTRoot; }
    public void setPythonASTRoot(ASTNode root) { this.pythonASTRoot = root; }

    public String getPythonAstJson() { return pythonAstJson; }
    public void setPythonAstJson(String json) { this.pythonAstJson = json; }


    public void reset() {
        products.clear();
        routes.clear();
        templateVariables.clear();
        templateToRoute.clear();
        templateNames.clear();
        templateParents.clear();
        templateIncludes.clear();
        templateBlocks.clear();
        templateASTs.clear();
        pages.clear();
        globalVariables.clear();
        variableScopes.clear();
        loopIndices.clear();
        generatedPages.clear();
        generatedTemplates.clear();
        staticFiles.clear();
        copiedFiles.clear();
        warnings.clear();
        logEntries.clear();
        currentTemplate = null;
        semanticPassed = true;
        frozen = false;
        pythonFilePath = DEFAULT_PYTHON_FILE_PATH;
        templatesDirectory = DEFAULT_TEMPLATES_DIRECTORY;
        outputDirectory = DEFAULT_OUTPUT_DIRECTORY;
    }


    public void freeze() {
        frozen = true;
        logEntries.add("[GenerationContext] Frozen — core data is now read-only");
    }


    public boolean isFrozen() {
        return frozen;
    }

    private void ensureMutable() {
        if (frozen) {
            throw new IllegalStateException(
                    "GenerationContext is frozen — cannot mutate core data");
        }
    }


    public void validate() {
        if (templateNames.isEmpty()) {
            addWarning("No templates found");
        }
        if (templateASTs.isEmpty()) {
            addWarning("No template ASTs parsed");
        }
        if (routes.isEmpty()) {
            addWarning("No routes extracted");
        }

        for (var e : templateParents.entrySet()) {
            if (!hasTemplate(e.getValue())) {
                addWarning("Base template '" + e.getValue()
                        + "' not found (extends from " + e.getKey() + ")");
            }
        }

        for (var e : templateIncludes.entrySet()) {
            for (String inc : e.getValue()) {
                if (!hasTemplate(inc)) {
                    addWarning("Included template '" + inc
                            + "' not found (from " + e.getKey() + ")");
                }
            }
        }

        detectInheritanceCycles();
    }

    private void detectInheritanceCycles() {
        for (String start : templateParents.keySet()) {
            Set<String> visited = new LinkedHashSet<>();
            String cur = start;
            while (cur != null) {
                if (!visited.add(cur)) {
                    addWarning("Template inheritance cycle: "
                            + String.join(" → ", visited) + " → " + cur);
                    break;
                }
                cur = templateParents.get(cur);
            }
        }
    }


    public Object resolveVariable(String expr) {
        if (expr == null || expr.trim().isEmpty()) {
            return null;
        }
        expr = expr.trim();

        if ("loop.index0".equals(expr)) {
            return getCurrentLoopIndex();
        }
        if ("loop.index".equals(expr)) {
            Integer i = getCurrentLoopIndex();
            return i == null ? null : i + 1;
        }

        String[] parts = expr.split("\\.");
        Object current = resolveSimpleName(parts[0].trim());
        for (int i = 1; i < parts.length && current != null; i++) {
            if (current instanceof Map) {
                current = ((Map<?, ?>) current).get(parts[i].trim());
            } else {
                return null;
            }
        }
        return current;
    }

    private Object resolveSimpleName(String name) {
        for (Map<String, Object> scope : variableScopes) {
            if (scope.containsKey(name)) {
                return scope.get(name);
            }
        }
        return globalVariables.get(name);
    }

    public void pushScope(String varName, Object value, int loopIndex) {
        Map<String, Object> scope = new LinkedHashMap<>();
        if (varName != null) {
            scope.put(varName, value);
        }
        variableScopes.push(scope);
        loopIndices.push(loopIndex);
    }


    public void popScope() {
        if (!variableScopes.isEmpty()) {
            variableScopes.pop();
        }
        if (!loopIndices.isEmpty()) {
            loopIndices.pop();
        }
    }


    public Integer getCurrentLoopIndex() {
        return loopIndices.isEmpty() ? null : loopIndices.peek();
    }



    public boolean hasVariable(String name) {
        return resolveVariable(name) != null;
    }


    public boolean hasRoute(String url) {
        return routes.containsKey(url);
    }


    public boolean hasTemplate(String name) {
        return templateNames.contains(name) || templateASTs.containsKey(name);
    }


    public boolean hasProducts() {
        return !products.isEmpty();
    }


    public void setCurrentTemplate(String name) {
        currentTemplate = name;
    }


    public String getCurrentTemplate() {
        return currentTemplate;
    }


    public void markGenerated(String template) {
        if (template != null) {
            generatedTemplates.add(template);
        }
    }


    public Set<String> getGeneratedTemplates() {
        return Collections.unmodifiableSet(generatedTemplates);
    }


    public Map<String, Object> getProductByName(String name) {
        if (name == null) {
            return null;
        }
        for (Map<String, Object> p : products) {
            if (name.equals(String.valueOf(p.get("name")))) {
                return new LinkedHashMap<>(p);
            }
        }
        return null;
    }


    public String getRouteFunction(String url) {
        return routes.get(url);
    }

    public void addProduct(Map<String, Object> product) {
        ensureMutable();
        if (product == null) {
            return;
        }
        String key = String.valueOf(product.get("name")) + "|"
                + String.valueOf(product.get("price"));
        for (Map<String, Object> existing : products) {
            String ek = String.valueOf(existing.get("name")) + "|"
                    + String.valueOf(existing.get("price"));
            if (key.equals(ek)) {
                return;
            }
        }
        products.add(new LinkedHashMap<>(product));
    }


    public List<Map<String, Object>> getProducts() {
        List<Map<String, Object>> copy = new ArrayList<>();
        for (Map<String, Object> p : products) {
            copy.add(new LinkedHashMap<>(p));
        }
        return Collections.unmodifiableList(copy);
    }


    public void addTemplateAST(String name, PageNode ast) {
        ensureMutable();
        if (name != null && ast != null) {
            templateASTs.put(name, ast);
            if (!pages.contains(ast)) {
                pages.add(ast);
            }
        }
    }


    public PageNode getTemplate(String name) {
        return templateASTs.get(name);
    }


    public Map<String, PageNode> getTemplateASTs() {
        return Collections.unmodifiableMap(templateASTs);
    }


    public void clearTemplateASTs() {
        ensureMutable();
        templateASTs.clear();
        pages.clear();
    }


    public void addRoute(String url, String function) {
        ensureMutable();
        if (url != null && function != null) {
            routes.put(url.trim(), function.trim());
        }
    }

    public Map<String, String> getRoutes() {
        return Collections.unmodifiableMap(routes);
    }


    public void setTemplateForRoute(String routeFunction, String templateName) {
        ensureMutable();
        if (routeFunction != null && templateName != null) {
            templateToRoute.put(routeFunction.trim(), templateName.trim());
        }
    }


    public String getTemplateForRoute(String routeFunction) {
        return templateToRoute.get(routeFunction);
    }

    public Map<String, String> getTemplateToRoute() {
        return Collections.unmodifiableMap(templateToRoute);
    }

    public void addGlobalVariable(String name, Object value) {
        ensureMutable();
        if (name == null) {
            return;
        }
        if (value instanceof List) {
            globalVariables.put(name, new ArrayList<>((List<?>) value));
        } else if (value instanceof Map) {
            globalVariables.put(name, new LinkedHashMap<>((Map<?, ?>) value));
        } else {
            globalVariables.put(name, value);
        }
    }


    public Object getGlobalVariable(String name) {
        return globalVariables.get(name);
    }


    public Object getVariable(String name) {
        return getGlobalVariable(name);
    }


    public Map<String, Object> getGlobalVariables() {
        Map<String, Object> copy = new LinkedHashMap<>();
        for (var e : globalVariables.entrySet()) {
            Object v = e.getValue();
            if (v instanceof List) {
                copy.put(e.getKey(),
                        Collections.unmodifiableList(new ArrayList<>((List<?>) v)));
            } else if (v instanceof Map) {
                copy.put(e.getKey(),
                        Collections.unmodifiableMap(new LinkedHashMap<>((Map<?, ?>) v)));
            } else {
                copy.put(e.getKey(), v);
            }
        }
        return Collections.unmodifiableMap(copy);
    }


    public void addGeneratedPage(String fileName, String html) {
        if (fileName != null && html != null) {
            generatedPages.put(fileName, html);
        }
    }


    public Map<String, String> getGeneratedPages() {
        return Collections.unmodifiableMap(generatedPages);
    }


    public void addOutput(String pageName, String html) {
        addGeneratedPage(pageName, html);
    }


    public Map<String, String> getOutputHtml() {
        return getGeneratedPages();
    }

    public void addTemplateName(String name) {
        ensureMutable();
        if (name != null) {
            templateNames.add(name);
        }
    }


    public List<String> getTemplateNames() {
        return new ArrayList<>(templateNames);
    }


    public List<PageNode> getPages() {
        return Collections.unmodifiableList(pages);
    }

    public List<PageNode> getTemplates() {
        return getPages();
    }

    public void setTemplateParent(String child, String parent) {
        ensureMutable();
        if (child != null && parent != null) {
            templateParents.put(child, parent);
        }
    }


    public String getTemplateParent(String child) {
        return templateParents.get(child);
    }


    public Map<String, String> getTemplateParents() {
        return Collections.unmodifiableMap(templateParents);
    }


    public void addTemplateInclude(String template, String include) {
        ensureMutable();
        if (template == null || include == null) {
            return;
        }
        templateIncludes.computeIfAbsent(template, k -> new ArrayList<>()).add(include);
    }


    public List<String> getTemplateIncludes(String template) {
        List<String> list = templateIncludes.get(template);
        return list == null ? List.of() : List.copyOf(list);
    }


    public void addTemplateBlock(String template, String blockName) {
        ensureMutable();
        if (template == null || blockName == null) {
            return;
        }
        templateBlocks.computeIfAbsent(template, k -> new ArrayList<>()).add(blockName);
    }


    public List<String> getTemplateBlocks(String template) {
        List<String> list = templateBlocks.get(template);
        return list == null ? List.of() : List.copyOf(list);
    }

    public void addTemplateVariables(String template, List<String> vars) {
        ensureMutable();
        templateVariables.put(template,
                vars != null ? new ArrayList<>(vars) : new ArrayList<>());
    }


    public Map<String, List<String>> getTemplateVariables() {
        Map<String, List<String>> copy = new LinkedHashMap<>();
        for (var e : templateVariables.entrySet()) {
            copy.put(e.getKey(), List.copyOf(e.getValue()));
        }
        return Collections.unmodifiableMap(copy);
    }


    public void addStaticFile(String path) {
        ensureMutable();
        if (path != null) {
            staticFiles.add(path);
        }
    }


    public Set<String> getStaticFiles() {
        return Collections.unmodifiableSet(staticFiles);
    }


    public void addCopiedFile(String path) {
        if (path != null) {
            copiedFiles.add(path);
        }
    }


    public List<String> getCopiedFiles() {
        return new ArrayList<>(copiedFiles);
    }


    public String getPythonFilePath() {
        return pythonFilePath;
    }

    public void setPythonFilePath(String path) {
        ensureMutable();
        pythonFilePath = path;
    }

    public String getTemplatesDirectory() {
        return templatesDirectory;
    }

    public void setTemplatesDirectory(String dir) {
        ensureMutable();
        templatesDirectory = dir;
    }


    public String getOutputDirectory() {
        return outputDirectory;
    }


    public void setOutputDirectory(String dir) {
        ensureMutable();
        outputDirectory = dir != null ? dir : DEFAULT_OUTPUT_DIRECTORY;
    }


    public boolean isSemanticPassed() {
        return semanticPassed;
    }


    public void setSemanticPassed(boolean value) {
        semanticPassed = value;
    }


    public void addWarning(String warning) {
        if (warning != null) {
            warnings.add(warning);
        }
    }


    public List<String> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }


    public void addLog(String entry) {
        if (entry != null) {
            logEntries.add(entry);
        }
    }


    public List<String> getLogEntries() {
        return Collections.unmodifiableList(logEntries);
    }


    public void printSummary() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println(" Context Summary");
        System.out.println("=".repeat(50));
        System.out.println(" Products  : " + products.size());
        System.out.println(" Routes    : " + routes.size());
        System.out.println(" Templates : " + templateNames.size());
        System.out.println(" ASTs      : " + templateASTs.size());
        System.out.println(" Blocks    : "
                + templateBlocks.values().stream().mapToInt(List::size).sum());
        System.out.println(" Includes  : "
                + templateIncludes.values().stream().mapToInt(List::size).sum());
        System.out.println(" Globals   : " + globalVariables.size());
        System.out.println(" Static    : " + staticFiles.size());
        System.out.println(" Warnings  : " + warnings.size());
        System.out.println(" Logs      : " + logEntries.size());
        System.out.println(" Frozen    : " + frozen);
        System.out.println(" Semantic  : " + semanticPassed);
        System.out.println("=".repeat(50));
    }


    public void printContext() {
        printSummary();
        System.out.println("\n Products detail:");
        for (int i = 0; i < products.size(); i++) {
            System.out.println("  [" + i + "] " + products.get(i));
        }
        System.out.println("\n Routes:");
        for (var e : routes.entrySet()) {
            System.out.println("  " + e.getKey() + " → " + e.getValue() + "()");
        }
        System.out.println("\n Route → Template:");
        for (var e : templateToRoute.entrySet()) {
            System.out.println("  " + e.getKey() + "() → " + e.getValue());
        }
        System.out.println("\n Templates:");
        for (String n : templateNames) {
            String parent = templateParents.get(n);
            System.out.println("  - " + n + (parent != null ? " extends " + parent : ""));
        }
        if (!warnings.isEmpty()) {
            System.out.println("\n Warnings:");
            for (String w : warnings) {
                System.out.println("  - " + w);
            }
        }
    }
}
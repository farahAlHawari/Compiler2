package generation;

import AST.Core.PageNode;

import java.util.*;

/**
 * Shared data container for the Code Generation phase.
 * Built by {@link ContextBuilder} (Person 1).
 * After {@link #freeze()}, core data becomes read-only;
 * Person 2/3/4 may still add generated pages, warnings, and logs.
 */
public class GenerationContext {

    private static final String DEFAULT_PYTHON_FILE_PATH = "";
    private static final String DEFAULT_TEMPLATES_DIRECTORY = "tests";
    private static final String DEFAULT_OUTPUT_DIRECTORY = "output";

    // ---------- Python data ----------
    private final List<Map<String, Object>> products = new ArrayList<>();
    private final Map<String, String> routes = new LinkedHashMap<>();
    private final Map<String, List<String>> templateVariables = new LinkedHashMap<>();
    // route function name -> template file name, e.g. "index" -> "index.html"
    private final Map<String, String> templateToRoute = new LinkedHashMap<>();

    // ---------- Templates ----------
    private final LinkedHashSet<String> templateNames = new LinkedHashSet<>();
    private final Map<String, String> templateParents = new LinkedHashMap<>();
    private final Map<String, List<String>> templateIncludes = new LinkedHashMap<>();
    private final Map<String, List<String>> templateBlocks = new LinkedHashMap<>();
    private final Map<String, PageNode> templateASTs = new LinkedHashMap<>();
    private final List<PageNode> pages = new ArrayList<>();

    // ---------- Globals + scopes ----------
    private final Map<String, Object> globalVariables = new LinkedHashMap<>();
    private final Deque<Map<String, Object>> variableScopes = new ArrayDeque<>();
    private final Deque<Integer> loopIndices = new ArrayDeque<>();
    private String currentTemplate;

    // ---------- Paths ----------
    private String pythonFilePath = DEFAULT_PYTHON_FILE_PATH;
    private String templatesDirectory = DEFAULT_TEMPLATES_DIRECTORY;
    private String outputDirectory = DEFAULT_OUTPUT_DIRECTORY;

    // ---------- Output ----------
    private final Map<String, String> generatedPages = new LinkedHashMap<>();
    private final LinkedHashSet<String> generatedTemplates = new LinkedHashSet<>();
    private final LinkedHashSet<String> staticFiles = new LinkedHashSet<>();
    private final LinkedHashSet<String> copiedFiles = new LinkedHashSet<>();

    // ---------- Status ----------
    private boolean semanticPassed = true;
    private boolean frozen = false;
    private final List<String> warnings = new ArrayList<>();
    private final List<String> logEntries = new ArrayList<>();

    // =====================================================================
    // Lifecycle
    // =====================================================================

    /**
     * Clears all data and restores defaults.
     * Used by {@link ContextBuilder#build()} before a fresh extraction.
     * Always allowed (even if previously frozen) so a new build can start.
     */
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

    /**
     * Locks core data against further mutation from ContextBuilder APIs.
     * Generated pages, warnings, logs, and copied files remain writable
     * for Person 2/3/4.
     */
    public void freeze() {
        frozen = true;
        logEntries.add("[GenerationContext] Frozen — core data is now read-only");
    }

    /** @return whether {@link #freeze()} has been called */
    public boolean isFrozen() {
        return frozen;
    }

    private void ensureMutable() {
        if (frozen) {
            throw new IllegalStateException(
                    "GenerationContext is frozen — cannot mutate core data");
        }
    }

    /**
     * Runs post-build checks and records non-fatal warnings:
     * missing templates, missing extends/includes, inheritance cycles.
     */
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

    // =====================================================================
    // Variable resolution (Person 3)
    // =====================================================================

    /**
     * Resolves a Jinja expression against scopes then globals.
     * Supports nested dotted paths such as {@code user.address.city}.
     * Special cases: {@code loop.index0}, {@code loop.index}.
     *
     * @param expr expression text without {@code {{ }}}, e.g. {@code p.name}
     * @return resolved value, or {@code null} if not found
     */
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

    /**
     * Pushes a for-loop scope.
     *
     * @param varName    loop variable name (e.g. {@code p})
     * @param value      current item
     * @param loopIndex  zero-based index
     */
    public void pushScope(String varName, Object value, int loopIndex) {
        Map<String, Object> scope = new LinkedHashMap<>();
        if (varName != null) {
            scope.put(varName, value);
        }
        variableScopes.push(scope);
        loopIndices.push(loopIndex);
    }

    /** Pops the innermost for-loop scope. */
    public void popScope() {
        if (!variableScopes.isEmpty()) {
            variableScopes.pop();
        }
        if (!loopIndices.isEmpty()) {
            loopIndices.pop();
        }
    }

    /** @return current {@code loop.index0}, or {@code null} if not in a loop */
    public Integer getCurrentLoopIndex() {
        return loopIndices.isEmpty() ? null : loopIndices.peek();
    }

    // =====================================================================
    // Helpers
    // =====================================================================

    /** @return true if {@link #resolveVariable(String)} would not return null */
    public boolean hasVariable(String name) {
        return resolveVariable(name) != null;
    }

    /** @return true if a route URL is registered */
    public boolean hasRoute(String url) {
        return routes.containsKey(url);
    }

    /** @return true if template name is known (file list or AST map) */
    public boolean hasTemplate(String name) {
        return templateNames.contains(name) || templateASTs.containsKey(name);
    }

    /** @return true if at least one product exists */
    public boolean hasProducts() {
        return !products.isEmpty();
    }

    /** Sets the template currently being rendered (Person 3). */
    public void setCurrentTemplate(String name) {
        currentTemplate = name;
    }

    /** @return template name currently being rendered, or null */
    public String getCurrentTemplate() {
        return currentTemplate;
    }

    /** Marks a template as successfully generated (Person 4). */
    public void markGenerated(String template) {
        if (template != null) {
            generatedTemplates.add(template);
        }
    }

    /** @return unmodifiable set of generated template names */
    public Set<String> getGeneratedTemplates() {
        return Collections.unmodifiableSet(generatedTemplates);
    }

    /**
     * Looks up a product by its {@code name} field.
     *
     * @param name product name
     * @return defensive copy of the product map, or null
     */
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

    /** @return function name for the given route URL, or null */
    public String getRouteFunction(String url) {
        return routes.get(url);
    }

    // =====================================================================
    // Products
    // =====================================================================

    /**
     * Adds a product. Skips duplicates that share the same name+price pair.
     *
     * @param product product fields (name, price, image, details, ...)
     */
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

    /**
     * @return unmodifiable deep-ish copy of products
     *         (each map is copied; nested lists/maps are shallow)
     */
    public List<Map<String, Object>> getProducts() {
        List<Map<String, Object>> copy = new ArrayList<>();
        for (Map<String, Object> p : products) {
            copy.add(new LinkedHashMap<>(p));
        }
        return Collections.unmodifiableList(copy);
    }

    // =====================================================================
    // Template AST
    // =====================================================================

    /**
     * Registers a parsed template AST.
     *
     * @param name template file name (e.g. {@code index.html})
     * @param ast  root {@link PageNode}
     */
    public void addTemplateAST(String name, PageNode ast) {
        ensureMutable();
        if (name != null && ast != null) {
            templateASTs.put(name, ast);
            if (!pages.contains(ast)) {
                pages.add(ast);
            }
        }
    }

    /**
     * Returns the AST for a template name.
     *
     * @param name template file name
     * @return PageNode or null
     */
    public PageNode getTemplate(String name) {
        return templateASTs.get(name);
    }

    /** @return unmodifiable map of all template ASTs */
    public Map<String, PageNode> getTemplateASTs() {
        return Collections.unmodifiableMap(templateASTs);
    }

    /** Clears ASTs and the pages list (used at the start of parsing). */
    public void clearTemplateASTs() {
        ensureMutable();
        templateASTs.clear();
        pages.clear();
    }

    // =====================================================================
    // Routes
    // =====================================================================

    /**
     * Registers a route mapping.
     *
     * @param url      path such as {@code /} or {@code /details/<int:i>}
     * @param function Python view function name
     */
    public void addRoute(String url, String function) {
        ensureMutable();
        if (url != null && function != null) {
            routes.put(url.trim(), function.trim());
        }
    }

    /** @return unmodifiable map URL → function name */
    public Map<String, String> getRoutes() {
        return Collections.unmodifiableMap(routes);
    }

    // =====================================================================
    // Route → Template mapping
    // =====================================================================

    /**
     * Records which route (view function) renders which template.
     * Inferred by matching a {@code render_template(...)} call site to its
     * enclosing {@code def} function.
     *
     * @param routeFunction Python view function name (e.g. {@code index})
     * @param templateName  template file rendered by that function (e.g. {@code index.html})
     */
    public void setTemplateForRoute(String routeFunction, String templateName) {
        ensureMutable();
        if (routeFunction != null && templateName != null) {
            templateToRoute.put(routeFunction.trim(), templateName.trim());
        }
    }

    /**
     * @param routeFunction Python view function name
     * @return template file rendered by that function, or null
     */
    public String getTemplateForRoute(String routeFunction) {
        return templateToRoute.get(routeFunction);
    }

    /** @return unmodifiable map of route function name → template file name */
    public Map<String, String> getTemplateToRoute() {
        return Collections.unmodifiableMap(templateToRoute);
    }

    // =====================================================================
    // Globals
    // =====================================================================

    /**
     * Adds a global variable visible to Jinja resolution.
     * Lists and maps are stored as defensive copies.
     *
     * @param name  variable name
     * @param value any value (may be null)
     */
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

    /** @return global variable by exact name, or null */
    public Object getGlobalVariable(String name) {
        return globalVariables.get(name);
    }

    /**
     * Alias matching the original task spec's naming ({@code getVariable}).
     *
     * @param name variable name
     * @return global variable by exact name, or null
     * @see #getGlobalVariable(String)
     */
    public Object getVariable(String name) {
        return getGlobalVariable(name);
    }

    /** @return unmodifiable view of globals (collections wrapped as unmodifiable) */
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

    // =====================================================================
    // Generated pages (writable after freeze)
    // =====================================================================

    /**
     * Stores generated HTML for a page.
     * Allowed after {@link #freeze()}.
     *
     * @param fileName output file name (e.g. {@code index.html})
     * @param html     full HTML string
     */
    public void addGeneratedPage(String fileName, String html) {
        if (fileName != null && html != null) {
            generatedPages.put(fileName, html);
        }
    }

    /** @return unmodifiable map of generated HTML pages */
    public Map<String, String> getGeneratedPages() {
        return Collections.unmodifiableMap(generatedPages);
    }

    /**
     * Alias matching the original task spec's naming ({@code addOutput}).
     *
     * @param pageName output file name (e.g. {@code index.html})
     * @param html     full HTML string
     * @see #addGeneratedPage(String, String)
     */
    public void addOutput(String pageName, String html) {
        addGeneratedPage(pageName, html);
    }

    /**
     * Alias matching the original task spec's naming ({@code getOutputHtml}).
     *
     * @return unmodifiable map of generated HTML pages
     * @see #getGeneratedPages()
     */
    public Map<String, String> getOutputHtml() {
        return getGeneratedPages();
    }

    // =====================================================================
    // Template names / relations
    // =====================================================================

    /**
     * Registers a template file name.
     *
     * @param name file name including extension
     */
    public void addTemplateName(String name) {
        ensureMutable();
        if (name != null) {
            templateNames.add(name);
        }
    }

    /** @return ordered copy of known template names */
    public List<String> getTemplateNames() {
        return new ArrayList<>(templateNames);
    }

    /** @return unmodifiable ordered list of parsed PageNodes */
    public List<PageNode> getPages() {
        return Collections.unmodifiableList(pages);
    }

    /**
     * Alias matching the original task spec's naming ({@code getTemplates}).
     *
     * @return unmodifiable ordered list of parsed PageNodes
     * @see #getPages()
     */
    public List<PageNode> getTemplates() {
        return getPages();
    }

    /**
     * Records {@code {% extends "parent" %}} relationship.
     *
     * @param child  child template name
     * @param parent parent template name
     */
    public void setTemplateParent(String child, String parent) {
        ensureMutable();
        if (child != null && parent != null) {
            templateParents.put(child, parent);
        }
    }

    /** @return parent template name, or null */
    public String getTemplateParent(String child) {
        return templateParents.get(child);
    }

    /** @return unmodifiable child → parent map */
    public Map<String, String> getTemplateParents() {
        return Collections.unmodifiableMap(templateParents);
    }

    /**
     * Records {@code {% include "file" %}} usage.
     *
     * @param template template that contains the include
     * @param include  included file name
     */
    public void addTemplateInclude(String template, String include) {
        ensureMutable();
        if (template == null || include == null) {
            return;
        }
        templateIncludes.computeIfAbsent(template, k -> new ArrayList<>()).add(include);
    }

    /** @return unmodifiable list of includes for a template */
    public List<String> getTemplateIncludes(String template) {
        List<String> list = templateIncludes.get(template);
        return list == null ? List.of() : List.copyOf(list);
    }

    /**
     * Records a {@code {% block name %}} found in a template.
     *
     * @param template  template file name
     * @param blockName block identifier
     */
    public void addTemplateBlock(String template, String blockName) {
        ensureMutable();
        if (template == null || blockName == null) {
            return;
        }
        templateBlocks.computeIfAbsent(template, k -> new ArrayList<>()).add(blockName);
    }

    /** @return unmodifiable list of block names in a template */
    public List<String> getTemplateBlocks(String template) {
        List<String> list = templateBlocks.get(template);
        return list == null ? List.of() : List.copyOf(list);
    }

    // =====================================================================
    // Template variables
    // =====================================================================

    /**
     * Stores variables passed to a template via {@code render_template(...)}.
     *
     * @param template template file name
     * @param vars     variable names (may be null → empty)
     */
    public void addTemplateVariables(String template, List<String> vars) {
        ensureMutable();
        templateVariables.put(template,
                vars != null ? new ArrayList<>(vars) : new ArrayList<>());
    }

    /** @return unmodifiable deep copy of template → variable names */
    public Map<String, List<String>> getTemplateVariables() {
        Map<String, List<String>> copy = new LinkedHashMap<>();
        for (var e : templateVariables.entrySet()) {
            copy.put(e.getKey(), List.copyOf(e.getValue()));
        }
        return Collections.unmodifiableMap(copy);
    }

    // =====================================================================
    // Static / copied files (writable after freeze)
    // =====================================================================

    /**
     * Registers a static asset path to be copied by Person 4.
     *
     * @param path relative file path
     */
    public void addStaticFile(String path) {
        ensureMutable();
        if (path != null) {
            staticFiles.add(path);
        }
    }

    /** @return unmodifiable set of static file paths */
    public Set<String> getStaticFiles() {
        return Collections.unmodifiableSet(staticFiles);
    }

    /**
     * Records a file that was actually copied to output.
     * Allowed after {@link #freeze()}.
     *
     * @param path destination or source path
     */
    public void addCopiedFile(String path) {
        if (path != null) {
            copiedFiles.add(path);
        }
    }

    /** @return ordered copy of copied file paths */
    public List<String> getCopiedFiles() {
        return new ArrayList<>(copiedFiles);
    }

    // =====================================================================
    // Paths
    // =====================================================================

    /** @return path of the Flask/Python source file */
    public String getPythonFilePath() {
        return pythonFilePath;
    }

    /**
     * Sets the Python source path used by ContextBuilder.
     *
     * @param path file path
     */
    public void setPythonFilePath(String path) {
        ensureMutable();
        pythonFilePath = path;
    }

    /** @return templates directory path */
    public String getTemplatesDirectory() {
        return templatesDirectory;
    }

    /**
     * Sets the templates directory.
     *
     * @param dir directory path
     */
    public void setTemplatesDirectory(String dir) {
        ensureMutable();
        templatesDirectory = dir;
    }

    /** @return output directory for generated HTML */
    public String getOutputDirectory() {
        return outputDirectory;
    }

    /**
     * Sets the output directory (defaults to {@code output} if null).
     *
     * @param dir directory path
     */
    public void setOutputDirectory(String dir) {
        ensureMutable();
        outputDirectory = dir != null ? dir : DEFAULT_OUTPUT_DIRECTORY;
    }

    // =====================================================================
    // Status
    // =====================================================================

    /** @return true if semantic analysis reported zero errors */
    public boolean isSemanticPassed() {
        return semanticPassed;
    }

    /**
     * Records whether semantic analysis succeeded.
     * Set by Main before generation starts.
     *
     * @param value true if no semantic errors
     */
    public void setSemanticPassed(boolean value) {
        semanticPassed = value;
    }

    // =====================================================================
    // Warnings / Logs (writable after freeze)
    // =====================================================================

    /**
     * Appends a non-fatal generation warning.
     * Allowed after {@link #freeze()}.
     *
     * @param warning message text
     */
    public void addWarning(String warning) {
        if (warning != null) {
            warnings.add(warning);
        }
    }

    /** @return unmodifiable list of warnings */
    public List<String> getWarnings() {
        return Collections.unmodifiableList(warnings);
    }

    /**
     * Appends a log line for {@code generation_log.txt}.
     * Allowed after {@link #freeze()}.
     *
     * @param entry log message
     */
    public void addLog(String entry) {
        if (entry != null) {
            logEntries.add(entry);
        }
    }

    /** @return unmodifiable list of log entries */
    public List<String> getLogEntries() {
        return Collections.unmodifiableList(logEntries);
    }

    // =====================================================================
    // Debug
    // =====================================================================

    /** Prints a short statistics summary to stdout. */
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

    /** Prints summary plus products, routes, templates, and warnings. */
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
//package generation;
//
//import AST.Core.PageNode;
//
//import java.util.*;
//
///**
// * Shared data container for the Code Generation phase.
// * Built by {@link ContextBuilder} (Person 1).
// * After {@link #freeze()}, core data becomes read-only;
// * Person 2/3/4 may still add generated pages, warnings, and logs.
// */
//public class GenerationContext {
//
//    private static final String DEFAULT_PYTHON_FILE_PATH = "";
//    private static final String DEFAULT_TEMPLATES_DIRECTORY = "tests";
//    private static final String DEFAULT_OUTPUT_DIRECTORY = "output";
//
//    // ---------- Python data ----------
//    private final List<Map<String, Object>> products = new ArrayList<>();
//    private final Map<String, String> routes = new LinkedHashMap<>();
//    private final Map<String, List<String>> templateVariables = new LinkedHashMap<>();
//
//    // ---------- Templates ----------
//    private final LinkedHashSet<String> templateNames = new LinkedHashSet<>();
//    private final Map<String, String> templateParents = new LinkedHashMap<>();
//    private final Map<String, List<String>> templateIncludes = new LinkedHashMap<>();
//    private final Map<String, List<String>> templateBlocks = new LinkedHashMap<>();
//    private final Map<String, PageNode> templateASTs = new LinkedHashMap<>();
//    private final List<PageNode> pages = new ArrayList<>();
//
//    // ---------- Globals + scopes ----------
//    private final Map<String, Object> globalVariables = new LinkedHashMap<>();
//    private final Deque<Map<String, Object>> variableScopes = new ArrayDeque<>();
//    private final Deque<Integer> loopIndices = new ArrayDeque<>();
//    private String currentTemplate;
//
//    // ---------- Paths ----------
//    private String pythonFilePath = DEFAULT_PYTHON_FILE_PATH;
//    private String templatesDirectory = DEFAULT_TEMPLATES_DIRECTORY;
//    private String outputDirectory = DEFAULT_OUTPUT_DIRECTORY;
//
//    // ---------- Output ----------
//    private final Map<String, String> generatedPages = new LinkedHashMap<>();
//    private final LinkedHashSet<String> generatedTemplates = new LinkedHashSet<>();
//    private final LinkedHashSet<String> staticFiles = new LinkedHashSet<>();
//    private final LinkedHashSet<String> copiedFiles = new LinkedHashSet<>();
//
//    // ---------- Status ----------
//    private boolean semanticPassed = true;
//    private boolean frozen = false;
//    private final List<String> warnings = new ArrayList<>();
//    private final List<String> logEntries = new ArrayList<>();
//
//    // =====================================================================
//    // Lifecycle
//    // =====================================================================
//
//    /**
//     * Clears all data and restores defaults.
//     * Used by {@link ContextBuilder#build()} before a fresh extraction.
//     * Always allowed (even if previously frozen) so a new build can start.
//     */
//    public void reset() {
//        products.clear();
//        routes.clear();
//        templateVariables.clear();
//        templateNames.clear();
//        templateParents.clear();
//        templateIncludes.clear();
//        templateBlocks.clear();
//        templateASTs.clear();
//        pages.clear();
//        globalVariables.clear();
//        variableScopes.clear();
//        loopIndices.clear();
//        generatedPages.clear();
//        generatedTemplates.clear();
//        staticFiles.clear();
//        copiedFiles.clear();
//        warnings.clear();
//        logEntries.clear();
//        currentTemplate = null;
//        semanticPassed = true;
//        frozen = false;
//        pythonFilePath = DEFAULT_PYTHON_FILE_PATH;
//        templatesDirectory = DEFAULT_TEMPLATES_DIRECTORY;
//        outputDirectory = DEFAULT_OUTPUT_DIRECTORY;
//    }
//
//    /**
//     * Locks core data against further mutation from ContextBuilder APIs.
//     * Generated pages, warnings, logs, and copied files remain writable
//     * for Person 2/3/4.
//     */
//    public void freeze() {
//        frozen = true;
//        logEntries.add("[GenerationContext] Frozen — core data is now read-only");
//    }
//
//    /** @return whether {@link #freeze()} has been called */
//    public boolean isFrozen() {
//        return frozen;
//    }
//
//    private void ensureMutable() {
//        if (frozen) {
//            throw new IllegalStateException(
//                    "GenerationContext is frozen — cannot mutate core data");
//        }
//    }
//
//    /**
//     * Runs post-build checks and records non-fatal warnings:
//     * missing templates, missing extends/includes, inheritance cycles.
//     */
//    public void validate() {
//        if (templateNames.isEmpty()) {
//            addWarning("No templates found");
//        }
//        if (templateASTs.isEmpty()) {
//            addWarning("No template ASTs parsed");
//        }
//        if (routes.isEmpty()) {
//            addWarning("No routes extracted");
//        }
//
//        for (var e : templateParents.entrySet()) {
//            if (!hasTemplate(e.getValue())) {
//                addWarning("Base template '" + e.getValue()
//                        + "' not found (extends from " + e.getKey() + ")");
//            }
//        }
//
//        for (var e : templateIncludes.entrySet()) {
//            for (String inc : e.getValue()) {
//                if (!hasTemplate(inc)) {
//                    addWarning("Included template '" + inc
//                            + "' not found (from " + e.getKey() + ")");
//                }
//            }
//        }
//
//        detectInheritanceCycles();
//    }
//
//    private void detectInheritanceCycles() {
//        for (String start : templateParents.keySet()) {
//            Set<String> visited = new LinkedHashSet<>();
//            String cur = start;
//            while (cur != null) {
//                if (!visited.add(cur)) {
//                    addWarning("Template inheritance cycle: "
//                            + String.join(" → ", visited) + " → " + cur);
//                    break;
//                }
//                cur = templateParents.get(cur);
//            }
//        }
//    }
//
//    // =====================================================================
//    // Variable resolution (Person 3)
//    // =====================================================================
//
//    /**
//     * Resolves a Jinja expression against scopes then globals.
//     * Supports nested dotted paths such as {@code user.address.city}.
//     * Special cases: {@code loop.index0}, {@code loop.index}.
//     *
//     * @param expr expression text without {@code {{ }}}, e.g. {@code p.name}
//     * @return resolved value, or {@code null} if not found
//     */
//    public Object resolveVariable(String expr) {
//        if (expr == null || expr.trim().isEmpty()) {
//            return null;
//        }
//        expr = expr.trim();
//
//        if ("loop.index0".equals(expr)) {
//            return getCurrentLoopIndex();
//        }
//        if ("loop.index".equals(expr)) {
//            Integer i = getCurrentLoopIndex();
//            return i == null ? null : i + 1;
//        }
//
//        String[] parts = expr.split("\\.");
//        Object current = resolveSimpleName(parts[0].trim());
//        for (int i = 1; i < parts.length && current != null; i++) {
//            if (current instanceof Map) {
//                current = ((Map<?, ?>) current).get(parts[i].trim());
//            } else {
//                return null;
//            }
//        }
//        return current;
//    }
//
//    private Object resolveSimpleName(String name) {
//        for (Map<String, Object> scope : variableScopes) {
//            if (scope.containsKey(name)) {
//                return scope.get(name);
//            }
//        }
//        return globalVariables.get(name);
//    }
//
//    /**
//     * Pushes a for-loop scope.
//     *
//     * @param varName    loop variable name (e.g. {@code p})
//     * @param value      current item
//     * @param loopIndex  zero-based index
//     */
//    public void pushScope(String varName, Object value, int loopIndex) {
//        Map<String, Object> scope = new LinkedHashMap<>();
//        if (varName != null) {
//            scope.put(varName, value);
//        }
//        variableScopes.push(scope);
//        loopIndices.push(loopIndex);
//    }
//
//    /** Pops the innermost for-loop scope. */
//    public void popScope() {
//        if (!variableScopes.isEmpty()) {
//            variableScopes.pop();
//        }
//        if (!loopIndices.isEmpty()) {
//            loopIndices.pop();
//        }
//    }
//
//    /** @return current {@code loop.index0}, or {@code null} if not in a loop */
//    public Integer getCurrentLoopIndex() {
//        return loopIndices.isEmpty() ? null : loopIndices.peek();
//    }
//
//    // =====================================================================
//    // Helpers
//    // =====================================================================
//
//    /** @return true if {@link #resolveVariable(String)} would not return null */
//    public boolean hasVariable(String name) {
//        return resolveVariable(name) != null;
//    }
//
//    /** @return true if a route URL is registered */
//    public boolean hasRoute(String url) {
//        return routes.containsKey(url);
//    }
//
//    /** @return true if template name is known (file list or AST map) */
//    public boolean hasTemplate(String name) {
//        return templateNames.contains(name) || templateASTs.containsKey(name);
//    }
//
//    /** @return true if at least one product exists */
//    public boolean hasProducts() {
//        return !products.isEmpty();
//    }
//
//    /** Sets the template currently being rendered (Person 3). */
//    public void setCurrentTemplate(String name) {
//        currentTemplate = name;
//    }
//
//    /** @return template name currently being rendered, or null */
//    public String getCurrentTemplate() {
//        return currentTemplate;
//    }
//
//    /** Marks a template as successfully generated (Person 4). */
//    public void markGenerated(String template) {
//        if (template != null) {
//            generatedTemplates.add(template);
//        }
//    }
//
//    /** @return unmodifiable set of generated template names */
//    public Set<String> getGeneratedTemplates() {
//        return Collections.unmodifiableSet(generatedTemplates);
//    }
//
//    /**
//     * Looks up a product by its {@code name} field.
//     *
//     * @param name product name
//     * @return defensive copy of the product map, or null
//     */
//    public Map<String, Object> getProductByName(String name) {
//        if (name == null) {
//            return null;
//        }
//        for (Map<String, Object> p : products) {
//            if (name.equals(String.valueOf(p.get("name")))) {
//                return new LinkedHashMap<>(p);
//            }
//        }
//        return null;
//    }
//
//    /** @return function name for the given route URL, or null */
//    public String getRouteFunction(String url) {
//        return routes.get(url);
//    }
//
//    // =====================================================================
//    // Products
//    // =====================================================================
//
//    /**
//     * Adds a product. Skips duplicates that share the same name+price pair.
//     *
//     * @param product product fields (name, price, image, details, ...)
//     */
//    public void addProduct(Map<String, Object> product) {
//        ensureMutable();
//        if (product == null) {
//            return;
//        }
//        String key = String.valueOf(product.get("name")) + "|"
//                + String.valueOf(product.get("price"));
//        for (Map<String, Object> existing : products) {
//            String ek = String.valueOf(existing.get("name")) + "|"
//                    + String.valueOf(existing.get("price"));
//            if (key.equals(ek)) {
//                return;
//            }
//        }
//        products.add(new LinkedHashMap<>(product));
//    }
//
//    /**
//     * @return unmodifiable deep-ish copy of products
//     *         (each map is copied; nested lists/maps are shallow)
//     */
//    public List<Map<String, Object>> getProducts() {
//        List<Map<String, Object>> copy = new ArrayList<>();
//        for (Map<String, Object> p : products) {
//            copy.add(new LinkedHashMap<>(p));
//        }
//        return Collections.unmodifiableList(copy);
//    }
//
//    // =====================================================================
//    // Template AST
//    // =====================================================================
//
//    /**
//     * Registers a parsed template AST.
//     *
//     * @param name template file name (e.g. {@code index.html})
//     * @param ast  root {@link PageNode}
//     */
//    public void addTemplateAST(String name, PageNode ast) {
//        ensureMutable();
//        if (name != null && ast != null) {
//            templateASTs.put(name, ast);
//            if (!pages.contains(ast)) {
//                pages.add(ast);
//            }
//        }
//    }
//
//    /**
//     * Returns the AST for a template name.
//     *
//     * @param name template file name
//     * @return PageNode or null
//     */
//    public PageNode getTemplate(String name) {
//        return templateASTs.get(name);
//    }
//
//    /** @return unmodifiable map of all template ASTs */
//    public Map<String, PageNode> getTemplateASTs() {
//        return Collections.unmodifiableMap(templateASTs);
//    }
//
//    /** Clears ASTs and the pages list (used at the start of parsing). */
//    public void clearTemplateASTs() {
//        ensureMutable();
//        templateASTs.clear();
//        pages.clear();
//    }
//
//    // =====================================================================
//    // Routes
//    // =====================================================================
//
//    /**
//     * Registers a route mapping.
//     *
//     * @param url      path such as {@code /} or {@code /details/<int:i>}
//     * @param function Python view function name
//     */
//    public void addRoute(String url, String function) {
//        ensureMutable();
//        if (url != null && function != null) {
//            routes.put(url.trim(), function.trim());
//        }
//    }
//
//    /** @return unmodifiable map URL → function name */
//    public Map<String, String> getRoutes() {
//        return Collections.unmodifiableMap(routes);
//    }
//
//    // =====================================================================
//    // Globals
//    // =====================================================================
//
//    /**
//     * Adds a global variable visible to Jinja resolution.
//     * Lists and maps are stored as defensive copies.
//     *
//     * @param name  variable name
//     * @param value any value (may be null)
//     */
//    public void addGlobalVariable(String name, Object value) {
//        ensureMutable();
//        if (name == null) {
//            return;
//        }
//        if (value instanceof List) {
//            globalVariables.put(name, new ArrayList<>((List<?>) value));
//        } else if (value instanceof Map) {
//            globalVariables.put(name, new LinkedHashMap<>((Map<?, ?>) value));
//        } else {
//            globalVariables.put(name, value);
//        }
//    }
//
//    /** @return global variable by exact name, or null */
//    public Object getGlobalVariable(String name) {
//        return globalVariables.get(name);
//    }
//
//    /** @return unmodifiable view of globals (collections wrapped as unmodifiable) */
//    public Map<String, Object> getGlobalVariables() {
//        Map<String, Object> copy = new LinkedHashMap<>();
//        for (var e : globalVariables.entrySet()) {
//            Object v = e.getValue();
//            if (v instanceof List) {
//                copy.put(e.getKey(),
//                        Collections.unmodifiableList(new ArrayList<>((List<?>) v)));
//            } else if (v instanceof Map) {
//                copy.put(e.getKey(),
//                        Collections.unmodifiableMap(new LinkedHashMap<>((Map<?, ?>) v)));
//            } else {
//                copy.put(e.getKey(), v);
//            }
//        }
//        return Collections.unmodifiableMap(copy);
//    }
//
//    // =====================================================================
//    // Generated pages (writable after freeze)
//    // =====================================================================
//
//    /**
//     * Stores generated HTML for a page.
//     * Allowed after {@link #freeze()}.
//     *
//     * @param fileName output file name (e.g. {@code index.html})
//     * @param html     full HTML string
//     */
//    public void addGeneratedPage(String fileName, String html) {
//        if (fileName != null && html != null) {
//            generatedPages.put(fileName, html);
//        }
//    }
//
//    /** @return unmodifiable map of generated HTML pages */
//    public Map<String, String> getGeneratedPages() {
//        return Collections.unmodifiableMap(generatedPages);
//    }
//
//    // =====================================================================
//    // Template names / relations
//    // =====================================================================
//
//    /**
//     * Registers a template file name.
//     *
//     * @param name file name including extension
//     */
//    public void addTemplateName(String name) {
//        ensureMutable();
//        if (name != null) {
//            templateNames.add(name);
//        }
//    }
//
//    /** @return ordered copy of known template names */
//    public List<String> getTemplateNames() {
//        return new ArrayList<>(templateNames);
//    }
//
//    /** @return unmodifiable ordered list of parsed PageNodes */
//    public List<PageNode> getPages() {
//        return Collections.unmodifiableList(pages);
//    }
//
//    /**
//     * Records {@code {% extends "parent" %}} relationship.
//     *
//     * @param child  child template name
//     * @param parent parent template name
//     */
//    public void setTemplateParent(String child, String parent) {
//        ensureMutable();
//        if (child != null && parent != null) {
//            templateParents.put(child, parent);
//        }
//    }
//
//    /** @return parent template name, or null */
//    public String getTemplateParent(String child) {
//        return templateParents.get(child);
//    }
//
//    /** @return unmodifiable child → parent map */
//    public Map<String, String> getTemplateParents() {
//        return Collections.unmodifiableMap(templateParents);
//    }
//
//    /**
//     * Records {@code {% include "file" %}` usage.
//     *
//     * @param template template that contains the include
//     * @param include  included file name
//     */
//    public void addTemplateInclude(String template, String include) {
//        ensureMutable();
//        if (template == null || include == null) {
//            return;
//        }
//        templateIncludes.computeIfAbsent(template, k -> new ArrayList<>()).add(include);
//    }
//
//    /** @return unmodifiable list of includes for a template */
//    public List<String> getTemplateIncludes(String template) {
//        List<String> list = templateIncludes.get(template);
//        return list == null ? List.of() : List.copyOf(list);
//    }
//
//    /**
//     * Records a {@code {% block name %}} found in a template.
//     *
//     * @param template  template file name
//     * @param blockName block identifier
//     */
//    public void addTemplateBlock(String template, String blockName) {
//        ensureMutable();
//        if (template == null || blockName == null) {
//            return;
//        }
//        templateBlocks.computeIfAbsent(template, k -> new ArrayList<>()).add(blockName);
//    }
//
//    /** @return unmodifiable list of block names in a template */
//    public List<String> getTemplateBlocks(String template) {
//        List<String> list = templateBlocks.get(template);
//        return list == null ? List.of() : List.copyOf(list);
//    }
//
//    // =====================================================================
//    // Template variables
//    // =====================================================================
//
//    /**
//     * Stores variables passed to a template via {@code render_template(...)}.
//     *
//     * @param template template file name
//     * @param vars     variable names (may be null → empty)
//     */
//    public void addTemplateVariables(String template, List<String> vars) {
//        ensureMutable();
//        templateVariables.put(template,
//                vars != null ? new ArrayList<>(vars) : new ArrayList<>());
//    }
//
//    /** @return unmodifiable deep copy of template → variable names */
//    public Map<String, List<String>> getTemplateVariables() {
//        Map<String, List<String>> copy = new LinkedHashMap<>();
//        for (var e : templateVariables.entrySet()) {
//            copy.put(e.getKey(), List.copyOf(e.getValue()));
//        }
//        return Collections.unmodifiableMap(copy);
//    }
//
//    // =====================================================================
//    // Static / copied files (writable after freeze)
//    // =====================================================================
//
//    /**
//     * Registers a static asset path to be copied by Person 4.
//     *
//     * @param path relative file path
//     */
//    public void addStaticFile(String path) {
//        ensureMutable();
//        if (path != null) {
//            staticFiles.add(path);
//        }
//    }
//
//    /** @return unmodifiable set of static file paths */
//    public Set<String> getStaticFiles() {
//        return Collections.unmodifiableSet(staticFiles);
//    }
//
//    /**
//     * Records a file that was actually copied to output.
//     * Allowed after {@link #freeze()}.
//     *
//     * @param path destination or source path
//     */
//    public void addCopiedFile(String path) {
//        if (path != null) {
//            copiedFiles.add(path);
//        }
//    }
//
//    /** @return ordered copy of copied file paths */
//    public List<String> getCopiedFiles() {
//        return new ArrayList<>(copiedFiles);
//    }
//
//    // =====================================================================
//    // Paths
//    // =====================================================================
//
//    /** @return path of the Flask/Python source file */
//    public String getPythonFilePath() {
//        return pythonFilePath;
//    }
//
//    /**
//     * Sets the Python source path used by ContextBuilder.
//     *
//     * @param path file path
//     */
//    public void setPythonFilePath(String path) {
//        ensureMutable();
//        pythonFilePath = path;
//    }
//
//    /** @return templates directory path */
//    public String getTemplatesDirectory() {
//        return templatesDirectory;
//    }
//
//    /**
//     * Sets the templates directory.
//     *
//     * @param dir directory path
//     */
//    public void setTemplatesDirectory(String dir) {
//        ensureMutable();
//        templatesDirectory = dir;
//    }
//
//    /** @return output directory for generated HTML */
//    public String getOutputDirectory() {
//        return outputDirectory;
//    }
//
//    /**
//     * Sets the output directory (defaults to {@code output} if null).
//     *
//     * @param dir directory path
//     */
//    public void setOutputDirectory(String dir) {
//        ensureMutable();
//        outputDirectory = dir != null ? dir : DEFAULT_OUTPUT_DIRECTORY;
//    }
//
//    // =====================================================================
//    // Status
//    // =====================================================================
//
//    /** @return true if semantic analysis reported zero errors */
//    public boolean isSemanticPassed() {
//        return semanticPassed;
//    }
//
//    /**
//     * Records whether semantic analysis succeeded.
//     * Set by Main before generation starts.
//     *
//     * @param value true if no semantic errors
//     */
//    public void setSemanticPassed(boolean value) {
//        semanticPassed = value;
//    }
//
//    // =====================================================================
//    // Warnings / Logs (writable after freeze)
//    // =====================================================================
//
//    /**
//     * Appends a non-fatal generation warning.
//     * Allowed after {@link #freeze()}.
//     *
//     * @param warning message text
//     */
//    public void addWarning(String warning) {
//        if (warning != null) {
//            warnings.add(warning);
//        }
//    }
//
//    /** @return unmodifiable list of warnings */
//    public List<String> getWarnings() {
//        return Collections.unmodifiableList(warnings);
//    }
//
//    /**
//     * Appends a log line for {@code generation_log.txt}.
//     * Allowed after {@link #freeze()}.
//     *
//     * @param entry log message
//     */
//    public void addLog(String entry) {
//        if (entry != null) {
//            logEntries.add(entry);
//        }
//    }
//
//    /** @return unmodifiable list of log entries */
//    public List<String> getLogEntries() {
//        return Collections.unmodifiableList(logEntries);
//    }
//
//    // =====================================================================
//    // Debug
//    // =====================================================================
//
//    /** Prints a short statistics summary to stdout. */
//    public void printSummary() {
//        System.out.println("\n" + "=".repeat(50));
//        System.out.println(" Context Summary");
//        System.out.println("=".repeat(50));
//        System.out.println(" Products  : " + products.size());
//        System.out.println(" Routes    : " + routes.size());
//        System.out.println(" Templates : " + templateNames.size());
//        System.out.println(" ASTs      : " + templateASTs.size());
//        System.out.println(" Blocks    : "
//                + templateBlocks.values().stream().mapToInt(List::size).sum());
//        System.out.println(" Includes  : "
//                + templateIncludes.values().stream().mapToInt(List::size).sum());
//        System.out.println(" Globals   : " + globalVariables.size());
//        System.out.println(" Static    : " + staticFiles.size());
//        System.out.println(" Warnings  : " + warnings.size());
//        System.out.println(" Logs      : " + logEntries.size());
//        System.out.println(" Frozen    : " + frozen);
//        System.out.println(" Semantic  : " + semanticPassed);
//        System.out.println("=".repeat(50));
//    }
//
//    /** Prints summary plus products, routes, templates, and warnings. */
//    public void printContext() {
//        printSummary();
//        System.out.println("\n Products detail:");
//        for (int i = 0; i < products.size(); i++) {
//            System.out.println("  [" + i + "] " + products.get(i));
//        }
//        System.out.println("\n Routes:");
//        for (var e : routes.entrySet()) {
//            System.out.println("  " + e.getKey() + " → " + e.getValue() + "()");
//        }
//        System.out.println("\n Templates:");
//        for (String n : templateNames) {
//            String parent = templateParents.get(n);
//            System.out.println("  - " + n + (parent != null ? " extends " + parent : ""));
//        }
//        if (!warnings.isEmpty()) {
//            System.out.println("\n Warnings:");
//            for (String w : warnings) {
//                System.out.println("  - " + w);
//            }
//        }
//    }
//}
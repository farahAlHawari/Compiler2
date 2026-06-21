package symbol_table;

import java.util.*;

/**
 * Unified Symbol Table for both Python (Flask) and Template (HTML/CSS/Jinja) parsing.
 *
 * DESIGN DECISIONS:
 *
 * 1. Data Structure: Stack<Scope> where each Scope contains a LinkedHashMap<String, SymbolEntry>.
 *    - Stack: handles nested scopes naturally (push on enter, pop on exit)
 *    - LinkedHashMap within each scope: O(1) lookup, preserves insertion order for printing
 *
 * 2. Why Stack + Map (not just Map):
 *    - A single Map cannot handle scope nesting (variables in different scopes with same name)
 *    - Stack allows us to push/pop scopes and search from innermost to outermost
 *    - Each scope level is independent, preventing cross-scope contamination
 *
 * 3. Why LinkedHashMap (not just HashMap):
 *    - Preserves insertion order so print output matches source code order
 *    - Still O(1) for lookup/insert/delete
 *
 * 4. For CSS where properties can repeat within same rule:
 *    - We store CSS properties in a separate list (cssProperties) that allows duplicates
 *    - The main symbols map still enforces uniqueness for variables/functions
 *    - [تعديل 2] بنستخدم forceInsert بدل insert عشان نسمح بالتكرار
 *
 * 5. Unified: One symbol table stores entries from BOTH Python and Template sources,
 *    with a "source" field to distinguish them.
 */
public class SymbolTable {

    // ==================== Core Data Structures ====================

    private Stack<Scope> scopeStack;                    // Stack of scopes for scope management
    private List<SymbolEntry> allEntries;               // All entries ever added (for printing)
    private List<SymbolEntry> duplicateErrors;          // Entries that caused duplicate errors
    private List<String> errorMessages;
    private List<Scope> completedScopes; // Semantic error messages
    private String currentFilePath = "";
    // For CSS: properties can repeat within different selectors,
    // so we store them in a list instead of a map
    private List<SymbolEntry> cssProperties;            // CSS properties (allows duplicates)
    private List<SymbolEntry> cssSelectors;             // CSS selectors
    private List<SymbolEntry> htmlAttributes;           // HTML attributes (can repeat across elements)

    // ==================== Semantic Error Checking Data ====================

    // For Missing Flask Variable: track render_template() calls from Flask
    private List<FlaskTemplateCall> renderTemplateCalls;

    // For Invalid Func Call + Wrong Args Count: track all function calls
    private List<FunctionCallInfo> functionCallInfos;

    // For Return Type Mismatch: track all return statements
    private List<ReturnInfo> returnInfos;
    private List<JinjaFilterUsage> jinjaFilterUsages;
    private List<DivisionInfo> divisionInfos;
    // Track current scope context for qualified naming
    private String currentSource;                       // "python" or "template"
    private String currentFileName = "";
    public SymbolTable() {
        this.scopeStack = new Stack<>();
        this.allEntries = new ArrayList<>();
        this.duplicateErrors = new ArrayList<>();
        this.errorMessages = new ArrayList<>();
        this.cssProperties = new ArrayList<>();
        this.cssSelectors = new ArrayList<>();
        this.htmlAttributes = new ArrayList<>();
        this.currentSource = "python";
        this.completedScopes = new ArrayList<>();
        this.renderTemplateCalls = new ArrayList<>();
        this.functionCallInfos = new ArrayList<>();
        this.returnInfos = new ArrayList<>();
        this.jinjaFilterUsages = new ArrayList<>();
        this.divisionInfos = new ArrayList<>();
        // Start with a global scope
        enterScope("global",  "global");
    }

    // ==================== Scope Management (Stack Operations) ====================

    /**
     * Enter a new scope (push onto stack).
     * Called when entering: function, class, block, style block, jinja block.
     */
//    public void enterScope(String scopeType, String contextName) {
//        int level = scopeStack.size();  // تلقائي من الـ stack
//        Scope newScope = new Scope(scopeType, level, contextName);
//        scopeStack.push(newScope);
//        completedScopes.add(newScope);
//    }
    public String getCurrentFileName() {
        return currentFileName;
    }

    public void setCurrentFileName(String currentFileName) {
        this.currentFileName = currentFileName;
    }

    public void enterScope(String scopeType, String contextName) {
        int level = scopeStack.size();
        Scope newScope = new Scope(scopeType, level, contextName);
        scopeStack.push(newScope);

        // completedScopes.add(newScope);
    }
    public String getCurrentFilePath() { return currentFilePath; }
    public void setCurrentFilePath(String currentFilePath) { this.currentFilePath = currentFilePath; }

    public List<DivisionInfo> getDivisionInfos() { return divisionInfos; }
    public void addDivisionInfo(DivisionInfo info) { divisionInfos.add(info); }
    /**
     * Exit the current scope (pop from stack).
     * Called when exiting: function, class, block, style block, jinja block.
     */
    // ✅ هذا صح - بعد ما السكوب يتقفل
    public Scope exitScope() {
        if (scopeStack.size() > 1) {
            Scope finished = scopeStack.pop();
            completedScopes.add(finished);  // ← بس هاد يضيف
            return finished;
        }
        return null;
    }

    /**
     * Get the current (innermost) scope.
     */
    public Scope currentScope() {
        return scopeStack.peek();
    }

    /**
     * Get the current scope depth.
     */
    public int currentScopeLevel() {
        return scopeStack.size() - 1;
    }
    // ==================== Symbol Operations ====================

    /**
     * Insert a symbol into the current scope.
     * Checks for duplicates in the CURRENT scope only.
     * Returns true if successful, false if duplicate found.
     */
    public boolean insert(SymbolEntry entry) {
        Scope current = currentScope();

        // Check for duplicate in current scope
        if (current.contains(entry.getName())) {
            SymbolEntry existing = current.lookup(entry.getName());
            String errorMsg = String.format(
                    "Semantic Error [Line %d]: Symbol '%s' is already declared in scope '%s' (first declared at line %d)",
                    entry.getLine(), entry.getName(), current.getContextName(),
                    existing != null ? existing.getLine() : -1
            );
            errorMessages.add(errorMsg);
            duplicateErrors.add(entry);
            return false;
        }

        current.insert(entry);
        allEntries.add(entry);
        return true;
    }

    // ==================== [تعديل 2] إصلاح insertCssProperty ====================
    /**
     * Insert a CSS property (allows duplicates since CSS properties can repeat
     * across different selectors). We still check for duplicates within the
     * SAME selector/rule and report a warning.
     *
     * التعديل: بنستخدم forceInsert بدل insert عشان Scope.insert()
     * بيرفض التكرار، لكن CSS properties بتتكرر (مثل color بأكثر من selector)
     * فلازم نسمح بالإدخال الإجباري.
     */
    public boolean insertCssProperty(SymbolEntry entry) {
        Scope current = currentScope();

        // تحذير لو الخاصية مكررة بنفس القاعدة (نفس السكوب)
        if (current.contains(entry.getName())) {
            String errorMsg = String.format(
                    "Warning [Line %d]: CSS property '%s' is duplicated within the same rule in scope '%s'",
                    entry.getLine(), entry.getName(), current.getContextName()
            );
            errorMessages.add(errorMsg);
        }

        // ✅ إدخال إجباري - هاد التعديل الأساسي
        // بدل current.insert(entry) يلي بيرجع false للتكرار
        current.forceInsert(entry);

        cssProperties.add(entry);
        allEntries.add(entry);
        return true;
    }
    // ==================== نهاية التعديل 2 ====================

    /**
     * Insert a CSS selector entry.
     */
    public void insertCssSelector(SymbolEntry entry) {
        cssSelectors.add(entry);
        allEntries.add(entry);
    }

    /**
     * Insert an HTML attribute entry (can repeat across different elements).
     */
    public void insertHtmlAttribute(SymbolEntry entry) {
        htmlAttributes.add(entry);
        allEntries.add(entry);
    }

    /**
     * Look up a symbol by name, searching from innermost scope to outermost.
     * This implements the scope chain: if not found in current scope, check parent.
     */
    public SymbolEntry lookup(String name) {
        // Search from innermost scope outward
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Scope scope = scopeStack.get(i);
            SymbolEntry entry = scope.lookup(name);
            if (entry != null) {
                return entry;
            }
        }
        return null; // Not found in any scope
    }

    /**
     * Look up a symbol in the current scope only (no parent traversal).
     * Used for duplicate detection: we only flag duplicates in the SAME scope.
     */
    public SymbolEntry lookupCurrentScope(String name) {
        return currentScope().lookup(name);
    }

    /**
     * Update a symbol's value/type. Searches from innermost scope outward.
     */
    public boolean update(String name, String newValue, String newType) {
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Scope scope = scopeStack.get(i);
            if (scope.update(name, newValue, newType)) {
                return true;
            }
        }
        return false; // Symbol not found
    }

    /**
     * Delete a symbol from the current scope.
     */
    public boolean delete(String name) {
        return currentScope().delete(name) != null;
    }

    /**
     * Check if a symbol exists in the current scope only.
     */
    public boolean checkCurrentScope(String name) {
        return currentScope().contains(name);
    }

    /**
     * Check if a symbol exists in any scope (from innermost to outermost).
     */
    public boolean checkAnyScope(String name) {
        return lookup(name) != null;
    }

    /**
     * Check if a symbol is global (exists in the global scope).
     */
    public boolean isGlobal(String name) {
        if (scopeStack.isEmpty()) return false;
        Scope globalScope = scopeStack.get(0);
        return globalScope.contains(name);
    }


    /**
     * Get the scope type where a symbol is declared.
     */
    public String getSymbolScopeType(String name) {
        SymbolEntry entry = lookup(name);
        return entry != null ? entry.getScopeType() : null;
    }

    // ==================== Source Tracking ====================

    public void setSource(String source) {
        this.currentSource = source;
    }

    public String getSource() {
        return currentSource;
    }

    // ==================== Error Management ====================

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public List<SymbolEntry> getDuplicateErrors() {
        return duplicateErrors;
    }

    public boolean hasErrors() {
        return !errorMessages.isEmpty();
    }

    // ==================== Semantic Error Checking Getters ====================

    public List<FlaskTemplateCall> getRenderTemplateCalls() {
        return renderTemplateCalls;
    }

    public void addRenderTemplateCall(FlaskTemplateCall call) {
        renderTemplateCalls.add(call);
    }

    public List<FunctionCallInfo> getFunctionCallInfos() {
        return functionCallInfos;
    }

    public void addFunctionCallInfo(FunctionCallInfo info) {
        functionCallInfos.add(info);
    }

    public List<ReturnInfo> getReturnInfos() {
        return returnInfos;
    }

    public void addReturnInfo(ReturnInfo info) {
        returnInfos.add(info);
    }
    public List<JinjaFilterUsage> getJinjaFilterUsages() {
        return jinjaFilterUsages;
    }

    public void addJinjaFilterUsage(JinjaFilterUsage usage) {
        jinjaFilterUsages.add(usage);
    }
    // ==================== Allocation / Free (as per lecture) ====================

    /**
     * Allocate: create a new empty symbol table.
     */
    public void allocate() {
        scopeStack.clear();
        allEntries.clear();
        duplicateErrors.clear();
        errorMessages.clear();
        cssProperties.clear();
        cssSelectors.clear();
        htmlAttributes.clear();
        completedScopes.clear();  // أضيفي هاد السطر
        renderTemplateCalls.clear();
        functionCallInfos.clear();
        returnInfos.clear();
        jinjaFilterUsages.clear();
        divisionInfos.clear();
        enterScope("global",  "global");
    }

    /**
     * Free: remove all entries from the symbol table.
     */
    public void free() {
        scopeStack.clear();
        allEntries.clear();
        duplicateErrors.clear();
        errorMessages.clear();
        cssProperties.clear();
        cssSelectors.clear();
        htmlAttributes.clear();
        renderTemplateCalls.clear();
        functionCallInfos.clear();
        returnInfos.clear();
        jinjaFilterUsages.clear();
        divisionInfos.clear();
    }

    // ==================== Printing ====================

    /**
     * Print the symbol table as a clean table + errors only.
     */
    public void printSymbolTable() {
        System.out.println("\n" + "=".repeat(115));
        System.out.println("                         UNIFIED SYMBOL TABLE (Python + Template)");
        System.out.println("=".repeat(115));

        // Print header
        System.out.println(String.format("| %-18s | %-12s | %-10s | %-12s | %-5s | %-5s | %-10s | %-15s |",
                "Name", "Kind", "Type", "Scope", "Level", "Line", "Source", "Value"));
        System.out.println("-".repeat(115));

        // Print all entries
        for (SymbolEntry entry : allEntries) {
            System.out.println(entry.toString());
        }

        System.out.println("=".repeat(115));
        System.out.println("Total symbols: " + allEntries.size());

        // Print errors only (if any)
        if (!errorMessages.isEmpty()) {
            System.out.println("\nSemantic Errors:");
            for (String err : errorMessages) {
                System.out.println("  " + err);
            }
        } else {
            System.out.println("\nNo Semantic Errors found.");
        }
    }

    // ==================== [تعديل 3] طباعة السكوبات بشكل منظم ====================
    /**
     * طباعة هيكل السكوبات كاملة - مفيد للعرض والتوضيح.
     * بيظهر كل سكوب ومحتوياته بشكل شجري.
     */
//    public void printScopeStructure() {
//        System.out.println("\n" + "=".repeat(80));
//        System.out.println("                    SCOPE STRUCTURE");
//        System.out.println("=".repeat(80));
//
//        List<Scope> allScopes = new ArrayList<>();
//        allScopes.addAll(completedScopes);
//        for (Scope s : scopeStack) {
//            allScopes.add(s);
//        }
//
//        allScopes.sort((a, b) -> Integer.compare(a.getScopeLevel(), b.getScopeLevel()));
//
//        for (Scope scope : allScopes) {
//            // ✅ التعديل هنا: تخطى السكوبات الفارغة عند الطباعة
////            if (scope.size() == 0 && !scope.getScopeType().equals("global")) continue;
//
//            String indent = "  ".repeat(scope.getScopeLevel());
//            System.out.println(indent + "┌── Scope: "
//                    + scope.getScopeType() + ":"
//                    + scope.getContextName()
//                    + " (level " + scope.getScopeLevel() + ")");
//
//            for (SymbolEntry entry : scope.getAllSymbols()) {
//                System.out.println(indent + "│   "
//                        + entry.getName()
//                        + " [" + entry.getKind() + "]"
//                        + " type=" + entry.getType()
//                        + " line=" + entry.getLine());
//            }
//            System.out.println(indent + "└── (" + scope.size() + " symbols)");
//        }
//        System.out.println("=".repeat(80));
//    }

public void printScopeStructure() {
    System.out.println("\n" + "=".repeat(80));
    System.out.println("                    SCOPE STRUCTURE");
    System.out.println("=".repeat(80));

    List<Scope> allScopes = new ArrayList<>();

    // 1) أضيفي السكوبات المكتملة (اللي اتقفلت)
    allScopes.addAll(completedScopes);

    // 2) أضيفي السكوبات اللي لسه مفتوحة على الستاك
    //    (بشكل رئيسي global scope اللي ما بيتقفل أبداً)
    Set<Scope> alreadyAdded = new HashSet<>(completedScopes);
    for (Scope s : scopeStack) {
        if (!alreadyAdded.contains(s)) {  // ← منع التكرار
            allScopes.add(s);
        }
    }

    allScopes.sort((a, b) -> Integer.compare(a.getScopeLevel(), b.getScopeLevel()));

    for (Scope scope : allScopes) {
        String indent = "  ".repeat(scope.getScopeLevel());
        System.out.println(indent + "┌── Scope: "
                + scope.getScopeType() + ":"
                + scope.getContextName()
                + " (level " + scope.getScopeLevel() + ")");

        for (SymbolEntry entry : scope.getAllSymbols()) {
            System.out.println(indent + "│   "
                    + entry.getName()
                    + " [" + entry.getKind() + "]"
                    + " type=" + entry.getType()
                    + " line=" + entry.getLine());
        }
        System.out.println(indent + "└── (" + scope.size() + " symbols)");
    }
    System.out.println("=".repeat(80));
}
    // ==================== نهاية التعديل 3 ====================

    // ==================== Getters for all data ====================

    public List<SymbolEntry> getAllEntries() {
        return allEntries;
    }

    public List<SymbolEntry> getCssProperties() {
        return cssProperties;
    }

    public List<SymbolEntry> getCssSelectors() {
        return cssSelectors;
    }

    public List<SymbolEntry> getHtmlAttributes() {
        return htmlAttributes;
    }

    public Stack<Scope> getScopeStack() {
        return scopeStack;
    }
}

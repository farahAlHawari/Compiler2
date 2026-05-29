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

    // For CSS: properties can repeat within different selectors,
    // so we store them in a list instead of a map
    private List<SymbolEntry> cssProperties;            // CSS properties (allows duplicates)
    private List<SymbolEntry> cssSelectors;             // CSS selectors
    private List<SymbolEntry> htmlAttributes;           // HTML attributes (can repeat across elements)

    // Track current scope context for qualified naming
    private String currentSource;                       // "python" or "template"

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
        // Start with a global scope
        enterScope("global", 0, "global");
    }

    // ==================== Scope Management (Stack Operations) ====================

    /**
     * Enter a new scope (push onto stack).
     * Called when entering: function, class, block, style block, jinja block.
     */
    public void enterScope(String scopeType, int level, String contextName) {
        Scope newScope = new Scope(scopeType, level, contextName);
        if (!scopeStack.isEmpty()) {
            newScope.setParent(scopeStack.peek());
        }
        scopeStack.push(newScope);
    }

    /**
     * Exit the current scope (pop from stack).
     * Called when exiting: function, class, block, style block, jinja block.
     */
    public Scope exitScope() {
        if (scopeStack.size() > 1) {
            Scope finished = scopeStack.pop();
            completedScopes.add(finished);  // احفظيها قبل الحذف
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
        enterScope("global", 0, "global");
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
    public void printScopeStructure() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("                    SCOPE STRUCTURE");
        System.out.println("=".repeat(80));

        List<Scope> allScopes = new ArrayList<>();
        allScopes.addAll(completedScopes);
        for (Scope s : scopeStack) {
            allScopes.add(s);
        }

        allScopes.sort((a, b) -> Integer.compare(a.getScopeLevel(), b.getScopeLevel()));

        for (Scope scope : allScopes) {
            // ✅ التعديل هنا: تخطى السكوبات الفارغة عند الطباعة
            if (scope.size() == 0 && !scope.getScopeType().equals("global")) continue;

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
//    public void printScopeStructure() {
//        System.out.println("\n" + "=".repeat(80));
//        System.out.println("                    SCOPE STRUCTURE");
//        System.out.println("=".repeat(80));
//
//        // اجمعي كل السكوبات: المنتهية + الحالية بالـ stack
//        List<Scope> allScopes = new ArrayList<>();
//        allScopes.addAll(completedScopes);
//        for (Scope s : scopeStack) {
//            allScopes.add(s);
//        }
//
//        // رتبيهم حسب المستوى عشان الطباعة تكون منظمة
//        allScopes.sort((a, b) -> a.getScopeLevel() - b.getScopeLevel());
//
//        for (Scope scope : allScopes) {
//            String indent = "  ".repeat(scope.getScopeLevel());
//            System.out.println(indent + "┌── Scope: "
//                    + scope.getScopeType() + ":"
//                    + scope.getContextName()
//                    + " (level " + scope.getScopeLevel() + ")");
//
//            List<SymbolEntry> symbols = scope.getAllSymbols();
//            for (SymbolEntry entry : symbols) {
//                System.out.println(indent + "│   "
//                        + entry.getName()
//                        + " [" + entry.getKind() + "]"
//                        + " type=" + entry.getType()
//                        + " line=" + entry.getLine());
//            }
//            System.out.println(indent + "└── ("
//                    + symbols.size() + " symbols)");
//        }
//
//        System.out.println("=".repeat(80));
//    }
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

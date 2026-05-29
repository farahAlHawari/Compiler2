package symbol_table;

/**
 * Represents a single scope level in the Symbol Table.
 * Each Scope holds a HashMap of SymbolEntry objects keyed by symbol name,
 * along with metadata about the scope itself (type, level, parent context).
 *
 * Scopes are organized as a stack: when we enter a new scope (function, class,
 * block, style block, jinja block), we push a new Scope onto the stack.
 * When we exit, we pop it off. This naturally handles nested scopes.
 *
 * Within each scope, we use a HashMap for O(1) lookup of symbols.
 * For cases where duplicate names are allowed within the same scope
 * (e.g., CSS properties that can repeat), we store them as a list
 * within the SymbolTable rather than overwriting.
 */
import java.util.*;

public class Scope {

    private String scopeType;       // "global", "class", "function", "block", "style", "jinja_block"
    private int scopeLevel;         // Nesting depth
    private String contextName;     // Name of the context (function name, class name, etc.)
    private Map<String, SymbolEntry> symbols;  // Symbols in this scope
    private Scope parent;           // Reference to parent scope (for lookup chain)

    public Scope(String scopeType, int scopeLevel, String contextName) {
        this.scopeType = scopeType;
        this.scopeLevel = scopeLevel;
        this.contextName = contextName;
        this.symbols = new LinkedHashMap<>(); // LinkedHashMap preserves insertion order
        this.parent = null;
    }

    // ==================== Core Operations ====================

    /**
     * Insert a symbol into this scope.
     * @return true if inserted successfully, false if symbol already exists in this scope
     */
    public boolean insert(SymbolEntry entry) {
        if (symbols.containsKey(entry.getName())) {
            return false; // Duplicate in same scope
        }
        symbols.put(entry.getName(), entry);
        return true;
    }
    public void forceInsert(SymbolEntry entry) {
        String key = entry.getName();
        if (symbols.containsKey(key)) {
            // الخاصية موجودة - بنضيف رقم للـ key
            int counter = 1;
            while (symbols.containsKey(key + "#" + counter)) {
                counter++;
            }
            key = key + "#" + counter;
        }
        symbols.put(key, entry);
    }
    /**
     * Look up a symbol by name in this scope only (no parent traversal).
     */
    public SymbolEntry lookup(String name) {
        return symbols.get(name);
    }

    /**
     * Check if a symbol exists in this scope.
     */
    public boolean contains(String name) {
        return symbols.containsKey(name);
    }

    /**
     * Update an existing symbol's value/type in this scope.
     */
    public boolean update(String name, String newValue, String newType) {
        SymbolEntry entry = symbols.get(name);
        if (entry != null) {
            entry.setValue(newValue);
            if (newType != null) {
                entry.setType(newType);
            }
            return true;
        }
        return false;
    }

    /**
     * Remove a symbol from this scope.
     */
    public SymbolEntry delete(String name) {
        return symbols.remove(name);
    }

    // ==================== Getters and Setters ====================

    public String getScopeType() {
        return scopeType;
    }

    public int getScopeLevel() {
        return scopeLevel;
    }

    public String getContextName() {
        return contextName;
    }

    public Map<String, SymbolEntry> getSymbols() {
        return symbols;
    }

    public Scope getParent() {
        return parent;
    }

    public void setParent(Scope parent) {
        this.parent = parent;
    }

    /**
     * Get all symbols in this scope as a list (preserving insertion order).
     */
    public List<SymbolEntry> getAllSymbols() {
        return new ArrayList<>(symbols.values());
    }

    /**
     * Get the number of symbols in this scope.
     */
    public int size() {
        return symbols.size();
    }

    @Override
    public String toString() {
        return scopeType + ":" + contextName + " (level " + scopeLevel + ", " + symbols.size() + " symbols)";
    }
}
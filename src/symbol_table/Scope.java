package symbol_table;


import java.util.*;

public class Scope {

    private String scopeType;
    private int scopeLevel;
    private String contextName;
    private Map<String, SymbolEntry> symbols;
    private Scope parent;

    public Scope(String scopeType, int scopeLevel, String contextName) {
        this.scopeType = scopeType;
        this.scopeLevel = scopeLevel;
        this.contextName = contextName;
        this.symbols = new LinkedHashMap<>();
        this.parent = null;
    }


    public boolean insert(SymbolEntry entry) {
        if (symbols.containsKey(entry.getName())) {
            return false;
        }
        symbols.put(entry.getName(), entry);
        return true;
    }
    public void forceInsert(SymbolEntry entry) {
        String key = entry.getName();
        if (symbols.containsKey(key)) {
            int counter = 1;
            while (symbols.containsKey(key + "#" + counter)) {
                counter++;
            }
            key = key + "#" + counter;
        }
        symbols.put(key, entry);
    }

    public SymbolEntry lookup(String name) {
        return symbols.get(name);
    }


    public boolean contains(String name) {
        return symbols.containsKey(name);
    }


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


    public SymbolEntry delete(String name) {
        return symbols.remove(name);
    }


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


    public List<SymbolEntry> getAllSymbols() {
        return new ArrayList<>(symbols.values());
    }

    public int size() {
        return symbols.size();
    }

    @Override
    public String toString() {
        return scopeType + ":" + contextName + " (level " + scopeLevel + ", " + symbols.size() + " symbols)";
    }
}
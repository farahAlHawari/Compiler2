package symbol_table;

import java.util.*;
import java.util.List;


import java.util.ArrayList;


public class SymbolTable {


    private Stack<Scope> scopeStack;
    private List<SymbolEntry> allEntries;
    private List<SymbolEntry> duplicateErrors;
    private List<String> errorMessages;
    private List<Scope> completedScopes;
    private String currentFilePath = "";

    private List<SymbolEntry> cssProperties;
    private List<SymbolEntry> cssSelectors;
    private List<SymbolEntry> htmlAttributes;




    private List<FlaskTemplateCall> renderTemplateCalls;


    private List<FunctionCallInfo> functionCallInfos;


    private List<ReturnInfo> returnInfos;
    private List<JinjaFilterUsage> jinjaFilterUsages;
    private List<DivisionInfo> divisionInfos;

    private List<OperationTypeInfo> operationTypeInfos;
    private List<IndexTypeInfo> indexTypeInfos;
    private List<FunctionArgTypeInfo> functionArgTypeInfos;

    private List<UnaryOpTypeInfo> unaryOpTypeInfos;

    private List<UnboundLocalInfo> unboundLocalInfos;
    private List<UseBeforeInitInfo> useBeforeInitInfos;


    private List<AttributeAccessInfo> attributeAccessInfos;

    private List<OperationOnNoneInfo> operationOnNoneInfos;


    private String currentSource;
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
        this.operationTypeInfos = new ArrayList<>();
        this.indexTypeInfos = new ArrayList<>();
        this.functionArgTypeInfos = new ArrayList<>();
        this.unaryOpTypeInfos = new ArrayList<>();
        this.unboundLocalInfos = new ArrayList<>();
        this.useBeforeInitInfos = new ArrayList<>();
        this.attributeAccessInfos = new ArrayList<>();
        this.operationOnNoneInfos = new ArrayList<>();
        // Start with a global scope
        enterScope("global",  "global");
    }


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


    }
    public String getCurrentFilePath() { return currentFilePath; }
    public void setCurrentFilePath(String currentFilePath) { this.currentFilePath = currentFilePath; }

    public List<DivisionInfo> getDivisionInfos() { return divisionInfos; }
    public void addDivisionInfo(DivisionInfo info) { divisionInfos.add(info); }

    public List<OperationTypeInfo> getOperationTypeInfos() { return operationTypeInfos; }
    public void addOperationTypeInfo(OperationTypeInfo info) { operationTypeInfos.add(info); }

    public List<IndexTypeInfo> getIndexTypeInfos() { return indexTypeInfos; }
    public void addIndexTypeInfo(IndexTypeInfo info) { indexTypeInfos.add(info); }

    public List<FunctionArgTypeInfo> getFunctionArgTypeInfos() { return functionArgTypeInfos; }
    public void addFunctionArgTypeInfo(FunctionArgTypeInfo info) { functionArgTypeInfos.add(info); }

    public List<UnaryOpTypeInfo> getUnaryOpTypeInfos() { return unaryOpTypeInfos; }
    public void addUnaryOpTypeInfo(UnaryOpTypeInfo info) { unaryOpTypeInfos.add(info); }

    public List<UnboundLocalInfo> getUnboundLocalInfos() { return unboundLocalInfos; }
    public void addUnboundLocalInfo(UnboundLocalInfo info) { unboundLocalInfos.add(info); }

    public List<UseBeforeInitInfo> getUseBeforeInitInfos() { return useBeforeInitInfos; }
    public void addUseBeforeInitInfo(UseBeforeInitInfo info) { useBeforeInitInfos.add(info); }

    public List<AttributeAccessInfo> getAttributeAccessInfos() { return attributeAccessInfos; }
    public void addAttributeAccessInfo(AttributeAccessInfo info) { attributeAccessInfos.add(info); }

    public List<OperationOnNoneInfo> getOperationOnNoneInfos() { return operationOnNoneInfos; }
    public void addOperationOnNoneInfo(OperationOnNoneInfo info) { operationOnNoneInfos.add(info); }

    public Scope exitScope() {
        if (scopeStack.size() > 1) {
            Scope finished = scopeStack.pop();
            completedScopes.add(finished);
            return finished;
        }
        return null;
    }


    public Scope currentScope() {
        return scopeStack.peek();
    }


    public int currentScopeLevel() {
        return scopeStack.size() - 1;
    }

    public boolean insert(SymbolEntry entry) {
        Scope current = currentScope();


        if (current.contains(entry.getName())) {
            SymbolEntry existing = current.lookup(entry.getName());


            String entryFile = (entry.getFileName() != null) ? entry.getFileName() : "";
            String existFile = (existing != null && existing.getFileName() != null)
                    ? existing.getFileName() : "";
            if (!entryFile.isEmpty() && !existFile.isEmpty()
                    && !entryFile.equals(existFile)) {
                return true;
            }

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

    public boolean insertCssProperty(SymbolEntry entry) {
        Scope current = currentScope();


        if (current.contains(entry.getName())) {
            String errorMsg = String.format(
                    "Warning [Line %d]: CSS property '%s' is duplicated within the same rule in scope '%s'",
                    entry.getLine(), entry.getName(), current.getContextName()
            );
            errorMessages.add(errorMsg);
        }


        current.forceInsert(entry);

        cssProperties.add(entry);
        allEntries.add(entry);
        return true;
    }

    public void insertCssSelector(SymbolEntry entry) {
        cssSelectors.add(entry);
        allEntries.add(entry);
    }


    public void insertHtmlAttribute(SymbolEntry entry) {
        htmlAttributes.add(entry);
        allEntries.add(entry);
    }


    public SymbolEntry lookup(String name) {

        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Scope scope = scopeStack.get(i);
            SymbolEntry entry = scope.lookup(name);
            if (entry != null) {
                return entry;
            }
        }
        return null;
    }


    public SymbolEntry lookupCurrentScope(String name) {
        return currentScope().lookup(name);
    }


    public boolean update(String name, String newValue, String newType) {
        for (int i = scopeStack.size() - 1; i >= 0; i--) {
            Scope scope = scopeStack.get(i);
            if (scope.update(name, newValue, newType)) {
                return true;
            }
        }
        return false; // Symbol not found
    }


    public boolean delete(String name) {
        return currentScope().delete(name) != null;
    }


    public boolean checkCurrentScope(String name) {
        return currentScope().contains(name);
    }


    public boolean checkAnyScope(String name) {
        return lookup(name) != null;
    }


    public boolean isGlobal(String name) {
        if (scopeStack.isEmpty()) return false;
        Scope globalScope = scopeStack.get(0);
        return globalScope.contains(name);
    }



    public String getSymbolScopeType(String name) {
        SymbolEntry entry = lookup(name);
        return entry != null ? entry.getScopeType() : null;
    }



    public void setSource(String source) {
        this.currentSource = source;
    }

    public String getSource() {
        return currentSource;
    }


    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public List<SymbolEntry> getDuplicateErrors() {
        return duplicateErrors;
    }

    public boolean hasErrors() {
        return !errorMessages.isEmpty();
    }


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


    public void allocate() {
        scopeStack.clear();
        allEntries.clear();
        duplicateErrors.clear();
        errorMessages.clear();
        cssProperties.clear();
        cssSelectors.clear();
        htmlAttributes.clear();
        completedScopes.clear();
        renderTemplateCalls.clear();
        functionCallInfos.clear();
        returnInfos.clear();
        jinjaFilterUsages.clear();
        divisionInfos.clear();
        unboundLocalInfos.clear();
        useBeforeInitInfos.clear();
        attributeAccessInfos.clear();
        operationOnNoneInfos.clear();
        unaryOpTypeInfos.clear();
        enterScope("global",  "global");
    }


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
        unboundLocalInfos.clear();
        useBeforeInitInfos.clear();
        attributeAccessInfos.clear();
        operationOnNoneInfos.clear();
        unaryOpTypeInfos.clear();
    }


    public void printSymbolTable() {
        System.out.println("\n" + "=".repeat(115));
        System.out.println("                         UNIFIED SYMBOL TABLE (Python + Template)");
        System.out.println("=".repeat(115));

        System.out.println(String.format("| %-18s | %-12s | %-10s | %-12s | %-5s | %-5s | %-10s | %-15s |",
                "Name", "Kind", "Type", "Scope", "Level", "Line", "Source", "Value"));
        System.out.println("-".repeat(115));

        for (SymbolEntry entry : allEntries) {
            System.out.println(entry.toString());
        }

        System.out.println("=".repeat(115));
        System.out.println("Total symbols: " + allEntries.size());

        if (!errorMessages.isEmpty()) {
            System.out.println("\nSemantic Errors:");
            for (String err : errorMessages) {
                System.out.println("  " + err);
            }
        } else {
            System.out.println("\nNo Semantic Errors found.");
        }
    }


public void printScopeStructure() {
    System.out.println("\n" + "=".repeat(80));
    System.out.println("                    SCOPE STRUCTURE");
    System.out.println("=".repeat(80));

    List<Scope> allScopes = new ArrayList<>();

    allScopes.addAll(completedScopes);


    Set<Scope> alreadyAdded = new HashSet<>(completedScopes);
    for (Scope s : scopeStack) {
        if (!alreadyAdded.contains(s)) {
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

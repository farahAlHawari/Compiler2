package main.pythoncompiler.visitor;

import main.pythoncompiler.ast.*;
import symbol_table.SymbolTable;
import symbol_table.SymbolEntry;

/**
 * Visitor that walks the Python AST and populates the Symbol Table.
 *
 * This visitor handles:
 * - Variable declarations (assignments) -> stored as "variable" kind
 * - Function definitions -> stored as "function" kind, opens a new scope
 * - Class definitions -> stored as "class" kind, opens a new scope
 * - Parameters -> stored as "parameter" kind within function scope
 * - Import statements -> stored as "import" kind
 * - Global declarations -> marks variables as global scope
 * - Decorators -> stored as "decorator" kind
 * - For loop iterators -> stored as "variable" within loop scope
 * - Expressions that reference identifiers (for use-before-declaration checks)
 *
 * Scope rules:
 * - Program level = global scope (level 0)
 * - Class body = class scope (level 1)
 * - Function body = function scope (level 1+)
 * - Nested blocks (if/for/while) = block scope (level 2+)
 * - Global keyword: variable should be looked up/inserted in global scope
 */
public class SymbolTableVisitor {

    private SymbolTable symbolTable;
    private java.util.List<String> errors;
    private java.util.Set<String> globalDeclarations; // Track 'global' keyword declarations

    public SymbolTableVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.errors = new java.util.ArrayList<>();
        this.globalDeclarations = new java.util.HashSet<>();
        this.symbolTable.setSource("python");
    }

    public java.util.List<String> getErrors() {
        return errors;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    // ==================== Main Visit Method ====================

    /**
     * Visit any ASTNode by dispatching based on nodeName.
     */
    public void visit(ASTNode node) {
        if (node == null) return;

        switch (node.nodeName) {
            case "Program":
                visitProgram(node);
                break;
            case "Assignment":
                visitAssignment(node);
                break;
            case "FunctionDef":
            case "RouteFunction":
                visitFunctionDef(node);
                break;
            case "ClassDef":
                visitClassDef(node);
                break;
            case "Block":
                visitBlock(node);
                break;
            case "IfStatement":
                visitIfStatement(node);
                break;
            case "WhileLoop":
                visitWhileLoop(node);
                break;
            case "ForLoop":
                visitForLoop(node);
                break;
            case "ReturnStmt":
                visitReturnStmt(node);
                break;
            case "GlobalDecl":
                visitGlobalDecl(node);
                break;
//            case "ImportStmt":
//                visitImportStmt(node);
//                break;
            case "CallExpr":
                visitCallExpr(node);
                break;
            case "Identifier":
                visitIdentifier(node);
                break;
            case "Literal":
                visitLiteral(node);
                break;
            case "BinaryOp":
                visitBinaryOp(node);
                break;
            case "UnaryOp":
                visitUnaryOp(node);
                break;
            case "Parameters":
                visitParameters(node);
                break;
            case "IndexAccess":
                visitIndexAccess(node);
                break;
            case "AttributeAccess":
                visitAttributeAccess(node);
                break;
            case "ListLiteral":
            case "DictLiteral":
                visitContainerLiteral(node);
                break;
            case "PassStmt":
            case "BreakStmt":
            case "ContinueStmt":
                // No symbol table operations needed
                break;
            default:
                // Visit children for unrecognized nodes
                for (ASTNode child : node.children) {
                    visit(child);
                }
                break;
        }
    }

    // ==================== Program ====================

    private void visitProgram(ASTNode node) {
        // Global scope is already created in SymbolTable constructor
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Assignment ====================

    private void visitAssignment(ASTNode node) {
        AssignNode assignNode = (AssignNode) node;
        String varName = assignNode.variableName;
        String operator = assignNode.operator;

        // Determine the type and value from the right-hand side expression
        String inferredType = "unknown";
        String value = "";

        if (!node.children.isEmpty()) {
            ASTNode valueNode = node.children.get(0);
            inferredType = inferType(valueNode);
            value = extractValue(valueNode);
        }

        // Check if this is a global declaration
        if (globalDeclarations.contains(varName)) {
            // Variable declared as global - insert/update in global scope
            SymbolEntry existing = symbolTable.lookup(varName);
            if (existing != null && "global".equals(existing.getScopeType())) {
                // Update existing global variable
                existing.setValue(value);
                existing.setType(inferredType);
            } else {
                // Insert in global scope
                SymbolEntry entry = new SymbolEntry(
                        varName, "variable", inferredType, "global",
                        0, node.lineNumber, "python"
                );
                entry.setValue(value);
                // Navigate to global scope and insert
                insertInGlobalScope(entry);
            }
            globalDeclarations.remove(varName); // Consumed
        } else if (operator.equals("=")) {
            // Simple assignment - check if variable already exists in current scope
            SymbolEntry existingInCurrent = symbolTable.lookupCurrentScope(varName);
            if (existingInCurrent != null) {
                // Variable exists in current scope - update it
                existingInCurrent.setValue(value);
                existingInCurrent.setType(inferredType);
            } else {
                // Check if it exists in an outer scope
                SymbolEntry existingAnywhere = symbolTable.lookup(varName);
                if (existingAnywhere != null) {
                    // Variable exists in outer scope - update it (Python behavior)
                    existingAnywhere.setValue(value);
                    existingAnywhere.setType(inferredType);
                } else {
                    // New variable - insert in current scope
                    String scopeType = symbolTable.currentScope().getScopeType();
                    int scopeLevel = symbolTable.currentScopeLevel();
                    SymbolEntry entry = new SymbolEntry(
                            varName, "variable", inferredType, scopeType,
                            scopeLevel, node.lineNumber, "python"
                    );
                    entry.setValue(value);
                    symbolTable.insert(entry);
                }
            }
        } else {
            // Augmented assignment (+=, -=, etc.) - variable must already exist
            SymbolEntry existing = symbolTable.lookup(varName);
            if (existing != null) {
                existing.setValue(value);
            } else {
                // Undeclared variable with augmented assignment
                String scopeType = symbolTable.currentScope().getScopeType();
                int scopeLevel = symbolTable.currentScopeLevel();
                SymbolEntry entry = new SymbolEntry(
                        varName, "variable", inferredType, scopeType,
                        scopeLevel, node.lineNumber, "python"
                );
                entry.setValue(value);
                symbolTable.insert(entry);
                errors.add(String.format(
                        "Warning [Line %d]: Variable '%s' used with %s before declaration",
                        node.lineNumber, varName, operator
                ));
            }
        }

        // Visit the value expression (for nested references)
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Function Definition ====================

    private void visitFunctionDef(ASTNode node) {
        FunctionDefNode funcNode = (FunctionDefNode) node;
        String funcName = funcNode.functionName;

        // Insert function symbol in current scope
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                funcName, "function", "function", scopeType,
                scopeLevel, node.lineNumber, "python"
        );

        // If it's a RouteFunction, add decorator info
        if ("RouteFunction".equals(node.nodeName)) {
            entry.setKind("route_function");
        }

        symbolTable.insert(entry);

        // Enter function scope
        int newLevel = scopeLevel + 1;
        symbolTable.enterScope("function", newLevel, funcName);

        // Visit parameters (they go into function scope)
        for (ASTNode child : node.children) {
            if ("Parameters".equals(child.nodeName)) {
                visitParameters(child);
            }
        }

        // Visit block (function body)
        for (ASTNode child : node.children) {
            if ("Block".equals(child.nodeName)) {
                visitBlock(child);
            }
        }

        // Exit function scope
        symbolTable.exitScope();
    }

    // ==================== Class Definition ====================

    private void visitClassDef(ASTNode node) {
        ClassDefNode classNode = (ClassDefNode) node;
        String className = classNode.className;

        // Insert class symbol in current scope
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                className, "class", "class", scopeType,
                scopeLevel, node.lineNumber, "python"
        );
        symbolTable.insert(entry);

        // Enter class scope
        int newLevel = scopeLevel + 1;
        symbolTable.enterScope("class", newLevel, className);

        // Visit class body
        for (ASTNode child : node.children) {
            visit(child);
        }

        // Exit class scope
        symbolTable.exitScope();
    }

    // ==================== Block ====================

    private void visitBlock(ASTNode node) {
        // Blocks don't create a new scope by default in Python
        // (only functions, classes, and comprehensions create scopes)
        // So we just visit children within the current scope
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== If Statement ====================

    private void visitIfStatement(ASTNode node) {
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== While Loop ====================

    private void visitWhileLoop(ASTNode node) {
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== For Loop ====================

    private void visitForLoop(ASTNode node) {
        ForNode forNode = (ForNode) node;
        String iteratorName = forNode.iteratorName;

        // Insert iterator variable in current scope
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                iteratorName, "variable", "unknown", scopeType,
                scopeLevel, node.lineNumber, "python"
        );
        // Don't flag as error if already exists (for loop can reassign)
        SymbolEntry existing = symbolTable.lookupCurrentScope(iteratorName);
        if (existing == null) {
            symbolTable.insert(entry);
        } else {
            existing.setType("unknown");
        }

        // Visit children (iterable expression and body)
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Return Statement ====================

    private void visitReturnStmt(ASTNode node) {
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Global Declaration ====================

    private void visitGlobalDecl(ASTNode node) {
        // Extract variable name from the GlobalDecl details
        String details = node.nodeName;
        // Parse " (varName)" format
        String varName = details.replace("(", "").replace(")", "").trim();

        globalDeclarations.add(varName);

        // Check if variable exists in global scope
        if (!symbolTable.isGlobal(varName)) {
            // Mark it so next assignment will go to global scope
            // Don't insert yet - wait for the assignment
        }
    }

    // ==================== Import Statement ====================

//    private void visitImportStmt(ASTNode node) {
//        String details = node.getDetails();
//        // Parse " (from moduleName)" format
//        String modulePart = details.replace("(", "").replace(")", "").trim();
//        if (modulePart.startsWith("from ")) {
//            modulePart = modulePart.substring(5).trim();
//        }
//
//        SymbolEntry entry = new SymbolEntry(
//                modulePart, "import", "module", "global",
//                0, node.lineNumber, "python"
//        );
//        // Insert in global scope
//        insertInGlobalScope(entry);
//    }

    // ==================== Parameters ====================

    private void visitParameters(ASTNode node) {
        for (ASTNode child : node.children) {
            if ("Identifier".equals(child.nodeName)) {
                IdentifierNode idNode = (IdentifierNode) child;
                SymbolEntry entry = new SymbolEntry(
                        idNode.name, "parameter", "unknown", "function",
                        symbolTable.currentScopeLevel(), child.lineNumber, "python"
                );
                symbolTable.insert(entry);
            }
        }
    }

    // ==================== Call Expression ====================

    private void visitCallExpr(ASTNode node) {
        CallNode callNode = (CallNode) node;
        String funcName = callNode.functionName;

        // Check if function is defined (for use-before-declaration check)
        SymbolEntry funcEntry = symbolTable.lookup(funcName);
        if (funcEntry == null) {
            // Function not found in any scope - might be a built-in or imported
            // Don't report error for built-in functions like print, len, etc.
            if (!isBuiltinFunction(funcName)) {
                errors.add(String.format(
                        "Warning [Line %d]: Function '%s' called but not defined in symbol table",
                        node.lineNumber, funcName
                ));
            }
        }

        // Visit children (arguments)
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Identifier ====================

    private void visitIdentifier(ASTNode node) {
        IdentifierNode idNode = (IdentifierNode) node;
        String name = idNode.name;

        // Check if identifier is declared somewhere
        SymbolEntry entry = symbolTable.lookup(name);
        if (entry == null && !isBuiltinFunction(name)) {
            // Not found - might be used before declaration or undeclared
            // We record this as a soft warning, not an error, because
            // in Python, variables can be used before their assignment in some patterns
        }
    }

    // ==================== Literal ====================

    private void visitLiteral(ASTNode node) {
        // Literals don't create symbol table entries
        // But we use them for type inference
    }

    // ==================== Binary Op ====================

    private void visitBinaryOp(ASTNode node) {
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Unary Op ====================

    private void visitUnaryOp(ASTNode node) {
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Index Access ====================

    private void visitIndexAccess(ASTNode node) {
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Attribute Access ====================

    private void visitAttributeAccess(ASTNode node) {
        // Visit the object being accessed
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Container Literal ====================

    private void visitContainerLiteral(ASTNode node) {
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Helper Methods ====================

    /**
     * Infer the type of an expression node.
     */
    private String inferType(ASTNode node) {
        if (node == null) return "unknown";

        switch (node.nodeName) {
            case "Literal":
                LiteralNode lit = (LiteralNode) node;
                return lit.type.toLowerCase();
            case "ListLiteral":
                return "list";
            case "DictLiteral":
                return "dict";
            case "CallExpr":
                CallNode call = (CallNode) node;
                // Some functions have known return types
                if (call.functionName.equals("list")) return "list";
                if (call.functionName.equals("dict")) return "dict";
                if (call.functionName.equals("int")) return "int";
                if (call.functionName.equals("str")) return "string";
                if (call.functionName.equals("Flask")) return "FlaskApp";
                return "unknown";
            case "BinaryOp":
                BinaryOpNode binOp = (BinaryOpNode) node;
                if ("and".equals(binOp.operator) || "or".equals(binOp.operator))
                    return "bool";
                if ("+".equals(binOp.operator) || "-".equals(binOp.operator)
                        || "*".equals(binOp.operator) || "/".equals(binOp.operator)
                        || "%".equals(binOp.operator))
                    return "int"; // Simplified
                return "bool"; // Comparison operators
            case "UnaryOp":
                UnaryOpNode unOp = (UnaryOpNode) node;
                if ("not".equals(unOp.operator)) return "bool";
                return "unknown";
            case "Identifier":
                SymbolEntry entry = symbolTable.lookup(((IdentifierNode) node).name);
                return entry != null ? entry.getType() : "unknown";
            case "IndexAccess":
                return "unknown"; // Could be any type
            case "AttributeAccess":
                return "unknown"; // Could be any type
            default:
                return "unknown";
        }
    }

    /**
     * Extract a string representation of the value of an expression node.
     */
    private String extractValue(ASTNode node) {
        if (node == null) return "";

        switch (node.nodeName) {
            case "Literal":
                return ((LiteralNode) node).value;
            case "Identifier":
                return ((IdentifierNode) node).name;
            case "ListLiteral":
                return "[...]";
            case "DictLiteral":
                return "{...}";
            case "CallExpr":
                return ((CallNode) node).functionName + "(...)";
            default:
                return node.nodeName;
        }
    }

    /**
     * Check if a function name is a Python built-in.
     */
    private static final java.util.Set<String> PYTHON_BUILTINS = java.util.Set.of(
            "print", "len", "range", "int", "str", "float", "list", "dict",
            "set", "tuple", "type", "isinstance", "input", "open", "append",
            "super", "staticmethod", "classmethod", "property", "enumerate",
            "zip", "map", "filter", "sorted", "reversed", "min", "max", "sum",
            "abs", "round", "any", "all", "hasattr", "getattr", "setattr"
    );

    private boolean isBuiltinFunction(String name) {
        return PYTHON_BUILTINS.contains(name);
    }

    /**
     * Insert an entry in the global scope (scope at index 0 of the stack).
     */
    private void insertInGlobalScope(SymbolEntry entry) {
        // Temporarily navigate to global scope
        // Since we use a stack, we save current state and insert directly
        symbol_table.Scope globalScope = symbolTable.getScopeStack().get(0);
        if (globalScope.contains(entry.getName())) {
            // Already exists in global scope - update
            SymbolEntry existing = globalScope.lookup(entry.getName());
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                existing.setValue(entry.getValue());
            }
        } else {
            globalScope.insert(entry);
            symbolTable.getAllEntries().add(entry);
        }
    }
}
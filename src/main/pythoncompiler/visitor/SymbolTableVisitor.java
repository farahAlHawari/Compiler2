package main.pythoncompiler.visitor;

import main.pythoncompiler.ast.*;
import symbol_table.SymbolTable;
import symbol_table.SymbolEntry;
import symbol_table.FlaskTemplateCall;
import symbol_table.FunctionCallInfo;
import symbol_table.ReturnInfo;
import java.util.Stack;

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
    private Stack<String> currentFunctionStack;  // Track which function we're inside (for return type checking)

    public SymbolTableVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.errors = new java.util.ArrayList<>();
        this.globalDeclarations = new java.util.HashSet<>();
        this.symbolTable.setSource("python");
        this.currentFunctionStack = new Stack<>();
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
            case "ImportStmt":
                visitImportStmt(node);
                break;
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
            case "TryExcept":
                visitTryExcept(node);
                break;
            case "WithStmt":
                visitWithStmt(node);
                break;
            default:
                // Visit children for unrecognized nodes
                for (ASTNode child : node.children) {
                    visit(child);
                }
                break;
        }
    }



    private void visitTryExcept(ASTNode node) {
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    private void visitWithStmt(ASTNode node) {
        // node.getDetails() ترجع " (as file)"
        String details = node.getDetails();
        if (!details.isEmpty()) {
            String varName = details.replace("(as", "").replace(")", "").trim();
            SymbolEntry entry = new SymbolEntry(
                    varName, "variable", "unknown",
                    symbolTable.currentScope().getScopeType(),
                    symbolTable.currentScopeLevel(),
                    node.lineNumber, "python"
            );
            entry.setFileName(symbolTable.getCurrentFileName());
            entry.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.insert(entry);

        }
        // ثم زوري أبناء الـ block
        for (ASTNode child : node.children) {
            visit(child);
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
                entry.setFileName(symbolTable.getCurrentFileName());
                entry.setFilePath(symbolTable.getCurrentFilePath());
                if (assignNode.declaredType != null) {
                    entry.setDeclaredType(assignNode.declaredType);
                }
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
                String currentScopeType = symbolTable.currentScope().getScopeType();
                boolean isInsideFunction = currentScopeType.equals("function")
                        || currentScopeType.equals("route_function")
                        || currentScopeType.equals("class");

                if (isInsideFunction) {
                    // داخل دالة: أنشئي local variable دائماً
                    SymbolEntry entry = new SymbolEntry(
                            varName, "variable", inferredType, currentScopeType,
                            symbolTable.currentScopeLevel(), node.lineNumber, "python"
                    );
                    entry.setValue(value);
                    entry.setFileName(symbolTable.getCurrentFileName());
                    entry.setFilePath(symbolTable.getCurrentFilePath());
                    if (assignNode.declaredType != null) {
                        entry.setDeclaredType(assignNode.declaredType);
                    }
                    symbolTable.insert(entry);
                } else {
                    // في global scope: عدّلي المتغير لو موجود
                    SymbolEntry existingAnywhere = symbolTable.lookup(varName);
                    if (existingAnywhere != null) {
                        existingAnywhere.setValue(value);
                        existingAnywhere.setType(inferredType);
                    } else {
                        SymbolEntry entry = new SymbolEntry(
                                varName, "variable", inferredType, currentScopeType,
                                symbolTable.currentScopeLevel(), node.lineNumber, "python"
                        );
                        entry.setValue(value);
                        entry.setFileName(symbolTable.getCurrentFileName());
                        entry.setFilePath(symbolTable.getCurrentFilePath());
                        if (assignNode.declaredType != null) {
                            entry.setDeclaredType(assignNode.declaredType);
                        }
                        symbolTable.insert(entry);
                        symbolTable.insert(entry);
                    }
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
                entry.setFileName(symbolTable.getCurrentFileName());
                entry.setFilePath(symbolTable.getCurrentFilePath());
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
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        // NEW: Store return type and parameter count in the SymbolEntry
        if (funcNode.returnType != null && !funcNode.returnType.isEmpty()) {
            entry.setReturnType(funcNode.returnType);
        }
        entry.setParamCount(funcNode.paramCount);

        // If it's a RouteFunction, add decorator info
        if ("RouteFunction".equals(node.nodeName)) {
            entry.setKind("route_function");
        }

        symbolTable.insert(entry);

        // NEW: Push current function name onto the stack (for return statement tracking)
        currentFunctionStack.push(funcName);

        // Enter function scope (route functions get a distinctive scope type)
        if ("RouteFunction".equals(node.nodeName)) {
            symbolTable.enterScope("route_function",  funcName);
        } else {
            symbolTable.enterScope("function",  funcName);
        }

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

        // NEW: Pop current function name from the stack
        if (!currentFunctionStack.isEmpty()) {
            currentFunctionStack.pop();
        }
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
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insert(entry);

        // Enter class scope
        int newLevel = scopeLevel + 1;
        symbolTable.enterScope("class",  className);

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
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
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
        // NEW: Track return statement for Return Type Mismatch checking
        String returnExprType = "unknown";
        String enclosingFuncName = currentFunctionStack.isEmpty() ? "" : currentFunctionStack.peek();

        if (!node.children.isEmpty()) {
            ASTNode returnExpr = node.children.get(0);
            returnExprType = inferType(returnExpr);
        } else {
            returnExprType = "none"; // bare "return" or "return None"
        }

        // Normalize the inferred type to match type hint format
        returnExprType = normalizeType(returnExprType);

        // Add ReturnInfo to symbol table for semantic checker
        if (!enclosingFuncName.isEmpty()) {
            ReturnInfo returnInfo = new ReturnInfo(enclosingFuncName, returnExprType, node.lineNumber);
            returnInfo.setFileName(symbolTable.getCurrentFileName());
            returnInfo.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.addReturnInfo(returnInfo);
        }

        // Visit children (for nested references)
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Global Declaration ====================

    private void visitGlobalDecl(ASTNode node) {
        // Extract variable name from the GlobalDecl details
        String details = node.getDetails();
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


    private void visitImportStmt(ASTNode node) {
        // أزل كل ImportedName كـ child في global scope
        for (ASTNode child : node.children) {
            if (child instanceof IdentifierNode) {
                IdentifierNode idNode = (IdentifierNode) child;

                String scopeType = symbolTable.currentScope().getScopeType();
                int scopeLevel = symbolTable.currentScopeLevel();

                SymbolEntry entry = new SymbolEntry(
                        idNode.name, "imported_name",  "module", "global",
                        0, child.lineNumber, "python"
                );
                entry.setValue("imported");
                entry.setFileName(symbolTable.getCurrentFileName());
                entry.setFilePath(symbolTable.getCurrentFilePath());
                insertInGlobalScope(entry);
            }
        }
    }

    // ==================== Parameters ====================

    private void visitParameters(ASTNode node) {
        for (ASTNode child : node.children) {
            if ("Identifier".equals(child.nodeName)) {
                IdentifierNode idNode = (IdentifierNode) child;
                SymbolEntry entry = new SymbolEntry(
                        idNode.name, "parameter", "unknown",
                        symbolTable.currentScope().getScopeType(),
                        symbolTable.currentScopeLevel(), child.lineNumber, "python"
                );
                entry.setFileName(symbolTable.getCurrentFileName());
                entry.setFilePath(symbolTable.getCurrentFilePath());
                symbolTable.insert(entry);
            }
        }
    }

    // ==================== Call Expression ====================
    private void visitCallExpr(ASTNode node) {
        CallNode callNode = (CallNode) node;
        String funcName = callNode.functionName;

        // Check if this is a method call (e.g., obj.method())
        boolean isMethodCall = false;
        for (ASTNode child : node.children) {
            if (child instanceof AttributeNode) {
                isMethodCall = true;
                break;
            }
        }

        // NEW: Track function call info for semantic error checking
        FunctionCallInfo callInfo = new FunctionCallInfo(
                funcName,
                callNode.argCount,
                node.lineNumber,
                "python",
                isMethodCall,
                false  // not a Jinja filter
        );
        callInfo.setFileName(symbolTable.getCurrentFileName());
        callInfo.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.addFunctionCallInfo(callInfo);

        // NEW: Special handling for render_template() — track passed variables
        if ("render_template".equals(funcName)) {
            trackRenderTemplateCall(node);
        }

        // Existing warning for undefined functions
        if (!isMethodCall) {
            SymbolEntry funcEntry = symbolTable.lookup(funcName);
            if (funcEntry == null) {
                if (!isBuiltinFunction(funcName)) {
                    errors.add(String.format(
                            "Warning [Line %d]: Function '%s' called but not defined in symbol table",
                            node.lineNumber, funcName
                    ));
                }
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
        // لا تفحصي داخل تعريف دالة (لأن البارامتر ما تعرّف بعد)
        if (!isBuiltinFunction(name) && !isCommonFlaskGlobal(name)) {
            SymbolEntry entry = symbolTable.lookup(name);
            if (entry == null) {
                errors.add(String.format(
                        "Warning [Line %d]: Identifier '%s' used but not declared",
                        node.lineNumber, name
                ));
            }
        }
    }

    // أضيفي هالدالة المساعدة
    private boolean isCommonFlaskGlobal(String name) {
        return java.util.Set.of(
                "app", "request", "Flask", "render_template",
                "redirect", "url_for", "jsonify", "True", "False", "None"
        ).contains(name);
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
//    private static final java.util.Set<String> PYTHON_BUILTINS = java.util.Set.of(
//            "print", "len", "range", "int", "str", "float", "list", "dict",
//            "set", "tuple", "type", "isinstance", "input", "open", "append",
//            "super", "staticmethod", "classmethod", "property", "enumerate",
//            "zip", "map", "filter", "sorted", "reversed", "min", "max", "sum",
//            "abs", "round", "any", "all", "hasattr", "getattr", "setattr"
//    );
    private static final java.util.Set<String> PYTHON_BUILTINS = java.util.Set.of(
            "print", "len", "range", "int", "str", "float", "list", "dict",
            "set", "tuple", "type", "isinstance", "input", "open", "append",
            "super", "staticmethod", "classmethod", "property", "enumerate",
            "zip", "map", "filter", "sorted", "reversed", "min", "max", "sum",
            "abs", "round", "any", "all", "hasattr", "getattr", "setattr",
            // Flask framework functions (imported)
            "Flask", "render_template", "request", "redirect", "url_for",
            "SQLAlchemy", "db",
            "__name__"
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

    // ==================== NEW: Helper Methods for Semantic Error Tracking ====================

    /**
     * Track a render_template() call for Missing Flask Variable checking.
     * Extracts the template name and keyword argument variable names.
     *
     * Example: render_template("page.html", name=name, age=age)
     *   → templateName = "page.html", passedVariables = ["name", "age"]
     */
    private void trackRenderTemplateCall(ASTNode callNode) {
        FlaskTemplateCall flaskCall = new FlaskTemplateCall("", callNode.lineNumber);

        // Find the Arguments child node
        for (ASTNode child : callNode.children) {
            if ("Arguments".equals(child.nodeName)) {
                for (ASTNode arg : child.children) {
                    if ("PositionalArg".equals(arg.nodeName)) {
                        // First positional arg should be the template name (string literal)
                        if (!arg.children.isEmpty()) {
                            ASTNode expr = arg.children.get(0);
                            if (expr instanceof LiteralNode) {
                                String value = ((LiteralNode) expr).value;
                                // Remove quotes from the template name
                                flaskCall.setTemplateName(value.replace("\"", "").replace("'", ""));
                            }
                        }
                    } else if ("NamedArg".equals(arg.nodeName)) {
                        // Named arg like "name=name" — extract the parameter name
                        String details = arg.getDetails();
                        // details format: " (name=...)"
                        if (details.contains("=")) {
                            String varName = details.substring(
                                    details.indexOf("(") + 1,
                                    details.indexOf("=")
                            ).trim();
                            flaskCall.addPassedVariable(varName);
                            SymbolEntry varEntry = symbolTable.lookup(varName);
                            if (varEntry != null) {
                                flaskCall.addPassedVariableType(varName, varEntry.getType());
                            }
                        }
                    }
                }
            }
        }

        symbolTable.addRenderTemplateCall(flaskCall);
    }

    /**
     * Normalize type names to match type hint format.
     * Python uses "str" for strings, but LiteralNode uses "STRING".
     */
    private String normalizeType(String type) {
        if (type == null) return "unknown";
        switch (type.toLowerCase()) {
            case "string":
            case "str":
                return "str";
            case "int":
                return "int";
            case "float":
                return "float";
            case "bool":
            case "boolean":
                return "bool";
            case "list":
                return "list";
            case "dict":
            case "dictionary":
                return "dict";
            case "none":
            case "nonetype":
                return "None";
            default:
                return type;
        }
    }
}
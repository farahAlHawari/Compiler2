package main.pythoncompiler.visitor;

import main.pythoncompiler.ast.*;
import symbol_table.*;
import java.util.Stack;


public class SymbolTableVisitor {

    private SymbolTable symbolTable;
    private java.util.List<String> errors;
    private java.util.Set<String> globalDeclarations;
    private Stack<String> currentFunctionStack;

    private int conditionalDepth;
    private java.util.Set<String> conditionallyAssignedVars;
    private java.util.Set<String> unconditionallyAssignedVars;

    public SymbolTableVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.errors = new java.util.ArrayList<>();
        this.globalDeclarations = new java.util.HashSet<>();
        this.symbolTable.setSource("python");
        this.currentFunctionStack = new Stack<>();

        this.conditionalDepth = 0;
        this.conditionallyAssignedVars = new java.util.HashSet<>();
        this.unconditionallyAssignedVars = new java.util.HashSet<>();
    }

    public java.util.List<String> getErrors() {
        return errors;
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    // ==================== Main Visit Method ====================

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
                break;
            case "TryExcept":
                visitTryExcept(node);
                break;
            case "WithStmt":
                visitWithStmt(node);
                break;
            default:
                for (ASTNode child : node.children) {
                    visit(child);
                }
                break;
        }
    }



    private void visitTryExcept(ASTNode node) {
        for (ASTNode child : node.children) {
            if ("Block".equals(child.nodeName)) {
                conditionalDepth++;
                visit(child);
                conditionalDepth--;
            } else {
                visit(child);
            }
        }
    }

    private void visitWithStmt(ASTNode node) {
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
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Program ====================

    private void visitProgram(ASTNode node) {
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Assignment ====================

    private void visitAssignment(ASTNode node) {
        AssignNode assignNode = (AssignNode) node;
        String varName = assignNode.variableName;
        String operator = assignNode.operator;

        if (operator.isEmpty()) {
            String currentScopeType = symbolTable.currentScope().getScopeType();
            SymbolEntry entry = new SymbolEntry(
                    varName, "variable", "unknown", currentScopeType,
                    symbolTable.currentScopeLevel(), node.lineNumber, "python"
            );
            entry.setDeclaredType(assignNode.declaredType);
            entry.setFileName(symbolTable.getCurrentFileName());
            entry.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.insert(entry);
            return;
        }


        String inferredType = "unknown";
        String value = "";

        if (!node.children.isEmpty()) {
            ASTNode valueNode = node.children.get(0);
            inferredType = inferType(valueNode);
            value = extractValue(valueNode);
        }

        if (globalDeclarations.contains(varName)) {
            SymbolEntry existing = symbolTable.lookup(varName);
            if (existing != null && "global".equals(existing.getScopeType())) {
                // Update existing global variable
                existing.setValue(value);
                existing.setType(inferredType);
            } else {
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
            globalDeclarations.remove(varName);
        } else if (operator.equals("=")) {
            SymbolEntry existingInCurrent = symbolTable.lookupCurrentScope(varName);
            if (existingInCurrent != null) {

                existingInCurrent.setValue(value);
                existingInCurrent.setType(inferredType);
            } else {
                String currentScopeType = symbolTable.currentScope().getScopeType();
                boolean isInsideFunction = currentScopeType.equals("function")
                        || currentScopeType.equals("route_function")
                        || currentScopeType.equals("class");

                if (isInsideFunction) {
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

                    }
                }
            }
        } else {



            String currentScopeType = symbolTable.currentScope().getScopeType();
            boolean isInsideFunction = currentScopeType.equals("function")
                    || currentScopeType.equals("route_function");


            SymbolEntry inCurrentScope = symbolTable.lookupCurrentScope(varName);
            boolean variableInCurrentScope = (inCurrentScope != null);


            SymbolEntry inAnyScope = symbolTable.lookup(varName);
            boolean variableInOuterScope = (inAnyScope != null && !variableInCurrentScope);


            boolean declaredGlobal = globalDeclarations.contains(varName);


            UnboundLocalInfo info = new UnboundLocalInfo(
                    varName, operator, node.lineNumber,
                    symbolTable.getCurrentFileName(),
                    symbolTable.getCurrentFilePath(),
                    currentScopeType,
                    symbolTable.currentScope().getContextName(),
                    isInsideFunction,
                    variableInCurrentScope,
                    variableInOuterScope,
                    declaredGlobal
            );
            symbolTable.addUnboundLocalInfo(info);
            String baseOp = operator.length() > 1
                    ? operator.substring(0, operator.length() - 1) : operator;

            String leftType = "unknown";
            SymbolEntry existingForType = symbolTable.lookup(varName);
            if (existingForType != null) {
                leftType = existingForType.getType();
            }

            String rightDisplay = "";
            if (!node.children.isEmpty()) {
                rightDisplay = extractValue(node.children.get(0));
            }

            if (isArithmeticOperator(baseOp) || isComparisonOperator(baseOp)) {
                OperationTypeInfo opInfo = new OperationTypeInfo(
                        operator, leftType, inferredType,
                        varName, rightDisplay, node.lineNumber
                );
                opInfo.setFileName(symbolTable.getCurrentFileName());
                opInfo.setFilePath(symbolTable.getCurrentFilePath());
                symbolTable.addOperationTypeInfo(opInfo);
            }
            if (variableInCurrentScope) {

                inCurrentScope.setValue(value);
            } else if (inAnyScope != null) {

                if (declaredGlobal) {

                    inAnyScope.setValue(value);
                }

            } else {

                SymbolEntry entry = new SymbolEntry(
                        varName, "variable", inferredType, currentScopeType,
                        symbolTable.currentScopeLevel(), node.lineNumber, "python"
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


        if (conditionalDepth > 0) {
            conditionallyAssignedVars.add(varName);
        } else {
            unconditionallyAssignedVars.add(varName);
            conditionallyAssignedVars.remove(varName);
        }


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
        if (funcNode.returnType != null && !funcNode.returnType.isEmpty()) {
            entry.setReturnType(funcNode.returnType);
        }
        entry.setParamCount(funcNode.paramCount);

        if ("RouteFunction".equals(node.nodeName)) {
            entry.setKind("route_function");
        }

        symbolTable.insert(entry);


        currentFunctionStack.push(funcName);

        if ("RouteFunction".equals(node.nodeName)) {
            symbolTable.enterScope("route_function",  funcName);
        } else {
            symbolTable.enterScope("function",  funcName);
        }


        conditionallyAssignedVars.clear();
        unconditionallyAssignedVars.clear();
        conditionalDepth = 0;


        for (ASTNode child : node.children) {
            if ("Parameters".equals(child.nodeName)) {
                visitParameters(child);
            }
        }

        for (ASTNode child : node.children) {
            if ("Block".equals(child.nodeName)) {
                visitBlock(child);
            }
        }


        symbolTable.exitScope();


        if (!currentFunctionStack.isEmpty()) {
            currentFunctionStack.pop();
        }
    }

    // ==================== Class Definition ====================

    private void visitClassDef(ASTNode node) {
        ClassDefNode classNode = (ClassDefNode) node;
        String className = classNode.className;


        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                className, "class", "class", scopeType,
                scopeLevel, node.lineNumber, "python"
        );
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insert(entry);


        int newLevel = scopeLevel + 1;
        symbolTable.enterScope("class",  className);


        for (ASTNode child : node.children) {
            visit(child);
        }


        symbolTable.exitScope();
    }

    // ==================== Block ====================

    private void visitBlock(ASTNode node) {

        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== If Statement ====================

    private void visitIfStatement(ASTNode node) {

        java.util.Set<String> savedCond = new java.util.HashSet<>(conditionallyAssignedVars);
        java.util.Set<String> savedUncond = new java.util.HashSet<>(unconditionallyAssignedVars);

        java.util.List<java.util.Set<String>> branchVarsList = new java.util.ArrayList<>();

        for (ASTNode child : node.children) {
            if (child.children == null || child.children.isEmpty()) continue;

            boolean hasCondition = !child.nodeName.equals("ElseBlock");

            conditionallyAssignedVars = new java.util.HashSet<>(savedCond);
            unconditionallyAssignedVars = new java.util.HashSet<>(savedUncond);

            if (hasCondition) {
                visit(child.children.get(0));
                for (int i = 1; i < child.children.size(); i++) {
                    conditionalDepth++;
                    visit(child.children.get(i));
                    conditionalDepth--;
                }
            } else {

                for (ASTNode bodyChild : child.children) {
                    conditionalDepth++;
                    visit(bodyChild);
                    conditionalDepth--;
                }
            }


            java.util.Set<String> thisBranchVars = new java.util.HashSet<>();
            java.util.Set<String> newCond = new java.util.HashSet<>(conditionallyAssignedVars);
            newCond.removeAll(savedCond);
            thisBranchVars.addAll(newCond);
            java.util.Set<String> newUncond = new java.util.HashSet<>(unconditionallyAssignedVars);
            newUncond.removeAll(savedUncond);
            thisBranchVars.addAll(newUncond);

            branchVarsList.add(thisBranchVars);
        }


        conditionallyAssignedVars = new java.util.HashSet<>(savedCond);
        unconditionallyAssignedVars = new java.util.HashSet<>(savedUncond);


        if (branchVarsList.size() >= 2) {

            java.util.Set<String> allBranches = new java.util.HashSet<>(branchVarsList.get(0));
            for (int i = 1; i < branchVarsList.size(); i++) {
                allBranches.retainAll(branchVarsList.get(i));
            }
            unconditionallyAssignedVars.addAll(allBranches);


            java.util.Set<String> anyBranch = new java.util.HashSet<>();
            for (java.util.Set<String> bv : branchVarsList) {
                anyBranch.addAll(bv);
            }
            anyBranch.removeAll(allBranches);
            conditionallyAssignedVars.addAll(anyBranch);
        } else if (branchVarsList.size() == 1) {

            conditionallyAssignedVars.addAll(branchVarsList.get(0));
        }
    }

    // ==================== While Loop ====================

    private void visitWhileLoop(ASTNode node) {
        for (ASTNode child : node.children) {
            if ("Block".equals(child.nodeName)) {
                conditionalDepth++;
                visit(child);
                conditionalDepth--;
            } else {
                visit(child);
            }
        }
    }

    // ==================== For Loop ====================

    private void visitForLoop(ASTNode node) {
        ForNode forNode = (ForNode) node;
        String iteratorName = forNode.iteratorName;


        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                iteratorName, "variable", "unknown", scopeType,
                scopeLevel, node.lineNumber, "python"
        );

        SymbolEntry existing = symbolTable.lookupCurrentScope(iteratorName);
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        if (existing == null) {
            symbolTable.insert(entry);
        } else {
            existing.setType("unknown");
        }


        for (ASTNode child : node.children) {
            if ("Block".equals(child.nodeName)) {
                conditionalDepth++;
                visit(child);
                conditionalDepth--;
            } else {
                visit(child);
            }
        }
    }

    // ==================== Return Statement ====================

    private void visitReturnStmt(ASTNode node) {

        String returnExprType = "unknown";
        String enclosingFuncName = currentFunctionStack.isEmpty() ? "" : currentFunctionStack.peek();

        if (!node.children.isEmpty()) {
            ASTNode returnExpr = node.children.get(0);
            returnExprType = inferType(returnExpr);
        } else {
            returnExprType = "none";
        }


        returnExprType = normalizeType(returnExprType);


        if (!enclosingFuncName.isEmpty()) {
            ReturnInfo returnInfo = new ReturnInfo(enclosingFuncName, returnExprType, node.lineNumber);
            returnInfo.setFileName(symbolTable.getCurrentFileName());
            returnInfo.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.addReturnInfo(returnInfo);
        }


        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Global Declaration ====================

    private void visitGlobalDecl(ASTNode node) {

        String details = node.getDetails();
        // Parse " (varName)" format
        String varName = details.replace("(", "").replace(")", "").trim();

        globalDeclarations.add(varName);


        if (!symbolTable.isGlobal(varName)) {

        }
    }

    // ==================== Import Statement ====================



    private void visitImportStmt(ASTNode node) {

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
                false
        );
        callInfo.setFileName(symbolTable.getCurrentFileName());
        callInfo.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.addFunctionCallInfo(callInfo);


        if ("render_template".equals(funcName)) {
            trackRenderTemplateCall(node);
        }


        if ("len".equals(funcName) && !isMethodCall) {
            checkLenArgumentType(node);
        }


        if (!isMethodCall) {
            checkBuiltinFunctionArgTypes(node);
        }

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

        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Identifier ====================

    private void visitIdentifier(ASTNode node) {
        IdentifierNode idNode = (IdentifierNode) node;
        String name = idNode.name;
        if (isBuiltinFunction(name) || isCommonFlaskGlobal(name)) return;

        SymbolEntry entry = symbolTable.lookup(name);
        String currentScopeType = symbolTable.currentScope().getScopeType();
        boolean isInsideFunction = currentScopeType.equals("function")
                || currentScopeType.equals("route_function")
                || currentScopeType.equals("class");

        if (entry == null) {

            boolean declaredLater = false;
            for (SymbolEntry e : symbolTable.getAllEntries()) {
                if (e.getName().equals(name) && e.getLine() > node.lineNumber) {
                    declaredLater = true;
                    break;
                }
            }
            UseBeforeInitInfo info = new UseBeforeInitInfo();
            info.setVariableName(name);
            info.setUsageLine(node.lineNumber);
            info.setFileName(symbolTable.getCurrentFileName());
            info.setFilePath(symbolTable.getCurrentFilePath());
            info.setScopeType(currentScopeType);
            info.setScopeContextName(symbolTable.currentScope().getContextName());
            info.setInsideFunction(isInsideFunction);
            info.setDeclaredLater(declaredLater);
            symbolTable.addUseBeforeInitInfo(info);

        } else if (entry.getDeclaredType() != null
                && !entry.getDeclaredType().isEmpty()
                && (entry.getValue() == null || entry.getValue().isEmpty())) {
            // Case 2: Has type annotation but no value (not initialized)
            UseBeforeInitInfo info = new UseBeforeInitInfo();
            info.setVariableName(name);
            info.setUsageLine(node.lineNumber);
            info.setFileName(symbolTable.getCurrentFileName());
            info.setFilePath(symbolTable.getCurrentFilePath());
            info.setScopeType(currentScopeType);
            info.setScopeContextName(symbolTable.currentScope().getContextName());
            info.setInsideFunction(isInsideFunction);
            info.setTypeAnnotationOnly(true);
            symbolTable.addUseBeforeInitInfo(info);

        } else if (conditionalDepth == 0
                && conditionallyAssignedVars.contains(name)
                && !unconditionallyAssignedVars.contains(name)) {
            // Case 3: Variable only assigned inside a conditional block
            UseBeforeInitInfo info = new UseBeforeInitInfo();
            info.setVariableName(name);
            info.setUsageLine(node.lineNumber);
            info.setFileName(symbolTable.getCurrentFileName());
            info.setFilePath(symbolTable.getCurrentFilePath());
            info.setScopeType(currentScopeType);
            info.setScopeContextName(symbolTable.currentScope().getContextName());
            info.setInsideFunction(isInsideFunction);
            info.setConditionalAssignment(true);
            symbolTable.addUseBeforeInitInfo(info);
        }
    }


    private boolean isCommonFlaskGlobal(String name) {
        return java.util.Set.of(
                "app", "request", "Flask", "render_template",
                "redirect", "url_for", "jsonify", "True", "False", "None"
        ).contains(name);
    }

    // ==================== Literal ====================

    private void visitLiteral(ASTNode node) {

    }

    // ==================== Binary Op ====================


    private void visitBinaryOp(ASTNode node) {

        for (ASTNode child : node.children) {
            visit(child);
        }

        if (node.children.size() < 2) return;


        String operator = null;
        if (node instanceof BinaryOpNode) {
            operator = ((BinaryOpNode) node).operator;
        }
        if (operator == null) return;


        if ("/".equals(operator) || "%".equals(operator)) {
            ASTNode divisorNode = node.children.get(1);
            boolean divisorIsLiteral = divisorNode instanceof LiteralNode;
            String divisorLiteralValue = divisorIsLiteral
                    ? ((LiteralNode) divisorNode).value : null;
            String divisorVariableName = (divisorNode instanceof IdentifierNode)
                    ? ((IdentifierNode) divisorNode).name : null;

            DivisionInfo divInfo = new DivisionInfo(
                    operator,
                    divisorIsLiteral,
                    divisorLiteralValue,
                    divisorVariableName,
                    node.lineNumber
            );

            divInfo.setFileName(symbolTable.getCurrentFileName());
            divInfo.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.addDivisionInfo(divInfo);
        }


        if (isArithmeticOperator(operator)) {
            checkOperationOnNone(node, operator);
        }

        if (isArithmeticOperator(operator) || isComparisonOperator(operator)) {
            recordOperationTypeInfo(node, operator);
        }
    }

    // ==================== Unary Op ====================

    private void visitUnaryOp(ASTNode node) {

        for (ASTNode child : node.children) {
            visit(child);
        }

        if (node instanceof UnaryOpNode) {
            UnaryOpNode unOp = (UnaryOpNode) node;
            String op = unOp.operator;

            if (!"+".equals(op) && !"-".equals(op)) return;

            if (!node.children.isEmpty()) {
                ASTNode operand = node.children.get(0);
                String operandType = inferType(operand);
                String operandDisplay = extractValue(operand);

                if (!"unknown".equals(operandType)) {
                    UnaryOpTypeInfo info = new UnaryOpTypeInfo(
                            op, operandType, operandDisplay, node.lineNumber
                    );
                    info.setFileName(symbolTable.getCurrentFileName());
                    info.setFilePath(symbolTable.getCurrentFilePath());
                    symbolTable.addUnaryOpTypeInfo(info);
                }
            }
        }
    }

    // ==================== Index Access ====================

    private void visitIndexAccess(ASTNode node) {
        if (node.children.size() >= 2) {
            ASTNode container = node.children.get(0);
            ASTNode index = node.children.get(1);

            String containerType = inferType(container);
            String indexType = inferType(index);
            String containerDisplay = extractValue(container);
            String indexDisplay = extractValue(index);

            IndexTypeInfo info = new IndexTypeInfo(
                    containerType, indexType, containerDisplay, indexDisplay, node.lineNumber
            );
            info.setFileName(symbolTable.getCurrentFileName());
            info.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.addIndexTypeInfo(info);
        }

        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Attribute Access ====================

    private void visitAttributeAccess(ASTNode node) {
        String objectName = null;
        String attributeName = null;
        int line = node.lineNumber;


        if (node instanceof AttributeNode) {
            attributeName = ((AttributeNode) node).attributeName;
        }


        if (node.children != null && node.children.size() > 0) {
            ASTNode objChild = node.children.get(0);
            if (objChild instanceof IdentifierNode) {
                objectName = ((IdentifierNode) objChild).name;
            } else {

                for (ASTNode child : node.children) {
                    visit(child);
                }
                return;
            }
        }


        if (node.children != null) {
            for (ASTNode child : node.children) {
                visit(child);
            }
        }


        if (objectName == null || attributeName == null || attributeName.isEmpty()) {
            return;
        }


        SymbolEntry entry = symbolTable.lookup(objectName);
        String objectType = "unknown";
        String objectValue = null;

        if (entry != null) {
            objectType = entry.getType() != null ? entry.getType() : "unknown";
            objectValue = entry.getValue() != null ? entry.getValue() : null;
        }


        AttributeAccessInfo info = new AttributeAccessInfo(objectName, attributeName, line);
        info.setFileName(symbolTable.getCurrentFileName());
        info.setFilePath(symbolTable.getCurrentFilePath());
        info.setObjectType(objectType);
        info.setObjectValue(objectValue);

        symbolTable.addAttributeAccessInfo(info);
    }
    // ==================== Container Literal ====================

    private void visitContainerLiteral(ASTNode node) {
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Helper Methods ====================


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
                    return "int";
                return "bool";
            case "UnaryOp":
                UnaryOpNode unOp = (UnaryOpNode) node;
                if ("not".equals(unOp.operator)) return "bool";
                return "unknown";
            case "Identifier":
                SymbolEntry entry = symbolTable.lookup(((IdentifierNode) node).name);
                return entry != null ? entry.getType() : "unknown";
            case "IndexAccess":
                return "unknown";
            case "AttributeAccess":
                return "unknown";
            default:
                return "unknown";
        }
    }


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


    private static final java.util.Set<String> PYTHON_BUILTINS = java.util.Set.of(
            "print", "len", "range", "int", "str", "float", "list", "dict",
            "set", "tuple", "type", "isinstance", "input", "open", "append",
            "super", "staticmethod", "classmethod", "property", "enumerate",
            "zip", "map", "filter", "sorted", "reversed", "min", "max", "sum",
            "abs", "round", "any", "all", "hasattr", "getattr", "setattr",

            "Flask", "render_template", "request", "redirect", "url_for",
            "SQLAlchemy", "db",
            "__name__"
    );

    private boolean isBuiltinFunction(String name) {
        return PYTHON_BUILTINS.contains(name);
    }


    private void insertInGlobalScope(SymbolEntry entry) {


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




    private void trackRenderTemplateCall(ASTNode callNode) {
        FlaskTemplateCall flaskCall = new FlaskTemplateCall("", callNode.lineNumber);


        for (ASTNode child : callNode.children) {
            if ("Arguments".equals(child.nodeName)) {
                for (ASTNode arg : child.children) {
                    if ("PositionalArg".equals(arg.nodeName)) {

                        if (!arg.children.isEmpty()) {
                            ASTNode expr = arg.children.get(0);
                            if (expr instanceof LiteralNode) {
                                String value = ((LiteralNode) expr).value;
                                // Remove quotes from the template name
                                flaskCall.setTemplateName(value.replace("\"", "").replace("'", ""));
                            }
                        }
                    } else if ("NamedArg".equals(arg.nodeName)) {

                        String details = arg.getDetails();

                        if (details.contains("=")) {
                            String varName = details.substring(
                                    details.indexOf("(") + 1,
                                    details.indexOf("=")
                            ).trim();
                            flaskCall.addPassedVariable(varName);
                            SymbolEntry varEntry = symbolTable.lookup(varName);
                            if (varEntry != null) {
                                flaskCall.addPassedVariableType(varName, varEntry.getType());
                                flaskCall.addPassedVariableValue(varName, varEntry.getValue());
                            }
                        }
                    }
                }
            }
        }

        symbolTable.addRenderTemplateCall(flaskCall);
    }


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

    private boolean isComparisonOperator(String operator) {
        return "<".equals(operator)
                || ">".equals(operator)
                || "<=".equals(operator)
                || ">=".equals(operator);
    }

    private void recordOperationTypeInfo(ASTNode node, String operator) {
        ASTNode left = node.children.get(0);
        ASTNode right = node.children.get(1);

        String leftType = inferType(left);
        String rightType = inferType(right);
        String leftDisplay = extractValue(left);
        String rightDisplay = extractValue(right);

        OperationTypeInfo info = new OperationTypeInfo(
                operator, leftType, rightType, leftDisplay, rightDisplay, node.lineNumber
        );
        info.setFileName(symbolTable.getCurrentFileName());
        info.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.addOperationTypeInfo(info);
    }


    private void checkLenArgumentType(ASTNode node) {
        ASTNode argsNode = null;
        for (ASTNode child : node.children) {
            if ("Arguments".equals(child.nodeName)) {
                argsNode = child;
                break;
            }
        }
        if (argsNode == null || argsNode.children.isEmpty()) return;

        ASTNode firstArgWrapper = argsNode.children.get(0);
        if (firstArgWrapper.children.isEmpty()) return;
        ASTNode argExpr = firstArgWrapper.children.get(0);

        String argType = inferType(argExpr);
        String argDisplay = extractValue(argExpr);

        FunctionArgTypeInfo info = new FunctionArgTypeInfo("len", argType, argDisplay, node.lineNumber);
        info.setFileName(symbolTable.getCurrentFileName());
        info.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.addFunctionArgTypeInfo(info);
    }

    private boolean isArithmeticOperator(String operator) {
        return "+".equals(operator)
                || "-".equals(operator)
                || "*".equals(operator)
                || "/".equals(operator)
                || "//".equals(operator)
                || "%".equals(operator)
                || "**".equals(operator);
    }

    private void checkOperationOnNone(ASTNode node, String operator) {
        ASTNode left = node.children.get(0);
        ASTNode right = node.children.get(1);

        boolean leftIsNone = isNodeNone(left);
        boolean rightIsNone = isNodeNone(right);

        if (leftIsNone || rightIsNone) {
            String leftName = getOperandName(left, "left");
            String rightName = getOperandName(right, "right");

            OperationOnNoneInfo info = new OperationOnNoneInfo(
                    operator, leftName, rightName, leftIsNone, rightIsNone, node.lineNumber
            );
            info.setFileName(symbolTable.getCurrentFileName());
            info.setFilePath(symbolTable.getCurrentFilePath());

            String otherType;
            if (leftIsNone) {
                otherType = getOperandName(right, "right");
            } else {
                otherType = getOperandName(left, "left");
            }
            info.setOtherOperandType(otherType);
            symbolTable.addOperationOnNoneInfo(info);
        }
    }

    private boolean isNodeNone(ASTNode node) {
        if (node instanceof LiteralNode) {
            LiteralNode lit = (LiteralNode) node;
            if ("None".equals(lit.value)) return true;
            if ("NONE".equals(lit.type)) return true;
        }

        if (node instanceof IdentifierNode) {
            String varName = ((IdentifierNode) node).name;
            SymbolEntry entry = symbolTable.lookup(varName);
            if (entry != null) {
                String type = entry.getType();
                String value = entry.getValue();
                if ("none".equals(type)) return true;
                if ("None".equals(value)) return true;
                if ("NONE".equals(type)) return true;
            }
        }

        return false;
    }

    private String getOperandName(ASTNode node, String side) {
        if (node instanceof IdentifierNode) {
            return ((IdentifierNode) node).name;
        } else if (node instanceof LiteralNode) {
            return ((LiteralNode) node).type.toLowerCase();
        }
        return side;
    }


    private void checkBuiltinFunctionArgTypes(ASTNode node) {
        if (!(node instanceof CallNode)) return;
        CallNode call = (CallNode) node;
        String funcName = call.functionName;


        java.util.Set<String> typeSensitiveBuiltins = java.util.Set.of(
                "sum", "sorted", "abs", "max", "min", "round"
        );
        if (!typeSensitiveBuiltins.contains(funcName)) return;


        ASTNode argsNode = null;
        for (ASTNode child : node.children) {
            if ("Arguments".equals(child.nodeName)) {
                argsNode = child;
                break;
            }
        }
        if (argsNode == null || argsNode.children.isEmpty()) return;

        ASTNode firstArgWrapper = argsNode.children.get(0);
        if (firstArgWrapper.children.isEmpty()) return;
        ASTNode argExpr = firstArgWrapper.children.get(0);

        String argType = inferType(argExpr);
        String argDisplay = extractValue(argExpr);

        FunctionArgTypeInfo info = new FunctionArgTypeInfo(
                funcName, argType, argDisplay, node.lineNumber
        );
        info.setFileName(symbolTable.getCurrentFileName());
        info.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.addFunctionArgTypeInfo(info);
    }
}
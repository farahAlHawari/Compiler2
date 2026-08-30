package Visitor;

import AST.Core.*;
import AST.Css.Rules.*;
import AST.Css.Selectors.*;
import AST.Css.Values.*;
import AST.Html.*;
import AST.Jinja.*;
import symbol_table.Scope;
import symbol_table.SymbolTable;
import symbol_table.SymbolEntry;
import symbol_table.FunctionCallInfo;
import symbol_table.OperationTypeInfo;


public class TemplateSymbolTableVisitor {

    private SymbolTable symbolTable;
    private java.util.List<String> errors;


    private static final java.util.Set<String> JINJA_BUILTINS = java.util.Set.of(
            "loop", "request", "session", "g", "config", "self",
            "range", "dict", "lipsum", "cycler", "joiner", "namespace",
            "true", "false", "none", "True", "False", "None"
    );


    private static final java.util.Set<String> JINJA_KEYWORDS = java.util.Set.of(
            "and", "or", "not", "in", "is", "True", "False", "None",
            "if", "else", "elif", "for", "endfor", "endif", "block",
            "endblock", "extends", "include", "import", "set", "macro",
            "endmacro", "with", "endwith", "filter", "endfilter",
            "length", "string", "int", "float", "list", "bool",
            "upper", "lower", "title", "trim", "default", "safe",
            "join", "first", "last", "count", "sort", "reverse"
    );

    private static final java.util.Set<String> JINJA_BUILTIN_FILTERS = java.util.Set.of(
            "upper", "lower", "title", "trim", "default", "safe",
            "join", "first", "last", "count", "sort", "reverse",
            "length", "string", "int", "float", "list", "bool",
            "round", "batch", "center", "e", "escape", "filesizeformat",
            "format", "indent", "replace", "truncate", "striptags",
            "wordcount", "capitalize", "xmlattr", "urlencode"
    );


    public TemplateSymbolTableVisitor(SymbolTable symbolTable) {
        this.symbolTable = symbolTable;
        this.errors = new java.util.ArrayList<>();
        this.symbolTable.setSource("template");
    }

    public SymbolTable getSymbolTable() {
        return symbolTable;
    }

    public java.util.List<String> getErrors() {
        return errors;
    }



    public void visit(ASTNode node) {
        if (node == null) return;

        if (node instanceof PageNode) {
            visitPage((PageNode) node);
        } else if (node instanceof HtmlElementNode) {

            visitHtmlElementChildren((HtmlElementNode) node);
        } else if (node instanceof HtmlAttributeNode) {

            visitHtmlAttribute((HtmlAttributeNode) node);
        } else if (node instanceof TextNode || node instanceof DoctypeNode) {

        } else if (node instanceof StyleBlockNode) {
            visitStyleBlock((StyleBlockNode) node);
        } else if (node instanceof StyleRuleNode) {
            visitStyleRule((StyleRuleNode) node);
        } else if (node instanceof DeclarationListNode) {
            visitDeclarationList((DeclarationListNode) node);
        } else if (node instanceof DeclarationNode) {
            visitDeclaration((DeclarationNode) node);
        } else if (node instanceof CombinedSelectorNode) {
            visitCombinedSelector((CombinedSelectorNode) node);
        } else if (node instanceof TypeSelectorNode) {
            visitTypeSelector((TypeSelectorNode) node);
        } else if (node instanceof ClassSelectorNode) {
            visitClassSelector((ClassSelectorNode) node);
        } else if (node instanceof IdSelectorNode) {
            visitIdSelector((IdSelectorNode) node);
        } else if (node instanceof UniversalSelectorNode) {
            visitUniversalSelector((UniversalSelectorNode) node);
        } else if (node instanceof PseudoClassNode) {
            visitPseudoClass((PseudoClassNode) node);
        } else if (node instanceof PseudoElementNode) {
            visitPseudoElement((PseudoElementNode) node);
        } else if (node instanceof AttributeSelectorNode) {
            visitAttributeSelector((AttributeSelectorNode) node);
        } else if (node instanceof CssValueNode) {

        } else if (node instanceof JinjaNode) {
            visitJinjaNode((JinjaNode) node);
        } else {
            for (ASTNode child : node.children) {
                visit(child);
            }
        }
    }

    // ==================== Page ====================

    private void visitPage(PageNode node) {
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== HTML Elements ====================


    private void visitHtmlElementChildren(HtmlElementNode node) {
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    private void visitHtmlAttribute(HtmlAttributeNode node) {
        String attrName = node.getAttrName();
        String attrValue = node.getAttrValue();


        if (attrName.equals("style")) return;


        if (attrValue == null || !attrValue.contains("{{")) return;


        java.util.regex.Pattern jinjaPattern =
                java.util.regex.Pattern.compile("\\{\\{(.*?)\\}\\}");
        java.util.regex.Matcher jinjaMatcher = jinjaPattern.matcher(attrValue);
        while (jinjaMatcher.find()) {
            String expr = jinjaMatcher.group(1).trim();
            extractVariablesFromExpression(expr, node.getLine());
        }
    }

    // ==================== CSS Style Block ====================

    private void visitStyleBlock(StyleBlockNode node) {

        symbolTable.enterScope("style",  "style");

        for (ASTNode child : node.children) {
            visit(child);
        }

        symbolTable.exitScope();
    }

    // ==================== CSS Rule ====================


    private void visitStyleRule(StyleRuleNode node) {
        String selectorText = extractSelectorText(node);
        int newLevel = symbolTable.currentScopeLevel() + 1;


        for (ASTNode child : node.children) {
            if (child instanceof SelectorNode || child instanceof CombinedSelectorNode) {
                visit(child);
            }
        }


        symbolTable.enterScope("style",  selectorText);


        for (ASTNode child : node.children) {
            if (!(child instanceof SelectorNode || child instanceof CombinedSelectorNode)) {
                visit(child);
            }
        }

        symbolTable.exitScope();
    }

    private String extractSelectorText(StyleRuleNode node) {
        StringBuilder sb = new StringBuilder();
        for (ASTNode child : node.children) {
            if (child instanceof SelectorNode || child instanceof CombinedSelectorNode) {
                String clean = cleanSelectorName(child.nodeName);
                sb.append(clean).append(" ");
            }
        }
        String result = sb.toString().trim();
        return result.isEmpty() ? "unknown" : result;
    }

    // ==================== CSS Selectors ====================

    private void visitSelector(SelectorNode node) {
        String cleanName = cleanSelectorName(node.nodeName);
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                cleanName, "css_selector", "selector", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insertCssSelector(entry);

        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    private void visitCombinedSelector(CombinedSelectorNode node) {
        StringBuilder selectorText = new StringBuilder();
        for (ASTNode child : node.children) {
            selectorText.append(cleanSelectorName(child.nodeName)).append(" ");
        }

        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                selectorText.toString().trim(), "css_selector", "combined_selector",
                scopeType, scopeLevel, node.getLine(), "template"
        );
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insertCssSelector(entry);

        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    private void visitTypeSelector(TypeSelectorNode node) {
        String cleanName = node.nodeName.replace("TypeSelector: ", "");
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                cleanName, "css_selector", "type_selector", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insertCssSelector(entry);
    }

    private void visitClassSelector(ClassSelectorNode node) {
        String cleanName = node.nodeName.replace("ClassSelector: ", "");
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                cleanName, "css_selector", "class_selector", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insertCssSelector(entry);
    }

    private void visitIdSelector(IdSelectorNode node) {
        String cleanName = node.nodeName.replace("IDSelector: ", "");
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                cleanName, "css_selector", "id_selector", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insertCssSelector(entry);
    }

    private void visitUniversalSelector(UniversalSelectorNode node) {
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                "*", "css_selector", "universal_selector", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insertCssSelector(entry);
    }

    private void visitPseudoClass(PseudoClassNode node) {
        String cleanName = ":" + node.nodeName.replace("PseudoClassSelector :", "");
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                cleanName, "css_selector", "pseudo_class", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insertCssSelector(entry);
    }

    private void visitPseudoElement(PseudoElementNode node) {
        String cleanName = "::" + node.nodeName.replace("PseudoElementSelector ::", "");
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                cleanName, "css_selector", "pseudo_element", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insertCssSelector(entry);
    }

    private void visitAttributeSelector(AttributeSelectorNode node) {
        String cleanName = "[" + node.nodeName.replace("AttributeSelector ", "") + "]";
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                cleanName, "css_selector", "attribute_selector", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insertCssSelector(entry);
    }

    // ==================== CSS Declarations ====================

    private void visitDeclarationList(DeclarationListNode node) {
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    private void visitDeclaration(DeclarationNode node) {
        String propertyName = node.getProperty();
        String propertyValue = node.getValue();

        if (propertyName.isEmpty()) {
            propertyName = "unknown_property";
        }

        String scopeType = symbolTable.currentScope().getScopeType();
        String contextName = symbolTable.currentScope().getContextName();
        int scopeLevel = symbolTable.currentScopeLevel();

        String displayScope = scopeType.equals("style") ?
                "style/" + contextName : scopeType;

        SymbolEntry entry = new SymbolEntry(
                propertyName, "css_property", "property", displayScope,
                scopeLevel, node.getLine(), "template"
        );
        entry.setValue(propertyValue);
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insertCssProperty(entry);

        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Jinja Nodes ====================

    private void visitJinjaNode(JinjaNode node) {
        String name = node.nodeName;

        if (node instanceof JinjaIfNode) {
            visitJinjaIf(node);
        } else if (name.startsWith("JinjaElif")) {

            visitJinjaElif(node);
        } else if (name.startsWith("JinjaElse")) {

            visitJinjaElse(node);
        } else if (name.startsWith("JinjaFor")) {
            visitJinjaFor(node);
        } else if (name.startsWith("JinjaBlock")) {
            visitJinjaBlock(node);
        } else if (name.startsWith("JinjaExpression")) {
            visitJinjaExpression(node);
        } else if (name.startsWith("JinjaExtends")) {
            visitJinjaExtends(node);
        } else if (name.startsWith("JinjaInclude")) {
            visitJinjaInclude(node);
        } else if (name.startsWith("JinjaImport")) {
            visitJinjaImport(node);
        } else if (name.startsWith("JinjaSet")) {
            visitJinjaSet(node);
        }
     else if (name.startsWith("JinjaWith")) {
        visitJinjaWith(node);}
        else if (name.startsWith("JinjaMacro")) {
            visitJinjaMacro(node);
        }
        else if (name.startsWith("JinjaSimple")) {
            visitJinjaSimpleFallback(node);
        }
        else {
            for (ASTNode child : node.children) {
                visit(child);
            }
        }
    }

    private void visitJinjaSimpleFallback(JinjaNode node) {
        String content = node.nodeName.substring("JinjaSimple".length()).trim();

        if (content.startsWith("for ")) {

            handleSimpleFor(content, node.getLine());

        } else if (content.startsWith("if ")) {

            handleSimpleIf(content, node.getLine());

        } else if (content.startsWith("elif ")) {

            String condition = content.substring(5).trim();
            extractVariablesFromExpression(condition, node.getLine());

        } else if (content.startsWith("else")) {


        } else if (content.startsWith("set ")) {

            handleSimpleSet(content, node.getLine());

        } else if (content.startsWith("macro ")) {

            handleSimpleMacro(content, node.getLine());

        } else if (content.startsWith("include ")) {

            String templateName = extractQuotedString(content);
            String scopeType = symbolTable.currentScope().getScopeType();
            int scopeLevel = symbolTable.currentScopeLevel();
            SymbolEntry entry = new SymbolEntry(
                    "include", "jinja_include", "template_ref", scopeType,
                    scopeLevel, node.getLine(), "template"
            );
            entry.setValue(templateName);
            entry.setFileName(symbolTable.getCurrentFileName());
            entry.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.insert(entry);

        } else if (content.startsWith("with ")) {

            String withExpr = content.substring(5).trim();
            symbolTable.enterScope("jinja_block", "with");
            if (withExpr.contains("=")) {
                String varName = withExpr.substring(0, withExpr.indexOf("=")).trim();
                String valueExpr = withExpr.substring(withExpr.indexOf("=") + 1).trim();
                SymbolEntry entry = new SymbolEntry(
                        varName, "variable", "jinja_set_var",
                        "jinja_block:with", symbolTable.currentScopeLevel(),
                        node.getLine(), "template"
                );
                entry.setValue(valueExpr);
                entry.setDeclaredType(inferTypeFromJinjaValue(valueExpr));
                entry.setFileName(symbolTable.getCurrentFileName());
                entry.setFilePath(symbolTable.getCurrentFilePath());
                symbolTable.insert(entry);
                extractVariablesFromExpression(valueExpr, node.getLine());
            }
            symbolTable.exitScope();

        } else {

            for (ASTNode child : node.children) {
                visit(child);
            }
        }
    }

    private void handleSimpleFor(String content, int line) {
        String expr = content.substring(4).trim();
        String iteratorName = "";
        String iterableName = "";

        if (expr.contains(" in ")) {
            String[] parts = expr.split(" in ", 2);
            iteratorName = parts[0].trim();
            iterableName = parts[1].trim();
            if (iterableName.contains(".")) {
                iterableName = iterableName.split("\\.")[0].trim();
            }
        }

        if (!iteratorName.isEmpty()) {

                    symbol_table.JinjaFilterUsage usage =
                            new symbol_table.JinjaFilterUsage(iterableName, null, "for_loop", line);
            usage.setFileName(symbolTable.getCurrentFileName());
            usage.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.addJinjaFilterUsage(usage);


            String scopeType = symbolTable.currentScope().getScopeType();
            int scopeLevel = symbolTable.currentScopeLevel();
            SymbolEntry iterEntry = new SymbolEntry(
                    iteratorName, "variable", "jinja_iterator",
                    scopeType, scopeLevel, line, "template"
            );
            iterEntry.setFileName(symbolTable.getCurrentFileName());
            iterEntry.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.insert(iterEntry);

        }


        if (!iterableName.isEmpty() && symbolTable.lookupCurrentScope(iterableName) == null) {
            String scopeType = symbolTable.currentScope().getScopeType();
            int scopeLevel = symbolTable.currentScopeLevel();
            SymbolEntry iterableEntry = new SymbolEntry(
                    iterableName, "variable", "jinja_iterable",
                    scopeType, scopeLevel, line, "template"
            );
            iterableEntry.setFileName(symbolTable.getCurrentFileName());
            iterableEntry.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.insert(iterableEntry);
        }
    }

    private void handleSimpleIf(String content, int line) {

        String condition = content.substring(3).trim();
        extractVariablesFromExpression(condition, line);
    }



private void handleSimpleSet(String content, int line) {
    String setExpr = content.substring(4).trim();
    if (setExpr.contains("=")) {
        String varName = setExpr.substring(0, setExpr.indexOf("=")).trim();
        String valueExpr = setExpr.substring(setExpr.indexOf("=") + 1).trim();

        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                varName, "variable", "jinja_set_var", scopeType,
                scopeLevel, line, "template"
        );
        entry.setValue(valueExpr);


        String inferredType = inferTypeFromJinjaValue(valueExpr);
        entry.setDeclaredType(inferredType);

        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insert(entry);

        extractVariablesFromExpression(valueExpr, line);
    } else {
        String varName = setExpr.trim();
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();
        SymbolEntry entry = new SymbolEntry(
                varName, "variable", "jinja_set_var", scopeType,
                scopeLevel, line, "template"
        );
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insert(entry);
    }
}


    private String inferTypeFromJinjaValue(String valueExpr) {
        if (valueExpr == null || valueExpr.trim().isEmpty()) return "unknown";
        valueExpr = valueExpr.trim();

        if ((valueExpr.startsWith("\"") && valueExpr.endsWith("\""))
                || (valueExpr.startsWith("'") && valueExpr.endsWith("'"))) return "string";
        if (valueExpr.startsWith("[") && valueExpr.endsWith("]")) return "list";
        if (valueExpr.startsWith("{") && valueExpr.endsWith("}")) return "dict";
        if (valueExpr.startsWith("(") && valueExpr.endsWith(")")) return "tuple";
        if ("true".equalsIgnoreCase(valueExpr) || "false".equalsIgnoreCase(valueExpr)) return "bool";
        if ("none".equalsIgnoreCase(valueExpr) || "null".equalsIgnoreCase(valueExpr)) return "NoneType";
        if (valueExpr.matches("-?\\d+")) return "int";
        if (valueExpr.matches("-?\\d+\\.\\d+")) return "float";

        SymbolEntry refEntry = symbolTable.lookup(valueExpr);
        if (refEntry != null) {
            String t = refEntry.getDeclaredType();
            if (t != null && !t.isEmpty()) return t;
            String regularType = refEntry.getType();
            if (regularType != null && !"jinja_set_var".equals(regularType)) return regularType;
        }
        return "unknown";
    }
    private void handleSimpleMacro(String content, int line) {

        String rest = content.substring(6).trim();
        int parenStart = rest.indexOf("(");
        int parenEnd = rest.lastIndexOf(")");

        if (parenStart == -1) return;

        String macroName = rest.substring(0, parenStart).trim();
        String paramsStr = (parenEnd > parenStart) ?
                rest.substring(parenStart + 1, parenEnd).trim() : "";


        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();
        SymbolEntry entry = new SymbolEntry(
                macroName, "function", "jinja_macro", scopeType,
                scopeLevel, line, "template"
        );
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insert(entry);


        symbolTable.enterScope("jinja_block", "macro:" + macroName);

        if (!paramsStr.isEmpty()) {
            String[] params = paramsStr.split(",");
            for (String param : params) {
                param = param.trim();
                if (!param.isEmpty()) {
                    SymbolEntry paramEntry = new SymbolEntry(
                            param, "parameter", "macro_param",
                            "jinja_block:macro:" + macroName,
                            symbolTable.currentScopeLevel(),
                            line, "template"
                    );
                    paramEntry.setFileName(symbolTable.getCurrentFileName());
                    paramEntry.setFilePath(symbolTable.getCurrentFilePath());
                    symbolTable.insert(paramEntry);
                }
            }
        }


        symbolTable.exitScope();
    }


    private void visitJinjaIf(JinjaNode node) {
        String condition = extractJinjaCondition(node.nodeName);
        extractVariablesFromExpression(condition, node.getLine());

        symbolTable.enterScope("jinja_block", "if");
        processIfBranchChildren(node);

        Scope exited = symbolTable.exitScope();
        if (exited != null) {
            promoteVariablesToParent(exited);
        }
    }


    private void processIfBranchChildren(ASTNode branchNode) {
        for (ASTNode child : branchNode.children) {
            if (child instanceof JinjaNode) {
                String childName = child.nodeName;

                if (childName.startsWith("JinjaElse")) {
                    Scope exited = symbolTable.exitScope();
                    promoteVariablesToParent(exited);
                    symbolTable.enterScope("jinja_block", "else");


                } else if (childName.startsWith("JinjaElif")) {
                    Scope exited = symbolTable.exitScope();
                    promoteVariablesToParent(exited);
                    String elifCond = extractJinjaCondition(childName);
                    extractVariablesFromExpression(elifCond, child.getLine());
                    symbolTable.enterScope("jinja_block", "elif");


                } else if (childName.startsWith("JinjaEndIf")
                        || childName.startsWith("JinjaEndFor")
                        || childName.startsWith("JinjaEndBlock")
                        || childName.startsWith("JinjaEndMacro")) {


                } else {
                    visit(child);
                }
            } else {
                visit(child);
            }
        }
    }

    private void visitJinjaElif(JinjaNode node) {
        String condition = extractJinjaCondition(node.nodeName);
        extractVariablesFromExpression(condition, node.getLine());

        int newLevel = symbolTable.currentScopeLevel() + 1;
        symbolTable.enterScope("jinja_block",  "elif");

        for (ASTNode child : node.children) {
            visit(child);
        }

        Scope exited = symbolTable.exitScope();
        if (exited != null) {
            promoteVariablesToParent(exited);
        }
    }


    private void visitJinjaElse(JinjaNode node) {
        int newLevel = symbolTable.currentScopeLevel() + 1;
        symbolTable.enterScope("jinja_block",  "else");

        for (ASTNode child : node.children) {
            visit(child);
        }

        Scope exited = symbolTable.exitScope();
        if (exited != null) {
            promoteVariablesToParent(exited);
        }
    }


    private void visitJinjaFor(JinjaNode node) {
        String expr = node instanceof JinjaForNode ?
                ((JinjaForNode) node).getForExpr() : extractJinjaCondition(node.nodeName);

        String iteratorName = "";
        String iterableName = "";

        if (expr.contains(" in ")) {
            String[] parts = expr.split(" in ", 2);
            iteratorName = parts[0].trim();
            iterableName = parts[1].trim();
            if (iterableName.contains(".")) {
                iterableName = iterableName.split("\\.")[0].trim();
            }
        }

        symbolTable.enterScope("jinja_block", "for:" + iteratorName);

        if (!iteratorName.isEmpty()) {


                    symbol_table.JinjaFilterUsage usage =
                            new symbol_table.JinjaFilterUsage(iterableName, null, "for_loop", node.getLine());
            usage.setFileName(symbolTable.getCurrentFileName());
            usage.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.addJinjaFilterUsage(usage);

            SymbolEntry iterEntry = new SymbolEntry(
                    iteratorName, "variable", "jinja_iterator",
                    "jinja_block:for:" + iteratorName,
                    symbolTable.currentScopeLevel(),
                    node.getLine(), "template"
            );
            iterEntry.setFileName(symbolTable.getCurrentFileName());
            iterEntry.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.insert(iterEntry);
        }


        if (!iterableName.isEmpty() && symbolTable.lookupCurrentScope(iterableName) == null) {
            SymbolEntry iterableEntry = new SymbolEntry(
                    iterableName, "variable", "jinja_iterable",
                    "jinja_block:for:" + iteratorName,
                    symbolTable.currentScopeLevel(),
                    node.getLine(), "template"
            );
            iterableEntry.setFileName(symbolTable.getCurrentFileName());
            iterableEntry.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.insert(iterableEntry);
        }

        for (ASTNode child : node.children) {
            visit(child);
        }

        symbolTable.exitScope();
    }

    private void visitJinjaBlock(JinjaNode node) {
        String blockName = ((JinjaBlockNode) node).getBlockName();

        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                "block:" + blockName, "jinja_block", "block", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insert(entry);


        symbolTable.enterScope("jinja_block",  "block:" + blockName);

        for (ASTNode child : node.children) {
            visit(child);
        }

        symbolTable.exitScope();
    }

    private void visitJinjaExpression(JinjaNode node) {
        String expr = node.nodeName
                .replace("JinjaExpression", "")
                .replace("{{", "").replace("}}", "").trim();


        extractVariablesFromExpression(expr, node.getLine());


        detectJinjaOperationTypes(expr, node.getLine());
    }

    private void visitJinjaExtends(JinjaNode node) {
        String templateName = extractQuotedString(node.nodeName);

        SymbolEntry entry = new SymbolEntry(
                templateName,
                "jinja_extends", "template_ref",
                symbolTable.currentScope().getScopeType(),
                symbolTable.currentScopeLevel(),
                node.getLine(), "template"
        );
        entry.setValue(templateName);
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insert(entry);
    }

    private void visitJinjaInclude(JinjaNode node) {
        String templateName = extractQuotedString(node.nodeName);

        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                "include", "jinja_include", "template_ref", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        entry.setValue(templateName);
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insert(entry);
    }

    private void visitJinjaImport(JinjaNode node) {
        String importText = node.nodeName.replace("JinjaImport", "").trim();

        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                "import", "jinja_import", "import", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        entry.setValue(importText);
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insert(entry);
    }


private void visitJinjaSet(JinjaNode node) {
    String setText = node.nodeName.replace("JinjaSet", "").trim();
    if (setText.contains("=")) {
        String varName = setText.substring(0, setText.indexOf("=")).trim();
        String valueExpr = setText.substring(setText.indexOf("=") + 1).trim();

        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                varName, "variable", "jinja_set_var", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        entry.setValue(valueExpr);


        String inferredType = inferTypeFromJinjaValue(valueExpr);
        entry.setDeclaredType(inferredType);

        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insert(entry);

        extractVariablesFromExpression(valueExpr, node.getLine());
    }
}

private void visitJinjaWith(JinjaNode node) {
    int newLevel = symbolTable.currentScopeLevel() + 1;
    symbolTable.enterScope("jinja_block",  "with");

    String withText = node.nodeName.replace("JinjaWith", "").trim();
    if (withText.contains("=")) {
        String varName = withText.substring(0, withText.indexOf("=")).trim();
        String valueExpr = withText.substring(withText.indexOf("=") + 1).trim();
        SymbolEntry entry = new SymbolEntry(
                varName, "variable", "jinja_set_var", "jinja_block",
                newLevel, node.getLine(), "template"
        );
        entry.setValue(valueExpr);


        entry.setDeclaredType(inferTypeFromJinjaValue(valueExpr));

        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insert(entry);
    }

    for (ASTNode child : node.children) { visit(child); }
    symbolTable.exitScope();
}
    private void visitJinjaMacro(JinjaNode node) {
        String macroName = ((JinjaMacroNode) node).getMacroName();
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                macroName, "function", "jinja_macro", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insert(entry);

        symbolTable.enterScope("jinja_block", "macro:" + macroName);

        JinjaMacroNode macroNode = (JinjaMacroNode) node;
        for (String param : macroNode.getParameters()) {
            SymbolEntry paramEntry = new SymbolEntry(
                    param, "parameter", "macro_param",
                    "jinja_block:macro:" + macroName,
                    symbolTable.currentScopeLevel(),
                    node.getLine(), "template"
            );
            paramEntry.setFileName(symbolTable.getCurrentFileName());
            paramEntry.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.insert(paramEntry);
        }

        for (ASTNode child : node.children) { visit(child); }
        symbolTable.exitScope();
    }



    private void promoteVariablesToParent(Scope exitedScope) {
        if (exitedScope == null || symbolTable.getScopeStack().isEmpty()) return;


        Scope parentScope = symbolTable.getScopeStack().peek();

        for (SymbolEntry entry : exitedScope.getAllSymbols()) {

            if (entry.getType().equals("jinja_set_var")) {
                if (!parentScope.contains(entry.getName())) {
                    SymbolEntry promotedEntry = new SymbolEntry(
                            entry.getName(), entry.getKind(), entry.getType(),
                            parentScope.getScopeType(), parentScope.getScopeLevel(),
                            entry.getLine(), entry.getSource()
                    );
                    promotedEntry.setValue(entry.getValue());
                    promotedEntry.setDeclaredType(entry.getDeclaredType());
                    promotedEntry.setFileName(symbolTable.getCurrentFileName());
                    promotedEntry.setFilePath(symbolTable.getCurrentFilePath());
                    parentScope.insert(promotedEntry);
                    symbolTable.getAllEntries().add(promotedEntry);
                }
            }
        }
    }


    private String cleanSelectorName(String nodeName) {
        if (nodeName == null) return "unknown";

        nodeName = nodeName.replace("TypeSelector: ", "");
        nodeName = nodeName.replace("ClassSelector: ", "");
        nodeName = nodeName.replace("IDSelector: ", "");
        nodeName = nodeName.replace("PseudoClassSelector :", ":");
        nodeName = nodeName.replace("PseudoElementSelector ::", "::");
        nodeName = nodeName.replace("UniversalSelector *", "*");
        nodeName = nodeName.replace("AttributeSelector ", "[");

        if (nodeName.startsWith("[") && !nodeName.endsWith("]")) {
            nodeName = nodeName + "]";
        }

        return nodeName;
    }

    private String extractJinjaCondition(String nodeName) {
        int parenStart = nodeName.indexOf("(");
        int parenEnd = nodeName.lastIndexOf(")");
        if (parenStart != -1 && parenEnd != -1 && parenEnd > parenStart) {
            return nodeName.substring(parenStart + 1, parenEnd).trim();
        }
        return "";
    }

    private String extractQuotedString(String text) {
        int start = text.indexOf("\"");
        int end = text.lastIndexOf("\"");
        if (start != -1 && end != -1 && end > start) {
            return text.substring(start + 1, end);
        }
        start = text.indexOf("'");
        end = text.lastIndexOf("'");
        if (start != -1 && end != -1 && end > start) {
            return text.substring(start + 1, end);
        }
        return text.replaceAll("[\"']", "").trim();
    }


    private void extractVariablesFromExpression(String expr, int line) {
        if (expr == null || expr.isEmpty()) return;

        expr = expr.replaceAll("\"[^\"]*\"", "").replaceAll("'[^']*'", "");

        java.util.regex.Pattern varPattern = java.util.regex.Pattern.compile(
                "\\b([a-zA-Z_][a-zA-Z0-9_]*)(\\.[a-zA-Z_][a-zA-Z0-9_]*)*\\b"
        );
        java.util.regex.Matcher matcher = varPattern.matcher(expr);

        while (matcher.find()) {
            String token = matcher.group(1);

            if (token.isEmpty()) continue;
            if (JINJA_BUILTINS.contains(token)) continue;
            if (JINJA_KEYWORDS.contains(token)) continue;
            if (token.matches("\\d+.*")) continue;


            if (symbolTable.lookupCurrentScope(token) == null) {
                String scopeType = symbolTable.currentScope().getScopeType();
                int scopeLevel = symbolTable.currentScopeLevel();

                SymbolEntry entry = new SymbolEntry(
                        token, "jinja_var", "context_var", scopeType,
                        scopeLevel, line, "template"
                );
                entry.setFileName(symbolTable.getCurrentFileName());
                entry.setFilePath(symbolTable.getCurrentFilePath());
                symbolTable.insert(entry);
            }
        }



        java.util.regex.Pattern divPattern = java.util.regex.Pattern.compile(
                "([a-zA-Z_][a-zA-Z0-9_.]*|\\d+(?:\\.\\d+)?)\\s*([/%])\\s*([a-zA-Z_][a-zA-Z0-9_.]*|\\d+(?:\\.\\d+)?)"
        );
        java.util.regex.Matcher divMatcher = divPattern.matcher(expr);
        while (divMatcher.find()) {
            String divisorToken = divMatcher.group(3).trim();
            String operatorToken = divMatcher.group(2).trim();

            boolean divisorIsNumericLiteral = divisorToken.matches("\\d+(?:\\.\\d+)?");
            String filterNameForDivision = "__division__" + operatorToken;

            if (divisorIsNumericLiteral) {

                symbol_table.JinjaFilterUsage divUsage = new symbol_table.JinjaFilterUsage(
                        divisorToken, filterNameForDivision, "division_literal", line
                );
                divUsage.setFileName(symbolTable.getCurrentFileName());
                divUsage.setFilePath(symbolTable.getCurrentFilePath());
                symbolTable.addJinjaFilterUsage(divUsage);
            } else if (!JINJA_BUILTINS.contains(divisorToken)) {

                symbol_table.JinjaFilterUsage divUsage = new symbol_table.JinjaFilterUsage(
                        divisorToken, filterNameForDivision, "division_variable", line
                );
                divUsage.setFileName(symbolTable.getCurrentFileName());
                divUsage.setFilePath(symbolTable.getCurrentFilePath());
                symbolTable.addJinjaFilterUsage(divUsage);
            }
        }


        java.util.regex.Pattern funcCallPattern = java.util.regex.Pattern.compile(
                "\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(([^)]*)\\)"
        );
        java.util.regex.Matcher funcMatcher = funcCallPattern.matcher(expr);
        while (funcMatcher.find()) {
            String calledFuncName = funcMatcher.group(1);
            String argsStr = funcMatcher.group(2).trim();

            if (JINJA_BUILTINS.contains(calledFuncName)) continue;
            if (JINJA_KEYWORDS.contains(calledFuncName)) continue;


            int macroArgCount = 0;
            if (!argsStr.isEmpty()) {

                macroArgCount = countArguments(argsStr);
            }

            FunctionCallInfo macroCall = new FunctionCallInfo(
                    calledFuncName, macroArgCount, line, "template", false, false
            );
            macroCall.setFileName(symbolTable.getCurrentFileName());
            macroCall.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.addFunctionCallInfo(macroCall);
        }

        if (expr.contains("|")) {
            String[] parts = expr.split("\\|");
            for (int i = 1; i < parts.length; i++) {
                String filterName = parts[i].trim();

                if (filterName.contains("(")) {
                    filterName = filterName.substring(0, filterName.indexOf("(")).trim();
                }
                String varName = parts[0].trim();
                if (!varName.isEmpty() && !JINJA_KEYWORDS.contains(varName)) {


                    symbol_table.JinjaFilterUsage usage =
                            new symbol_table.JinjaFilterUsage(varName, filterName, "filter", line);
                    usage.setFileName(symbolTable.getCurrentFileName());
                    usage.setFilePath(symbolTable.getCurrentFilePath());
                    symbolTable.addJinjaFilterUsage(usage);

                }
                if (!filterName.isEmpty() && !JINJA_BUILTIN_FILTERS.contains(filterName)
                        && !JINJA_KEYWORDS.contains(filterName)) {

                    int filterArgCount = 0;
                    if (parts[i].contains("(") && parts[i].contains(")")) {
                        String argsStr = parts[i].substring(
                                parts[i].indexOf("(") + 1,
                                parts[i].lastIndexOf(")")
                        ).trim();
                        if (!argsStr.isEmpty()) {
                            filterArgCount = argsStr.split(",").length;
                        }
                    }
                    FunctionCallInfo filterCall = new FunctionCallInfo(
                            filterName, filterArgCount, line, "template", false, true
                    );
                    filterCall.setFileName(symbolTable.getCurrentFileName());
                    filterCall.setFilePath(symbolTable.getCurrentFilePath());
                    symbolTable.addFunctionCallInfo(filterCall);
                }
            }
        }
    }

    private int countArguments(String argsStr) {
        if (argsStr == null || argsStr.trim().isEmpty()) return 0;

        int count = 1;
        int depth = 0;
        boolean inQuotes = false;
        char quoteChar = 0;

        for (int i = 0; i < argsStr.length(); i++) {
            char c = argsStr.charAt(i);

            if (inQuotes) {
                if (c == quoteChar) inQuotes = false;
                continue;
            }

            if (c == '"' || c == '\'') {
                inQuotes = true;
                quoteChar = c;
            } else if (c == '(' || c == '[') {
                depth++;
            } else if (c == ')' || c == ']') {
                depth--;
            } else if (c == ',' && depth == 0) {
                count++;
            }
        }

        return count;
    }

    private void detectJinjaOperationTypes(String expr, int line) {
        if (expr == null || expr.isEmpty()) return;


        java.util.regex.Pattern opPattern = java.util.regex.Pattern.compile(
                "([a-zA-Z_][a-zA-Z0-9_.]*|\"[^\"]*\"|'[^']*'|\\d+(?:\\.\\d+)?)"
                        + "\\s*(//|\\*\\*|<=|>=|[+\\-*/%<>])"
                        + "\\s*([a-zA-Z_][a-zA-Z0-9_.]*|\"[^\"]*\"|'[^']*'|\\d+(?:\\.\\d+)?)"
        );
        java.util.regex.Matcher matcher = opPattern.matcher(expr);

        while (matcher.find()) {
            String leftStr = matcher.group(1).trim();
            String operator = matcher.group(2).trim();
            String rightStr = matcher.group(3).trim();

            String leftType = inferJinjaOperandType(leftStr);
            String rightType = inferJinjaOperandType(rightStr);


            if ("unknown".equals(leftType) || "unknown".equals(rightType)) continue;


            if ("==".equals(operator) || "!=".equals(operator)) continue;


            if (isJinjaArithmeticOp(operator)
                    && ("NoneType".equals(leftType) || "NoneType".equals(rightType))) {
                continue;
            }

            OperationTypeInfo info = new OperationTypeInfo(
                    operator, leftType, rightType, leftStr, rightStr, line
            );
            info.setFileName(symbolTable.getCurrentFileName());
            info.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.addOperationTypeInfo(info);
        }
    }


    private String inferJinjaOperandType(String operand) {
        operand = operand.trim();


        if ((operand.startsWith("\"") && operand.endsWith("\""))
                || (operand.startsWith("'") && operand.endsWith("'"))) {
            return "string";
        }


        if (operand.matches("\\d+")) return "int";
        if (operand.matches("\\d+\\.\\d+")) return "float";


        String varName = operand.split("\\.")[0];
        if (JINJA_BUILTINS.contains(varName) || JINJA_KEYWORDS.contains(varName)) {
            return "unknown";
        }

        SymbolEntry entry = symbolTable.lookup(varName);
        if (entry != null && entry.getType() != null) {
            String type = entry.getType();
            if ("str".equals(type)) return "string";
            if ("none".equals(type)) return "NoneType";
            return type;
        }

        return "unknown";
    }

    private boolean isJinjaArithmeticOp(String operator) {
        return "+".equals(operator) || "-".equals(operator) || "*".equals(operator)
                || "/".equals(operator) || "//".equals(operator)
                || "%".equals(operator) || "**".equals(operator);
    }

}

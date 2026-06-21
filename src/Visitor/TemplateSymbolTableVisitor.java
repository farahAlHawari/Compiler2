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

/**
 * Visitor that walks the Template AST (HTML/CSS/Jinja) and populates
 * the unified Symbol Table.
 *
 * [تعديلات مهمة]:
 * - [تعديل 4] إصلاح سكوبات if/elif/else - لازم يكونوا بنفس المستوى مش متداخلين
 * - [تعديل 5] تحسين extractVariablesFromExpression باستخدام regex
 * - [تعديل 6] إضافة JINJA_KEYWORDS كـ static final بدل إنشاء Set كل مرة
 *
 * Scope handling:
 * - Page level = global scope
 * - Style block = style scope (level 1)
 * - Each CSS rule = sub-scope named by selector, e.g. style/body (level 2)
 * - Jinja block = jinja_block scope
 * - Jinja for loop = jinja_block scope with iterator variable
 * - if/elif/else = كلهم بنفس مستوى السكوب (مش متداخلين)
 */
public class TemplateSymbolTableVisitor {

    private SymbolTable symbolTable;
    private java.util.List<String> errors;

    // Jinja built-in variables that should NOT be stored as user variables
    private static final java.util.Set<String> JINJA_BUILTINS = java.util.Set.of(
            "loop", "request", "session", "g", "config", "self",
            "range", "dict", "lipsum", "cycler", "joiner", "namespace",
            "true", "false", "none", "True", "False", "None"
    );

    // ==================== [تعديل 6] JINJA_KEYWORDS كـ static final ====================
    // قبل: كنا نعمل Set.of(...) داخل extractVariablesFromExpression كل مرة
    // الحين: مرة واحدة فقط
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
    // ==================== نهاية التعديل 6 ====================

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

    // ==================== Main Dispatch Method ====================

    public void visit(ASTNode node) {
        if (node == null) return;

        if (node instanceof PageNode) {
            visitPage((PageNode) node);
        } else if (node instanceof HtmlElementNode) {
            // ✅ تعديل: لا نخزن الـ tag نفسه، بس نزور أطفاله
            visitHtmlElementChildren((HtmlElementNode) node);
        } else if (node instanceof HtmlAttributeNode) {
            // ✅ تعديل: بس لو في Jinja expression داخل الـ value
            visitHtmlAttribute((HtmlAttributeNode) node);
        } else if (node instanceof TextNode || node instanceof DoctypeNode) {
            // No symbol table operations
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
            // CSS values stored as part of Declaration, no separate entry
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

    // ✅ بدل visitHtmlElement القديمة
// لا نخزن الـ tag في Symbol Table، بس نزور أطفاله للوصول لـ Jinja داخلهم
    private void visitHtmlElementChildren(HtmlElementNode node) {
        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    private void visitHtmlAttribute(HtmlAttributeNode node) {
        String attrName = node.getAttrName();
        String attrValue = node.getAttrValue();

        // تخطى style دائماً
        if (attrName.equals("style")) return;

        // تخطى لو ما في Jinja expression داخل الـ value
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

    // ✅ الصح:
    private void visitStyleRule(StyleRuleNode node) {
        String selectorText = extractSelectorText(node);
        int newLevel = symbolTable.currentScopeLevel() + 1;

        // 1) زوري السيلكتورات قبل enterScope (بلفل الأب = 1)
        for (ASTNode child : node.children) {
            if (child instanceof SelectorNode || child instanceof CombinedSelectorNode) {
                visit(child);
            }
        }

        // 2) ادخلي السكوب
        symbolTable.enterScope("style",  selectorText);

        // 3) زوري الدكليريشنز جوا السكوب (بلفل الابن = 2)
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
            // ✅ [تعديل 4] elif ما عاد بيعمل سكوب لحاله
            // بيتعامل معه داخل visitJinjaIf
            visitJinjaElif(node);
        } else if (name.startsWith("JinjaElse")) {
            // ✅ [تعديل 4] else ما عاد بيعمل سكوب لحاله
            // بيتعامل معه داخل visitJinjaIf
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
            // {% for product in products %} - بشكل Simple (بدون أطفال)
            handleSimpleFor(content, node.getLine());

        } else if (content.startsWith("if ")) {
            // {% if show_banner %} - بشكل Simple
            handleSimpleIf(content, node.getLine());

        } else if (content.startsWith("elif ")) {
            // {% elif item == "carrot" %} - بشكل Simple
            String condition = content.substring(5).trim();
            extractVariablesFromExpression(condition, node.getLine());

        } else if (content.startsWith("else")) {
            // {% else %} - ما نساوي شي (الـ scope بيتبدل بالـ processIfBranchChildren)

        } else if (content.startsWith("set ")) {
            // {% set welcome_msg = "Welcome" %} - بشكل Simple
            handleSimpleSet(content, node.getLine());

        } else if (content.startsWith("macro ")) {
            // {% macro greeting(name) %} - بشكل Simple
            handleSimpleMacro(content, node.getLine());

        } else if (content.startsWith("include ")) {
            // {% include "header.html" %} - بشكل Simple
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
            // {% with x = 5 %} - بشكل Simple
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
            // endif, endfor, endblock, endmacro - نتخطاها
            // أو unknown - بس نزور أطفال
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

        // ✅ التعديل: lookupCurrentScope بدل lookup
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
        // content = "if show_banner" أو "if is_logged_in and is_admin"
        String condition = content.substring(3).trim();
        extractVariablesFromExpression(condition, line);
    }

//    private void handleSimpleSet(String content, int line) {
//        // content = "set welcome_msg "Welcome"" أو "set full_name user.name"
//        String setExpr = content.substring(4).trim();
//        if (setExpr.contains("=")) {
//            String varName = setExpr.substring(0, setExpr.indexOf("=")).trim();
//            String valueExpr = setExpr.substring(setExpr.indexOf("=") + 1).trim();
//
//            String scopeType = symbolTable.currentScope().getScopeType();
//            int scopeLevel = symbolTable.currentScopeLevel();
//
//            SymbolEntry entry = new SymbolEntry(
//                    varName, "variable", "jinja_set_var", scopeType,
//                    scopeLevel, line, "template"
//            );
//            entry.setValue(valueExpr);
//            entry.setFileName(symbolTable.getCurrentFileName());
//            entry.setFilePath(symbolTable.getCurrentFilePath());
//            symbolTable.insert(entry);
//
//            extractVariablesFromExpression(valueExpr, line);
//        } else {
//            // {% set x %} بدون = (نادر)
//            String varName = setExpr.trim();
//            String scopeType = symbolTable.currentScope().getScopeType();
//            int scopeLevel = symbolTable.currentScopeLevel();
//            SymbolEntry entry = new SymbolEntry(
//                    varName, "variable", "jinja_set_var", scopeType,
//                    scopeLevel, line, "template"
//            );
//            entry.setFileName(symbolTable.getCurrentFileName());
//            entry.setFilePath(symbolTable.getCurrentFilePath());
//            symbolTable.insert(entry);
//        }
//    }

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

        // ✅ تعديل 5: خزّن نوع القيمة المستنتج من valueExpr
        //   بدل ما يكون نوع المتغير "jinja_set_var" فقط، نحطي النوع الحقيقي
        //   في حقل منفصل عشان نقدر نفحصه في TypeMismatchChecker
        String inferredType = inferTypeFromJinjaValue(valueExpr);
        entry.setDeclaredType(inferredType);   // ← نستخدم declaredType لتخزين النوع المستنتج

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

    /**
     * ✅ تعديل 5: استنتاج نوع القيمة في Jinja {% set %}
     * يعطي النوع التقريبي للقيمة:
     *   "hello"  → "string"
     *   5        → "int"
     *   5.0      → "float"
     *   true     → "bool"
     *   [1,2,3]  → "list"
     *   {...}    → "dict"
     *   (1,2,3)  → "tuple"
     *   متغير آخر  → يرجع نوع المتغير الآخر (لو معروف)
     *   تعبير    → "unknown"
     */
//    private String inferTypeFromJinjaValue(String valueExpr) {
//        if (valueExpr == null || valueExpr.trim().isEmpty()) return "unknown";
//
//        valueExpr = valueExpr.trim();
//
//        // String literal
//        if ((valueExpr.startsWith("\"") && valueExpr.endsWith("\""))
//                || (valueExpr.startsWith("'") && valueExpr.endsWith("'"))) {
//            return "string";
//        }
//
//        // List
//        if (valueExpr.startsWith("[") && valueExpr.endsWith("]")) {
//            return "list";
//        }
//
//        // Dict
//        if (valueExpr.startsWith("{") && valueExpr.endsWith("}")) {
//            return "dict";
//        }
//
//        // Tuple
//        if (valueExpr.startsWith("(") && valueExpr.endsWith(")")) {
//            return "tuple";
//        }
//
//        // Boolean
//        if ("true".equalsIgnoreCase(valueExpr) || "false".equalsIgnoreCase(valueExpr)) {
//            return "bool";
//        }
//
//        // None
//        if ("none".equalsIgnoreCase(valueExpr) || "null".equalsIgnoreCase(valueExpr)) {
//            return "NoneType";
//        }
//
//        // Integer
//        if (valueExpr.matches("-?\\d+")) {
//            return "int";
//        }
//
//        // Float
//        if (valueExpr.matches("-?\\d+\\.\\d+")) {
//            return "float";
//        }
//
//        // متغير آخر — حاول تجيب نوعه من SymbolTable
//        SymbolEntry refEntry = symbolTable.lookup(valueExpr);
//        if (refEntry != null) {
//            String t = refEntry.getDeclaredType();
//            if (t != null && !t.isEmpty()) return t;
//            // لو ما عندوش declaredType، استخدم type العادي
//            String regularType = refEntry.getType();
//            if (regularType != null && !"jinja_set_var".equals(regularType)) {
//                return regularType;
//            }
//        }
//
//        return "unknown";
//    }
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
        // content = "macro greeting(name)" أو "macro render_card(title, content, color)"
        String rest = content.substring(6).trim();
        int parenStart = rest.indexOf("(");
        int parenEnd = rest.lastIndexOf(")");

        if (parenStart == -1) return;

        String macroName = rest.substring(0, parenStart).trim();
        String paramsStr = (parenEnd > parenStart) ?
                rest.substring(parenStart + 1, parenEnd).trim() : "";

        // ✅ تخزين تعريف الماكرو بالسكوب الحالي
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();
        SymbolEntry entry = new SymbolEntry(
                macroName, "function", "jinja_macro", scopeType,
                scopeLevel, line, "template"
        );
        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insert(entry);

        // ✅ فتح سكوب الماكرو وتخزين الباراميترات
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

        // Simple macro = ما عندو أطفال جوا العقدة
        // بس ممكن يكون في content كـ siblings
        // ما نقفل السكوب هون لأنه المحتوى جاي كـ siblings
        // الحل: نقفل السكوب لما نوصل JinjaSimple endmacro
        // ⚠️ مؤقتاً نقفل فوراً (لأنه ما فينا نتحكم بالـ siblings)
        symbolTable.exitScope();
    }

    // ==================== [تعديل 4] إصلاح سكوبات if/elif/else ====================
    /**
     * التعديل الأساسي: if/elif/else لازم يكونوا بنفس مستوى السكوب
     *
     * قبل التعديل (خطأ):
     *   global (level 0)
     *     └── if (level 1)
     *           └── else (level 2)     ← خطأ! متداخل داخل if
     *
     * بعد التعديل (صحيح):
     *   global (level 0)
     *     ├── if (level 1)
     *     └── else (level 1)           ← صحيح! بنفس مستوى if
     */
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

    /**
     * معالجة أطفال فرع if/elif بشكل متكرر.
     * السبب: الـ grammar بيعمل elif/else كـ containers متداخلة،
     * فـ JinjaElseNode ممكن يكون داخل JinjaElifNode مش child مباشر لـ IfNode.
     *
     * هالدالة بتلف على الأطفال ولما تلاقي elif/else بتعمل:
     * 1. ترفع متغيرات الفرع الحالي للـ parent (promote)
     * 2. تقفل سكوب الفرع الحالي
     * 3. تفتح سكوب جديد للفرع الجديد بنفس المستوى
     */
    private void processIfBranchChildren(ASTNode branchNode) {
        for (ASTNode child : branchNode.children) {
            if (child instanceof JinjaNode) {
                String childName = child.nodeName;

                if (childName.startsWith("JinjaElse")) {
                    Scope exited = symbolTable.exitScope();
                    promoteVariablesToParent(exited);
                    symbolTable.enterScope("jinja_block", "else");
                    // المحتوى جاي كـ siblings → اللوب رح يزورهم ضمن سكوب else

                } else if (childName.startsWith("JinjaElif")) {
                    Scope exited = symbolTable.exitScope();
                    promoteVariablesToParent(exited);
                    String elifCond = extractJinjaCondition(childName);
                    extractVariablesFromExpression(elifCond, child.getLine());
                    symbolTable.enterScope("jinja_block", "elif");
                    // المحتوى جاي كـ siblings → اللوب رح يزورهم ضمن سكوب elif

                } else if (childName.startsWith("JinjaEndIf")
                        || childName.startsWith("JinjaEndFor")
                        || childName.startsWith("JinjaEndBlock")
                        || childName.startsWith("JinjaEndMacro")) {
                    continue;

                } else {
                    visit(child);
                }
            } else {
                visit(child);
            }
        }
    }
//    private void visitJinjaIf(JinjaNode node) {
//        String condition = extractJinjaCondition(node.nodeName);
//        extractVariablesFromExpression(condition, node.getLine());
//
//        // نحفظ مستوى السكوب يلي راح نستخدمه لكل الفروع
//        int ifScopeLevel = symbolTable.currentScopeLevel() + 1;
//
//        // ===== 1) فرع if =====
//        symbolTable.enterScope("jinja_block", ifScopeLevel, "if");
//
//        // بنزور أبناء الـ if، بس لازم نميز بين:
//        // - محتوى عادي (نزوره ضمن سكوب if)
//        // - JinjaElse / JinjaElif (نغلق سكوب if ونفتح سكوب جديد بنفس المستوى)
//        // - JinjaEndIf / JinjaEndFor / JinjaEndBlock (نتجاوزها)
//        for (ASTNode child : node.children) {
//            if (child instanceof JinjaNode) {
//                String childName = child.nodeName;
//
//                if (childName.startsWith("JinjaElse")) {
//                    // نقفل سكوب if ونفتح سكوب else بنفس المستوى
//                    symbolTable.exitScope();
//                    symbolTable.enterScope("jinja_block", ifScopeLevel, "else");
//
//                    // نزور محتوى else (أبناء عقدة else)
//                    for (ASTNode elseChild : child.children) {
//                        visit(elseChild);
//                    }
//                    // ما نعمل exit هنا - بننتظر لآخر الدالة
//
//                } else if (childName.startsWith("JinjaElif")) {
//                    // نقفل السكوب الحالي ونفتح سكوب elif بنفس المستوى
//                    symbolTable.exitScope();
//
//                    String elifCond = extractJinjaCondition(childName);
//                    extractVariablesFromExpression(elifCond, child.getLine());
//
//                    symbolTable.enterScope("jinja_block", ifScopeLevel, "elif");
//
//                    // نزور محتوى elif (أبناء عقدة elif)
//                    for (ASTNode elifChild : child.children) {
//                        visit(elifChild);
//                    }
//                    // ما نعمل exit هنا - بننتظر لآخر الدالة
//
//                } else if (childName.startsWith("JinjaEndIf")
//                        || childName.startsWith("JinjaEndFor")
//                        || childName.startsWith("JinjaEndBlock")
//                        || childName.startsWith("JinjaEndMacro")) {
//                    // علامات الإغلاق - نتخطاها فقط
//                    continue;
//                } else {
//                    // أنواع Jinja تانية (مثل JinjaFor داخل if)
//                    visit(child);
//                }
//            } else {
//                // محتوى عادي (HTML, text, etc.) - نزوره ضمن السكوب الحالي
//                visit(child);
//            }
//        }
//
//        // نقفل آخر سكوب مفتوح (if أو else أو elif)
//        symbolTable.exitScope();
//    }

    /**
     * [تعديل 4] معالجة elif لو ظهرت بمعزل عن if
     * (بشكل عام ما لازم يصير هاد، بس لأمان)
     */
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

    /**
     * [تعديل 4] معالجة else لو ظهرت بمعزل عن if
     * (بشكل عام ما لازم يصير هاد، بس لأمان)
     */
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
    // ==================== نهاية التعديل 4 ====================

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

        // ✅ التعديل: lookupCurrentScope بدل lookup
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

        // بس نستخرج المتغيرات - ما نخزن function calls
        extractVariablesFromExpression(expr, node.getLine());
    }

    private void visitJinjaExtends(JinjaNode node) {
        String templateName = extractQuotedString(node.nodeName); // "base.html"

        SymbolEntry entry = new SymbolEntry(
                templateName,           // ✅ "base.html" مش "extends"
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

//    private void visitJinjaSet(JinjaNode node) {
//        String setText = node.nodeName.replace("JinjaSet", "").trim();
//        if (setText.contains("=")) {
//            String varName = setText.substring(0, setText.indexOf("=")).trim();
//            String valueExpr = setText.substring(setText.indexOf("=") + 1).trim();
//
//            String scopeType = symbolTable.currentScope().getScopeType();
//            int scopeLevel = symbolTable.currentScopeLevel();
//
//            SymbolEntry entry = new SymbolEntry(
//                    varName, "variable", "jinja_set_var", scopeType,
//                    scopeLevel, node.getLine(), "template"
//            );
//            entry.setValue(valueExpr);
//            entry.setFileName(symbolTable.getCurrentFileName());
//            entry.setFilePath(symbolTable.getCurrentFilePath());
//            symbolTable.insert(entry);
//
//            // ✅ أضيفي: استخراج المتغيرات من الـ value
//            extractVariablesFromExpression(valueExpr, node.getLine());
//        }
//    }
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

        // ✅ تعديل 5: خزّن النوع المستنتج من القيمة
        String inferredType = inferTypeFromJinjaValue(valueExpr);
        entry.setDeclaredType(inferredType);

        entry.setFileName(symbolTable.getCurrentFileName());
        entry.setFilePath(symbolTable.getCurrentFilePath());
        symbolTable.insert(entry);

        extractVariablesFromExpression(valueExpr, node.getLine());
    }
}
//    private void visitJinjaWith(JinjaNode node) {
//        // نفس منطق visitJinjaSet بالأساس
//        // بس مع فتح سكوب جديد
//        int newLevel = symbolTable.currentScopeLevel() + 1;
//        symbolTable.enterScope("jinja_block",  "with");
//
//        String withText = node.nodeName.replace("JinjaWith", "").trim();
//        if (withText.contains("=")) {
//            String varName = withText.substring(0, withText.indexOf("=")).trim();
//            String valueExpr = withText.substring(withText.indexOf("=") + 1).trim();
//            SymbolEntry entry = new SymbolEntry(
//                    varName, "variable", "jinja_set_var", "jinja_block",
//                    newLevel, node.getLine(), "template"
//            );
//            entry.setValue(valueExpr);
//            entry.setFileName(symbolTable.getCurrentFileName());
//            entry.setFilePath(symbolTable.getCurrentFilePath());
//            symbolTable.insert(entry);
//        }
//
//        for (ASTNode child : node.children) { visit(child); }
//        symbolTable.exitScope();
//    }
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

        // ✅ تعديل 5
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
                    symbolTable.currentScopeLevel(),  // ← بعد enterScope
                    node.getLine(), "template"
            );
            paramEntry.setFileName(symbolTable.getCurrentFileName());
            paramEntry.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.insert(paramEntry);
        }

        for (ASTNode child : node.children) { visit(child); }
        symbolTable.exitScope();
    }


    // ==================== Jinja Variable Leakage ====================

    /**
     * Jinja variable leakage: المتغيرات المعرّفة بأي فرع (if/elif/else)
     * بتكون مرئية بعد endif بالسكوب الأب.
     * هاد سلوك صحيح بالـ Jinja - مش خطأ.
     */
    private void promoteVariablesToParent(Scope exitedScope) {
        if (exitedScope == null || symbolTable.getScopeStack().isEmpty()) return;

        // الأب الفعلي = أعلى شي بالستاك بعد ما طلعنا
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
    // ==================== Helper Methods ====================

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

    // ==================== [تعديل 5] تحسين extractVariablesFromExpression ====================
    /**
     * استخراج المتغيرات من تعابير Jinja باستخدام regex أدق.
     *
     * قبل: كنا نستخدم split بالنص وممكن يفشل بحالات معقدة
     * بعد: بنستخدم Pattern/Matcher لاستخراج أسماء المتغيرات بدقة
     *
     * قواعد:
     * - p.name  → بنخزن "p" بس (name هي attribute مش variable)
     * - loop.index → لا نخزن loop (builtin)
     * - products  → بنخزن "products" كـ jinja_var
     * - 123, "hello" → نتخطى (أرقام ونصوص)
     */
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

            // ✅ التعديل: lookupCurrentScope بدل lookup
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

        // ===== Division By Zero case 4 (Bridge): {{ count / price }} or {{ x % y }} =====
        // Jinja expressions are plain text here, so we scan for "<left> / <right>"
        // or "<left> % <right>" directly. We only care about the divisor (right
        // side): if it's a literal 0, or a Flask-passed variable whose value is 0,
        // DivisionByZeroChecker will flag it — exactly like Python's runtime
        // ZeroDivisionError would when Jinja evaluates the expression.
        java.util.regex.Pattern divPattern = java.util.regex.Pattern.compile(
                "([a-zA-Z_][a-zA-Z0-9_.]*|\\d+(?:\\.\\d+)?)\\s*([/%])\\s*([a-zA-Z_][a-zA-Z0-9_.]*|\\d+(?:\\.\\d+)?)"
        );
        java.util.regex.Matcher divMatcher = divPattern.matcher(expr);
        while (divMatcher.find()) {
            String divisorToken = divMatcher.group(3).trim();
            String operatorToken = divMatcher.group(2).trim();

            boolean divisorIsNumericLiteral = divisorToken.matches("\\d+(?:\\.\\d+)?");
            String filterNameForDivision = "__division__" + operatorToken; // marks usage as a division/modulo, not a real filter

            if (divisorIsNumericLiteral) {
                // Direct literal divisor inside a Jinja expression, e.g. {{ x / 0 }}
                symbol_table.JinjaFilterUsage divUsage = new symbol_table.JinjaFilterUsage(
                        divisorToken, filterNameForDivision, "division_literal", line
                );
                divUsage.setFileName(symbolTable.getCurrentFileName());
                divUsage.setFilePath(symbolTable.getCurrentFilePath());
                symbolTable.addJinjaFilterUsage(divUsage);
            } else if (!JINJA_BUILTINS.contains(divisorToken)) {
                // لاحظي: شلنا فحص JINJA_KEYWORDS هون فقط (مش من باقي الكود)
                // لأنه كلمات متل count/length/string هي أسماء filters بس برضو
                // ممكن تكون أسماء متغيرات عادية بسياق رياضي زي {{ price / count }}
                symbol_table.JinjaFilterUsage divUsage = new symbol_table.JinjaFilterUsage(
                        divisorToken, filterNameForDivision, "division_variable", line
                );
                divUsage.setFileName(symbolTable.getCurrentFileName());
                divUsage.setFilePath(symbolTable.getCurrentFilePath());
                symbolTable.addJinjaFilterUsage(divUsage);
            }
        }

        // NEW: Track Jinja function/macro calls for Invalid Func Call and Wrong Args Count checking
        // Pattern: function_name(arg1, arg2)
        java.util.regex.Pattern funcCallPattern = java.util.regex.Pattern.compile(
                "\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(([^)]*)\\)"
        );
        java.util.regex.Matcher funcMatcher = funcCallPattern.matcher(expr);
        while (funcMatcher.find()) {
            String calledFuncName = funcMatcher.group(1);
            String argsStr = funcMatcher.group(2).trim();

            if (JINJA_BUILTINS.contains(calledFuncName)) continue;
            if (JINJA_KEYWORDS.contains(calledFuncName)) continue;

            // Count arguments
            int macroArgCount = 0;
            if (!argsStr.isEmpty()) {
                // Split by comma, but ignore commas inside quotes
                macroArgCount = countArguments(argsStr);
            }

            FunctionCallInfo macroCall = new FunctionCallInfo(
                    calledFuncName, macroArgCount, line, "template", false, false
            );
            macroCall.setFileName(symbolTable.getCurrentFileName());
            macroCall.setFilePath(symbolTable.getCurrentFilePath());
            symbolTable.addFunctionCallInfo(macroCall);
        }
        // NEW: Track Jinja filters for Invalid Function Call checking
        // Jinja filters are after the pipe: {{ var|filter1|filter2 }}
        if (expr.contains("|")) {
            String[] parts = expr.split("\\|");
            for (int i = 1; i < parts.length; i++) {  // Skip first part (the variable)
                String filterName = parts[i].trim();
                // Remove any arguments: "default('N/A')" → "default"
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
                    // Count arguments if present: filter(arg1, arg2) → 2 args
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
//    private void extractVariablesFromExpression(String expr, int line) {
//        if (expr == null || expr.isEmpty()) return;
//
//        expr = expr.replaceAll("\"[^\"]*\"", "").replaceAll("'[^']*'", "");
//
//        java.util.regex.Pattern varPattern = java.util.regex.Pattern.compile(
//                "\\b([a-zA-Z_][a-zA-Z0-9_]*)(\\.[a-zA-Z_][a-zA-Z0-9_]*)*\\b"
//        );
//        java.util.regex.Matcher matcher = varPattern.matcher(expr);
//
//        while (matcher.find()) {
//            String token = matcher.group(1);
//
//            if (token.isEmpty()) continue;
//            if (JINJA_BUILTINS.contains(token)) continue;
//            if (JINJA_KEYWORDS.contains(token)) continue;
//            if (token.matches("\\d+.*")) continue;
//
//            // ✅ التعديل: lookupCurrentScope بدل lookup
//            if (symbolTable.lookupCurrentScope(token) == null) {
//                String scopeType = symbolTable.currentScope().getScopeType();
//                int scopeLevel = symbolTable.currentScopeLevel();
//
//                SymbolEntry entry = new SymbolEntry(
//                        token, "jinja_var", "context_var", scopeType,
//                        scopeLevel, line, "template"
//                );
//                entry.setFileName(symbolTable.getCurrentFileName());
//                entry.setFilePath(symbolTable.getCurrentFilePath());
//                symbolTable.insert(entry);
//            }
//        }
//        // NEW: Track Jinja function/macro calls for Invalid Func Call and Wrong Args Count checking
//        // Pattern: function_name(arg1, arg2)
//        java.util.regex.Pattern funcCallPattern = java.util.regex.Pattern.compile(
//                "\\b([a-zA-Z_][a-zA-Z0-9_]*)\\s*\\(([^)]*)\\)"
//        );
//        java.util.regex.Matcher funcMatcher = funcCallPattern.matcher(expr);
//        while (funcMatcher.find()) {
//            String calledFuncName = funcMatcher.group(1);
//            String argsStr = funcMatcher.group(2).trim();
//
//            if (JINJA_BUILTINS.contains(calledFuncName)) continue;
//            if (JINJA_KEYWORDS.contains(calledFuncName)) continue;
//
//            // Count arguments
//            int macroArgCount = 0;
//            if (!argsStr.isEmpty()) {
//                // Split by comma, but ignore commas inside quotes
//                macroArgCount = countArguments(argsStr);
//            }
//
//            FunctionCallInfo macroCall = new FunctionCallInfo(
//                    calledFuncName, macroArgCount, line, "template", false, false
//            );
//            macroCall.setFileName(symbolTable.getCurrentFileName());
//            macroCall.setFilePath(symbolTable.getCurrentFilePath());
//            symbolTable.addFunctionCallInfo(macroCall);
//        }
//        // NEW: Track Jinja filters for Invalid Function Call checking
//        // Jinja filters are after the pipe: {{ var|filter1|filter2 }}
//        if (expr.contains("|")) {
//            String[] parts = expr.split("\\|");
//            for (int i = 1; i < parts.length; i++) {  // Skip first part (the variable)
//                String filterName = parts[i].trim();
//                // Remove any arguments: "default('N/A')" → "default"
//                if (filterName.contains("(")) {
//                    filterName = filterName.substring(0, filterName.indexOf("(")).trim();
//                }
//                String varName = parts[0].trim();
//                if (!varName.isEmpty() && !JINJA_KEYWORDS.contains(varName)) {
//
//
//                            symbol_table.JinjaFilterUsage usage =
//                                    new symbol_table.JinjaFilterUsage(varName, filterName, "filter", line);
//                    usage.setFileName(symbolTable.getCurrentFileName());
//                    usage.setFilePath(symbolTable.getCurrentFilePath());
//                    symbolTable.addJinjaFilterUsage(usage);
//
//                }
//                if (!filterName.isEmpty() && !JINJA_BUILTIN_FILTERS.contains(filterName)
//                        && !JINJA_KEYWORDS.contains(filterName)) {
//                    // Count arguments if present: filter(arg1, arg2) → 2 args
//                    int filterArgCount = 0;
//                    if (parts[i].contains("(") && parts[i].contains(")")) {
//                        String argsStr = parts[i].substring(
//                                parts[i].indexOf("(") + 1,
//                                parts[i].lastIndexOf(")")
//                        ).trim();
//                        if (!argsStr.isEmpty()) {
//                            filterArgCount = argsStr.split(",").length;
//                        }
//                    }
//                    FunctionCallInfo filterCall = new FunctionCallInfo(
//                            filterName, filterArgCount, line, "template", false, true
//                    );
//                    filterCall.setFileName(symbolTable.getCurrentFileName());
//                    filterCall.setFilePath(symbolTable.getCurrentFilePath());
//                    symbolTable.addFunctionCallInfo(filterCall);
//                }
//            }
//        }
//    }
    /**
     * Count the number of arguments in a function call argument string.
     * Handles nested parentheses and quoted strings.
     */
    private int countArguments(String argsStr) {
        if (argsStr == null || argsStr.trim().isEmpty()) return 0;

        int count = 1; // At least 1 arg if string is not empty
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

}

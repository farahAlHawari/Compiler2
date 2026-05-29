package Visitor;

import AST.Core.*;
import AST.Css.Rules.*;
import AST.Css.Selectors.*;
import AST.Css.Values.*;
import AST.Html.*;
import AST.Jinja.*;
import symbol_table.SymbolTable;
import symbol_table.SymbolEntry;

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

        // في Jinja داخل الـ value، نستخرج المتغيرات منه
        extractVariablesFromExpression(attrValue, node.getLine());
    }

    // ==================== CSS Style Block ====================

    private void visitStyleBlock(StyleBlockNode node) {
        int newLevel = symbolTable.currentScopeLevel() + 1;
        symbolTable.enterScope("style", newLevel, "style");

        for (ASTNode child : node.children) {
            visit(child);
        }

        symbolTable.exitScope();
    }

    // ==================== CSS Rule ====================

    private void visitStyleRule(StyleRuleNode node) {
        String selectorText = extractSelectorText(node);

        int newLevel = symbolTable.currentScopeLevel() + 1;
        symbolTable.enterScope("style", newLevel, selectorText);

        for (ASTNode child : node.children) {
            visit(child);
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
        symbolTable.insertCssSelector(entry);
    }

    private void visitClassSelector(ClassSelectorNode node) {
        String cleanName = "." + node.nodeName.replace("ClassSelector: ", "");
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                cleanName, "css_selector", "class_selector", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        symbolTable.insertCssSelector(entry);
    }

    private void visitIdSelector(IdSelectorNode node) {
        String cleanName = "#" + node.nodeName.replace("IDSelector: ", "");
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                cleanName, "css_selector", "id_selector", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        symbolTable.insertCssSelector(entry);
    }

    private void visitUniversalSelector(UniversalSelectorNode node) {
        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                "*", "css_selector", "universal_selector", scopeType,
                scopeLevel, node.getLine(), "template"
        );
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

        symbolTable.insertCssProperty(entry);

        for (ASTNode child : node.children) {
            visit(child);
        }
    }

    // ==================== Jinja Nodes ====================

    private void visitJinjaNode(JinjaNode node) {
        String name = node.nodeName;

        if (name.startsWith("JinjaIf") && !name.startsWith("JinjaIfNode")) {
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
        } else if (name.startsWith("JinjaMacro")) {
            visitJinjaMacro(node);
        } else {
            for (ASTNode child : node.children) {
                visit(child);
            }
        }
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

        // نحفظ مستوى السكوب يلي راح نستخدمه لكل الفروع
        int ifScopeLevel = symbolTable.currentScopeLevel() + 1;

        // ===== 1) فرع if =====
        symbolTable.enterScope("jinja_block", ifScopeLevel, "if");

        // بنزور أبناء الـ if، بس لازم نميز بين:
        // - محتوى عادي (نزوره ضمن سكوب if)
        // - JinjaElse / JinjaElif (نغلق سكوب if ونفتح سكوب جديد بنفس المستوى)
        // - JinjaEndIf / JinjaEndFor / JinjaEndBlock (نتجاوزها)
        for (ASTNode child : node.children) {
            if (child instanceof JinjaNode) {
                String childName = child.nodeName;

                if (childName.startsWith("JinjaElse")) {
                    // نقفل سكوب if ونفتح سكوب else بنفس المستوى
                    symbolTable.exitScope();
                    symbolTable.enterScope("jinja_block", ifScopeLevel, "else");

                    // نزور محتوى else (أبناء عقدة else)
                    for (ASTNode elseChild : child.children) {
                        visit(elseChild);
                    }
                    // ما نعمل exit هنا - بننتظر لآخر الدالة

                } else if (childName.startsWith("JinjaElif")) {
                    // نقفل السكوب الحالي ونفتح سكوب elif بنفس المستوى
                    symbolTable.exitScope();

                    String elifCond = extractJinjaCondition(childName);
                    extractVariablesFromExpression(elifCond, child.getLine());

                    symbolTable.enterScope("jinja_block", ifScopeLevel, "elif");

                    // نزور محتوى elif (أبناء عقدة elif)
                    for (ASTNode elifChild : child.children) {
                        visit(elifChild);
                    }
                    // ما نعمل exit هنا - بننتظر لآخر الدالة

                } else if (childName.startsWith("JinjaEndIf")
                        || childName.startsWith("JinjaEndFor")
                        || childName.startsWith("JinjaEndBlock")
                        || childName.startsWith("JinjaEndMacro")) {
                    // علامات الإغلاق - نتخطاها فقط
                    continue;
                } else {
                    // أنواع Jinja تانية (مثل JinjaFor داخل if)
                    visit(child);
                }
            } else {
                // محتوى عادي (HTML, text, etc.) - نزوره ضمن السكوب الحالي
                visit(child);
            }
        }

        // نقفل آخر سكوب مفتوح (if أو else أو elif)
        symbolTable.exitScope();
    }

    /**
     * [تعديل 4] معالجة elif لو ظهرت بمعزل عن if
     * (بشكل عام ما لازم يصير هاد، بس لأمان)
     */
    private void visitJinjaElif(JinjaNode node) {
        String condition = extractJinjaCondition(node.nodeName);
        extractVariablesFromExpression(condition, node.getLine());

        int newLevel = symbolTable.currentScopeLevel() + 1;
        symbolTable.enterScope("jinja_block", newLevel, "elif");

        for (ASTNode child : node.children) {
            visit(child);
        }

        symbolTable.exitScope();
    }

    /**
     * [تعديل 4] معالجة else لو ظهرت بمعزل عن if
     * (بشكل عام ما لازم يصير هاد، بس لأمان)
     */
    private void visitJinjaElse(JinjaNode node) {
        int newLevel = symbolTable.currentScopeLevel() + 1;
        symbolTable.enterScope("jinja_block", newLevel, "else");

        for (ASTNode child : node.children) {
            visit(child);
        }

        symbolTable.exitScope();
    }
    // ==================== نهاية التعديل 4 ====================

    private void visitJinjaFor(JinjaNode node) {
        String expr = extractJinjaCondition(node.nodeName);

        String iteratorName = "";
        String iterableName = "";

        if (expr.contains(" in ")) {
            String[] parts = expr.split(" in ", 2);
            iteratorName = parts[0].trim();
            iterableName = parts[1].trim();
        }

        int newLevel = symbolTable.currentScopeLevel() + 1;
        symbolTable.enterScope("jinja_block", newLevel, "for:" + iteratorName);

        // ✅ خزّن iterator variable (p) بالسكوب الجديد
        if (!iteratorName.isEmpty()) {
            SymbolEntry iterEntry = new SymbolEntry(
                    iteratorName, "variable", "jinja_iterator",
                    "jinja_block", newLevel, node.getLine(), "template"
            );
            symbolTable.insert(iterEntry);
        }

        // ✅ خزّن iterable (products) بس لو مش موجود بأي scope قبل
        // لو موجود بالـ Python global scope ما نكرره
        if (!iterableName.isEmpty() && symbolTable.lookup(iterableName) == null) {
            SymbolEntry iterableEntry = new SymbolEntry(
                    iterableName, "variable", "jinja_iterable",
                    "jinja_block", newLevel, node.getLine(), "template"
            );
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
                blockName, "jinja_block", "block", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        symbolTable.insert(entry);

        int newLevel = scopeLevel + 1;
        symbolTable.enterScope("jinja_block", newLevel, "block:" + blockName);

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
            symbolTable.insert(entry);
        }
    }

    private void visitJinjaMacro(JinjaNode node) {
        String macroName = ((JinjaMacroNode) node).getMacroName();

        String scopeType = symbolTable.currentScope().getScopeType();
        int scopeLevel = symbolTable.currentScopeLevel();

        SymbolEntry entry = new SymbolEntry(
                macroName, "function", "jinja_macro", scopeType,
                scopeLevel, node.getLine(), "template"
        );
        symbolTable.insert(entry);

        int newLevel = scopeLevel + 1;
        symbolTable.enterScope("jinja_block", newLevel, "macro:" + macroName);

        for (ASTNode child : node.children) {
            visit(child);
        }

        symbolTable.exitScope();
    }

    // ==================== Helper Methods ====================

    private String cleanSelectorName(String nodeName) {
        if (nodeName == null) return "unknown";

        nodeName = nodeName.replace("TypeSelector: ", "");
        nodeName = nodeName.replace("ClassSelector: ", ".");
        nodeName = nodeName.replace("IDSelector: ", "#");
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

        // نشيل الـ string literals أول (مثل "base.html", 'text')
        // عشان ما نستخرج كلمات من داخل quotes
        expr = expr.replaceAll("\"[^\"]*\"", "").replaceAll("'[^']*'", "");

        // نستخدم regex يلتقط أسماء مثل "p.name" كوحدة واحدة
        // [a-zA-Z_][a-zA-Z0-9_]* = اسم متغير
        // (\.[a-zA-Z_][a-zA-Z0-9_]*)* = attribute access اختياري
        java.util.regex.Pattern varPattern = java.util.regex.Pattern.compile(
                "\\b([a-zA-Z_][a-zA-Z0-9_]*)(\\.[a-zA-Z_][a-zA-Z0-9_]*)*\\b"
        );
        java.util.regex.Matcher matcher = varPattern.matcher(expr);

        while (matcher.find()) {
            // نأخذ بس الاسم الأول (الـ root variable)
            // p.name → p فقط
            // loop.index0 → loop (سيُتجاهل لأنه builtin)
            // products → products
            String token = matcher.group(1);

            if (token.isEmpty()) continue;

            // تجاهل Jinja builtins (loop, request, session...)
            if (JINJA_BUILTINS.contains(token)) continue;

            // تجاهل Jinja/Python keywords
            if (JINJA_KEYWORDS.contains(token)) continue;

            // تجاهل أرقام
            if (token.matches("\\d+.*")) continue;

            // نخزن بس لو مش موجود بأي scope
            if (symbolTable.lookup(token) == null) {
                String scopeType = symbolTable.currentScope().getScopeType();
                int scopeLevel = symbolTable.currentScopeLevel();

                SymbolEntry entry = new SymbolEntry(
                        token, "jinja_var", "context_var", scopeType,
                        scopeLevel, line, "template"
                );
                symbolTable.insert(entry);
            }
        }
    }

}

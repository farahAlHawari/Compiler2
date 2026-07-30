package generation;

import AST.Core.ASTNode;
import AST.Core.PageNode;
import AST.Html.DoctypeNode;
import AST.Html.HtmlAttributeNode;
import AST.Html.HtmlElementNode;
import AST.Html.TextNode;
import AST.Css.Rules.DeclarationListNode;
import AST.Css.Rules.DeclarationNode;
import AST.Css.Rules.StyleBlockNode;
import AST.Css.Rules.StyleRuleNode;
import AST.Css.Selectors.AttributeSelectorNode;
import AST.Css.Selectors.ClassSelectorNode;
import AST.Css.Selectors.CombinedSelectorNode;
import AST.Css.Selectors.IdSelectorNode;
import AST.Css.Selectors.PseudoClassNode;
import AST.Css.Selectors.PseudoElementNode;
import AST.Css.Selectors.TypeSelectorNode;
import AST.Css.Selectors.UniversalSelectorNode;
import AST.Css.Values.ColorCssValue;
import AST.Css.Values.FunctionCallCssValue;
import AST.Css.Values.IdentifierCssValue;
import AST.Css.Values.NumericCssValue;
import AST.Css.Values.StringCssValue;
import AST.Core.ASTNode;
import AST.Core.PageNode;
import AST.Jinja.*;


import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.Set;

public class JinjaRenderer {

    private GenerationContext context;
    private StaticRenderer staticRenderer;

    private static final Set<String> VOID_TAGS = Set.of(
            "area", "base", "br", "col", "embed", "hr", "img", "input",
            "link", "meta", "param", "source", "track", "wbr"
    );

    // ==================== Public API ====================

    public String render(PageNode page, GenerationContext ctx, StaticRenderer sr) {
        this.context = ctx;
        this.staticRenderer = sr;
        StringBuilder html = new StringBuilder();
        renderNode(page, html);
        return html.toString();
    }

    // ==================== Main Dispatch ====================

    private void renderNode(ASTNode node, StringBuilder html) {
        if (node == null) return;

        if (node instanceof PageNode) {
            for (ASTNode child : node.children) {
                renderNode(child, html);
            }
            return;
        }

        if (node instanceof HtmlElementNode) {
            renderHtmlElement((HtmlElementNode) node, html);
            return;
        }

        if (node instanceof JinjaExpressionNode) {
            renderExpression((JinjaExpressionNode) node, html);
            return;
        }

        if (node instanceof JinjaForNode) {
            renderFor((JinjaForNode) node, html);
            return;
        }

        if (node instanceof JinjaIfNode) {
            renderIf((JinjaIfNode) node, html);
            return;
        }

        if (node instanceof JinjaElseNode) {
            return;
        }

        if (node instanceof JinjaEndForNode
                || node instanceof JinjaEndIfNode
                || node instanceof JinjaEndBlockNode) {
            return;
        }

        if (node instanceof JinjaMacroNode
                || node instanceof JinjaIncludeNode
                || node instanceof JinjaImportNode
                || node instanceof JinjaSetNode) {
            context.addWarning("Unsupported Jinja tag: " + node.nodeName);
            return;
        }

        if (node instanceof JinjaExtendsNode
                || node instanceof JinjaBlockNode
                || node instanceof JinjaEndMacroNode) {
            context.addWarning("Unexpected " + node.nodeName + " after template merge");
            return;
        }

        staticRenderer.renderStaticNode(node, html);
    }

    // ==================== HTML Element with Jinja-Aware Child Traversal ====================

    private void renderHtmlElement(HtmlElementNode node, StringBuilder html) {
        String tagName = node.getTagName();
        html.append("<").append(tagName);

        for (ASTNode child : node.children) {
            if (child instanceof HtmlAttributeNode) {
                renderAttribute((HtmlAttributeNode) child, html);
            }
        }

        if (isVoidTag(tagName)) {
            html.append(">\n");          // ✅ تغيير 1
            return;
        }

        html.append(">\n");              // ✅ تغيير 2

        for (ASTNode child : node.children) {
            if (!(child instanceof HtmlAttributeNode)) {
                renderNode(child, html);
            }
        }

        html.append("</").append(tagName).append(">\n");   // ✅ تغيير 3
    }

    private void renderAttribute(HtmlAttributeNode node, StringBuilder html) {
        html.append(" ").append(node.getAttrName());
        String val = node.getAttrValue();
        if (val.startsWith("\"") && val.endsWith("\"") && val.length() > 1)
            val = val.substring(1, val.length() - 1);
        val = resolveExpressionsInString(val);

        val = convertRouteToStaticPage(node.getAttrName(), val);

        if (!val.isEmpty())
            html.append("=\"").append(val).append("\"");
    }

    private String resolveExpressionsInString(String str) {
        int start = str.indexOf("{{");
        while (start >= 0) {
            int end = str.indexOf("}}", start);
            if (end < 0) break;
            String expr = str.substring(start + 2, end).trim();
            Object value = context.resolveVariable(expr);
            if (value == null && expr.equals("loop.index0")) {
                value = context.getCurrentLoopIndex();
            }
            String replacement = (value != null) ? value.toString() : "";
            str = str.substring(0, start) + replacement + str.substring(end + 2);
            start = str.indexOf("{{", start + replacement.length());
        }
        return str;
    }

    private String convertRouteToStaticPage(String attrName, String value) {

        if (!attrName.equals("href") && !attrName.equals("action"))
            return value;

        switch (value) {

            case "/":
                return "index.html";

            case "/add":
                return "add.html";

            default:

                if (value.startsWith("/details/")) {
                    String id = value.substring("/details/".length());
                    return "product_details_" + id + ".html";
                }

                if (value.startsWith("/delete/")) {
                    return "#";
                }

                return value;
        }
    }

    private boolean isVoidTag(String tagName) {
        return VOID_TAGS.contains(tagName.toLowerCase());
    }

    // ==================== {{ expression }} ====================

    private void renderExpression(JinjaExpressionNode node, StringBuilder html) {
        String expr = extractExpression(node);
        Object value = context.resolveVariable(expr);

        if (value == null) {
            context.addWarning("Variable '" + expr + "' not found in GenerationContext");
            html.append("");
            return;
        }

        if (expr.equals("loop.index0")) {
            value = context.getCurrentLoopIndex();
        }

        html.append(value.toString());
    }

    // ==================== {% for var in collection %} ====================

    private void renderFor(JinjaForNode node, StringBuilder html) {
        String forExpr = node.getForExpr();
        String[] parts = forExpr.split(" in ");
        String varName = parts[0].trim();
        String collectionName = parts[1].trim();

        Object collectionObj = context.resolveVariable(collectionName);
        if (collectionObj == null) {
            context.addWarning("Collection '" + collectionName + "' not found for for loop");
            return;
        }

        List<Object> items = (List<Object>) collectionObj;
        if (items.isEmpty()) return;

        List<ASTNode> bodyNodes = getForBody(node);

        for (int i = 0; i < items.size(); i++) {
            context.pushScope(varName, items.get(i), i);
            for (ASTNode bodyNode : bodyNodes) {
                renderNode(bodyNode, html);
            }
            context.popScope();
        }

        context.addLog("[JinjaRenderer] Expanded for loop: " + forExpr + " (" + items.size() + " iterations)");
    }

    // ==================== {% if condition %} / {% else %} ====================

    private void renderIf(JinjaIfNode node, StringBuilder html) {
        String condition = node.getCondition();
        if (condition.startsWith("if ")) {
            condition = condition.substring(3).trim();
        }

        boolean result = evaluateCondition(condition);

        // phase: 0 = if-branch, 1 = else-branch, 2 = trailing (always render)
        int phase = 0;

        for (ASTNode child : node.children) {
            if (child instanceof JinjaEndIfNode) {
                phase = 2;
                continue;
            }

            if (child instanceof JinjaElseNode) {
                phase = 1;
                // ارسم أولاد الـ ElseNode لما الشرط false
                if (!result && child.children != null) {
                    for (ASTNode c : child.children) {
                        if (!(c instanceof JinjaEndIfNode)) {
                            renderNode(c, html);
                        }
                    }
                }
                // ★ المفتاح: إذا ElseNode عنده أولاد → الـ else content منتهي
                // أي sibling بعدو مش جزء من else → لازم يرسم دائماً
                if (child.children != null && !child.children.isEmpty()) {
                    phase = 2;
                }
                continue;
            }

            switch (phase) {
                case 0: // if-branch: ارسم لما الشرط true
                    if (result) renderNode(child, html);
                    break;
                case 1: // else-branch: ارسم لما الشرط false (ElseNode بدون أولاد)
                    if (!result) renderNode(child, html);
                    break;
                case 2: // trailing: ارسم دائماً
                    renderNode(child, html);
                    break;
            }
        }

        context.addLog("[JinjaRenderer] Evaluated condition: " + condition + " → " + result);
    }
    private List<ASTNode> getForBody(JinjaForNode node) {
        List<ASTNode> body = new ArrayList<>();
        for (ASTNode child : node.children) {
            if (child instanceof JinjaEndForNode) break;
            body.add(child);
        }
        return body;
    }

    private String extractExpression(JinjaExpressionNode node) {
        String name = node.nodeName;
        int start = name.indexOf("{{") + 2;
        int end = name.indexOf("}}");
        if (start > 0 && end > start) {
            return name.substring(start, end).trim();
        }
        return name;
    }

    private boolean evaluateCondition(String condition) {
        if (!condition.contains(".")) {
            Object value = context.resolveVariable(condition);
            if (value == null) return false;
            if (value instanceof List) return !((List<?>) value).isEmpty();
            if (value instanceof String) return !((String) value).isEmpty();
            return true;
        }

        String[] parts = condition.split("\\.");
        Object obj = context.resolveVariable(parts[0]);
        if (obj == null) {
            context.addWarning("Variable '" + parts[0] + "' not found for condition");
            return false;
        }
        if (obj instanceof Map) {
            Object field = ((Map<?, ?>) obj).get(parts[1]);
            return field != null && !field.toString().isEmpty();
        }
        return true;
    }
}
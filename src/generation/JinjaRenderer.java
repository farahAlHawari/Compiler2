package generation;

import AST.Core.ASTNode;
import AST.Core.PageNode;
import AST.Html.HtmlAttributeNode;
import AST.Html.HtmlElementNode;
import AST.Jinja.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class JinjaRenderer {

    private GenerationContext context;
    private StaticRenderer staticRenderer;

    private static final Set<String> VOID_TAGS = Set.of(
            "area", "base", "br", "col", "embed", "hr", "img", "input",
            "link", "meta", "param", "source", "track", "wbr"
    );

    public String render(PageNode page, GenerationContext ctx, StaticRenderer sr) {
        this.context = ctx;
        this.staticRenderer = sr;
        StringBuilder html = new StringBuilder();
        renderNode(page, html);
        return html.toString();
    }

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

    private void renderHtmlElement(HtmlElementNode node, StringBuilder html) {
        String tagName = node.getTagName();
        html.append("<").append(tagName);

        for (ASTNode child : node.children) {
            if (child instanceof HtmlAttributeNode) {
                renderAttribute((HtmlAttributeNode) child, html);
            }
        }

        if (isVoidTag(tagName)) {
            html.append(">\n");
            return;
        }

        html.append(">\n");

        for (ASTNode child : node.children) {
            if (!(child instanceof HtmlAttributeNode)) {
                renderNode(child, html);
            }
        }

        html.append("</").append(tagName).append(">\n");
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

    /**
     * ★★★ الإصلاح: النسخة القديمة كانت "مقفلة" على app1.py بالذات — تفحص
     * قيماً حرفية ثابتة ("/", "/add", بادئة "/details/", بادئة "/delete/").
     * كانت تعمل بالصدفة مع هذا التطبيق فقط لأن أسماء الدوال طابقت أسماء
     * القوالب (index→index.html، add→add.html). أي تطبيق Flask آخر
     * بمسارات أو أسماء دوال مختلفة كانت ستكسر الروابط الناتجة.
     *
     * النسخة الجديدة تبني المطابقة ديناميكياً من context.getRoutes() +
     * context.getTemplateForRoute()، بنفس منطق تسمية الملفات المستخدم
     * فعلياً في Generator (funcName + ".html" العادي، أو
     * baseName + "_" + i + ".html" للـ Routes المعاملية)، فتدعم أي عدد
     * من الـ Routes بدل مسارين اثنين فقط.
     */
    private String convertRouteToStaticPage(String attrName, String value) {
        if (!attrName.equals("href") && !attrName.equals("action")) {
            return value;
        }
        if (value == null || value.isEmpty() || context == null) {
            return value;
        }

        for (Map.Entry<String, String> route : context.getRoutes().entrySet()) {
            String routePattern = route.getKey();   // مثل "/details/<int:i>"
            String funcName = route.getValue();      // مثل "details"

            java.util.regex.Matcher m = Pattern.compile(routeToRegex(routePattern))
                    .matcher(value);
            if (!m.matches()) {
                continue;
            }

            String templateName = context.getTemplateForRoute(funcName);
            if (templateName == null) {
                // Route موجود لكن لا يعرض صفحة (مثل /delete/<int:i> الذي
                // يعمل redirect فقط) — لا صفحة له فعلياً
                return "#";
            }

            String baseName = templateName.endsWith(".html")
                    ? templateName.substring(0, templateName.length() - 5)
                    : templateName;

            return (m.groupCount() >= 1)
                    ? baseName + "_" + m.group(1) + ".html"
                    : funcName + ".html";
        }

        return value; // رابط خارجي غير معروف — إرجاعه كما هو
    }

    /** يحوّل نمط Route من Flask (مثل /details/<int:i>) إلى Regex قابل للمطابقة. */
    private String routeToRegex(String routePattern) {
        StringBuilder regex = new StringBuilder("^");
        int i = 0;
        while (i < routePattern.length()) {
            int lt = routePattern.indexOf('<', i);
            if (lt < 0) {
                regex.append(Pattern.quote(routePattern.substring(i)));
                break;
            }
            int gt = routePattern.indexOf('>', lt);
            if (gt < 0) {
                regex.append(Pattern.quote(routePattern.substring(i)));
                break;
            }
            if (lt > i) {
                regex.append(Pattern.quote(routePattern.substring(i, lt)));
            }
            String param = routePattern.substring(lt + 1, gt);
            regex.append(param.startsWith("int:") ? "(\\d+)" : "([^/]+)");
            i = gt + 1;
        }
        regex.append("$");
        return regex.toString();
    }

    private boolean isVoidTag(String tagName) {
        return VOID_TAGS.contains(tagName.toLowerCase());
    }

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

    private void renderFor(JinjaForNode node, StringBuilder html) {
        String forExpr = node.getForExpr();
        String[] parts = forExpr.split(" in ");

        // ★ إصلاح: forExpr مشوّه (بدون " in ") → تحذير بدل انهيار
        if (parts.length < 2) {
            context.addWarning("Malformed for-expression: '" + forExpr + "'");
            return;
        }

        String varName = parts[0].trim();
        String collectionName = parts[1].trim();

        Object collectionObj = context.resolveVariable(collectionName);
        if (collectionObj == null) {
            context.addWarning("Collection '" + collectionName + "' not found for for loop");
            return;
        }

        // ★★★ الإصلاح: كان فيه (List<Object>) collectionObj بدون حراسة.
        // لو المتغير مربوط بقيمة مش List (مثلاً Map أو نص) كان يرمي
        // ClassCastException ويوقف التوليد بالكامل — مخالف صراحة لمبدأ
        // "لا ينهار البرنامج". الآن: تحذير + skip فقط.
        if (!(collectionObj instanceof List)) {
            context.addWarning("Collection '" + collectionName + "' is not a list (found "
                    + collectionObj.getClass().getSimpleName() + ") — skipping for loop");
            return;
        }

        List<?> items = (List<?>) collectionObj;
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

    private void renderIf(JinjaIfNode node, StringBuilder html) {
        String condition = node.getCondition();
        if (condition.startsWith("if ")) {
            condition = condition.substring(3).trim();
        }

        boolean result = evaluateCondition(condition);
        int phase = 0; // 0=if, 1=else, 2=trailing

        for (ASTNode child : node.children) {
            if (child instanceof JinjaEndIfNode) {
                phase = 2;
                continue;
            }

            if (child instanceof JinjaElseNode) {
                phase = 1;
                if (!result && child.children != null) {
                    for (ASTNode c : child.children) {
                        if (!(c instanceof JinjaEndIfNode)) {
                            renderNode(c, html);
                        }
                    }
                }
                if (child.children != null && !child.children.isEmpty()) {
                    phase = 2;
                }
                continue;
            }

            switch (phase) {
                case 0: if (result) renderNode(child, html); break;
                case 1: if (!result) renderNode(child, html); break;
                case 2: renderNode(child, html); break;
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
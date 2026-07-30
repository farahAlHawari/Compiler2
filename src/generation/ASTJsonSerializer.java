package generation;

import AST.Core.ASTNode;
import AST.Core.PageNode;
import AST.Html.*;
import AST.Jinja.*;
import AST.Css.Rules.*;
import AST.Css.Selectors.*;
import AST.Css.Values.*;

import java.util.*;

/**
 * Person 4 — يحوّل شجرة AST (Jinja/HTML/CSS) إلى JSON string.
 * يستخدم StringBuilder فقط — بدون مكتبات خارجية.
 *
 * ⚠️ يحترم بنية AST — يقرأ فقط، ما يعدّل.
 */
public class ASTJsonSerializer {

    // =====================================================================
    // Public API
    // =====================================================================

    /**
     * يُسلسل كل template ASTs الموجودة بالـ context إلى JSON array واحد.
     */
    public String serializeJinjaASTs(GenerationContext context) {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        Map<String, PageNode> asts = context.getTemplateASTs();
        List<String> names = new ArrayList<>(asts.keySet());
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            PageNode ast = asts.get(name);

            sb.append("  {\n");
            sb.append("    \"template\": ").append(escapeJson(name)).append(",\n");
            sb.append("    \"ast\": ");
            serializeNode(ast, sb, 2);
            sb.append("\n  }");
            if (i < names.size() - 1) sb.append(",");
            sb.append("\n");
        }

        sb.append("]\n");
        return sb.toString();
    }

    // =====================================================================
    // Recursive Serialization
    // =====================================================================

    /**
     * يُسلسل عقدة AST واحدة (وأولادها) إلى JSON.
     * يكتب داخل الـ sb المُمرّر مع مسافات البداية.
     */
    private void serializeNode(ASTNode node, StringBuilder sb, int indent) {
        if (node == null) {
            sb.append("null");
            return;
        }

        String pad = "  ".repeat(indent);
        String childPad = "  ".repeat(indent + 1);

        sb.append("{\n");

        // type
        sb.append(pad).append("  \"type\": ")
                .append(escapeJson(node.getClass().getSimpleName())).append(",\n");

        // nodeName
        sb.append(pad).append("  \"nodeName\": ")
                .append(escapeJson(node.nodeName)).append(",\n");

        // line

        sb.append(pad).append("  \"line\": ").append(node.getLine());

        // ★ حقول خاصة بكل نوع ★
        String extraFields = extractExtraFields(node);
        if (extraFields != null && !extraFields.isEmpty()) {
            sb.append(",\n").append(extraFields);
        }

        // children
        if (node.children != null && !node.children.isEmpty()) {
            sb.append(",\n");
            sb.append(pad).append("  \"children\": [\n");

            for (int i = 0; i < node.children.size(); i++) {
                ASTNode child = node.children.get(i);
                serializeNode(child, sb, indent + 1);
                if (i < node.children.size() - 1) sb.append(",");
                sb.append("\n");
            }

            sb.append(pad).append("  ]");
        }

        sb.append("\n").append(pad).append("}");
    }

    // =====================================================================
    // Extra Fields per Node Type
    // =====================================================================

    /**
     * يرجع حقول JSON إضافية خاصة بنوع العقدة.
     * يرجع null إذا ما في حقول إضافية.
     * التنسيق: كل سطر يبدأ بالـ pad + "  " وينتهي بفاصلة.
     */
    private String extractExtraFields(ASTNode node) {
        String pad = "  ";

        if (node instanceof HtmlElementNode) {
            HtmlElementNode el = (HtmlElementNode) node;
            return pad + "  \"tagName\": " + escapeJson(el.getTagName())
                    + ",\n" + pad + "  \"isVoid\": " + isVoidTag(String.valueOf(el));
        }
        else if (node instanceof HtmlAttributeNode) {
            HtmlAttributeNode attr = (HtmlAttributeNode) node;
            return pad + "  \"attrName\": " + escapeJson(attr.getAttrName())
                    + ",\n" + pad + "  \"attrValue\": " + escapeJson(attr.getAttrValue());
        }
        else if (node instanceof JinjaForNode) {
            JinjaForNode forNode = (JinjaForNode) node;
            return pad + "  \"forExpr\": " + escapeJson(forNode.getForExpr());
        }
        else if (node instanceof JinjaIfNode) {
            JinjaIfNode ifNode = (JinjaIfNode) node;
            return pad + "  \"condition\": " + escapeJson(ifNode.getCondition());
        }
        else if (node instanceof JinjaElifNode) {
            JinjaElifNode elifNode = (JinjaElifNode) node;
            return pad + "  \"condition\": " + escapeJson(elifNode.getCondition());
        }
        else if (node instanceof JinjaBlockNode) {
            JinjaBlockNode blockNode = (JinjaBlockNode) node;
            return pad + "  \"blockName\": " + escapeJson(blockNode.getBlockName());
        }
        else if (node instanceof JinjaMacroNode) {
            JinjaMacroNode macroNode = (JinjaMacroNode) node;
            return pad + "  \"macroName\": " + escapeJson(macroNode.getMacroName());
        }
        else if (node instanceof DeclarationNode) {
            DeclarationNode decl = (DeclarationNode) node;
            return pad + "  \"property\": " + escapeJson(decl.getProperty())
                    + ",\n" + pad + "  \"value\": " + escapeJson(decl.getValue());
        }
        else if (node instanceof StyleBlockNode) {
            // closingLine موجود لكن ما في getter — نتجاهله
            return null;
        }
        // TextNode, DoctypeNode, JinjaElseNode, JinjaExpressionNode,
        // Selectors, CSS Values — المحتوى كله بـ nodeName
        return null;
    }
    // ✅ تعديل: أضفنا helper لأن isVoidTag private بال HtmlElementNode
    private boolean isVoidTag(String tagName) {
        return tagName.equals("area") || tagName.equals("base") || tagName.equals("br")
                || tagName.equals("col") || tagName.equals("embed") || tagName.equals("hr")
                || tagName.equals("img") || tagName.equals("input") || tagName.equals("link")
                || tagName.equals("meta") || tagName.equals("param") || tagName.equals("source")
                || tagName.equals("track") || tagName.equals("wbr");
    }

    // =====================================================================
    // JSON Helpers
    // =====================================================================

    /**
     * يهرب نص ليكون صالح داخل JSON string (مع quotes).
     * "hello" → "\"hello\""
     */
    private String escapeJson(String value) {
        if (value == null) return "null";
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        sb.append("\"");
        return sb.toString();
    }
}
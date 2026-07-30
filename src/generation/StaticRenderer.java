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

import java.util.List;

public class StaticRenderer {
    private GenerationContext context;

    public StaticRenderer(GenerationContext context) {
        this.context = context;
    }

    public void renderStaticNode(ASTNode node, StringBuilder html) {
        if (node instanceof PageNode) {
            for (ASTNode child : node.children) {       // ✅ تعديل: children بدل getChildren()
                renderStaticNode(child, html);
            }
        }
        else if (node instanceof DoctypeNode) {
            html.append("<!DOCTYPE html>\n");
        }
        else if (node instanceof HtmlElementNode) {
            renderHtmlElement((HtmlElementNode) node, html);
        }
        else if (node instanceof HtmlAttributeNode) {
            renderAttribute((HtmlAttributeNode) node, html);
        }
        else if (node instanceof TextNode) {
            html.append(extractText((TextNode) node));
        }
        else if (node instanceof StyleBlockNode) {
            renderStyleBlock((StyleBlockNode) node, html);
        }
        else if (node instanceof StyleRuleNode) {
            renderStyleRule((StyleRuleNode) node, html);
        }
        else if (node instanceof DeclarationNode) {
            renderDeclaration((DeclarationNode) node, html);
        }
        else if (node instanceof DeclarationListNode) {
            for (ASTNode child : node.children) {       // ✅ تعديل: children بدل getChildren()
                renderStaticNode(child, html);
            }
        }
        // Selectors
        else if (node instanceof TypeSelectorNode) { renderSelector(node, html); }
        else if (node instanceof ClassSelectorNode) { renderSelector(node, html); }
        else if (node instanceof IdSelectorNode) { renderSelector(node, html); }
        else if (node instanceof UniversalSelectorNode) { renderSelector(node, html); }
        else if (node instanceof CombinedSelectorNode) { renderSelector(node, html); }
        else if (node instanceof PseudoClassNode) { renderSelector(node, html); }
        else if (node instanceof PseudoElementNode) { renderSelector(node, html); }
        else if (node instanceof AttributeSelectorNode) { renderSelector(node, html); }
        // CSS Values
        else if (node instanceof NumericCssValue) { renderCssValue(node, html); }
        else if (node instanceof StringCssValue) { renderCssValue(node, html); }
        else if (node instanceof IdentifierCssValue) { renderCssValue(node, html); }
        else if (node instanceof FunctionCallCssValue) { renderCssValue(node, html); }
        else if (node instanceof ColorCssValue) { renderCssValue(node, html); }
        // ★ Unknown node → Warning + skip ★
        else {
            context.addWarning("Unknown static node: " + node.getClass().getSimpleName());
        }
    }

    private void renderHtmlElement(HtmlElementNode node, StringBuilder html) {
        html.append("<").append(node.getTagName());
        // كتابة attributes
        for (ASTNode child : node.children) {          // ✅ تعديل: children بدل getChildren()
            if (child instanceof HtmlAttributeNode) {
                renderAttribute((HtmlAttributeNode) child, html);
            }
        }
        // ✅ تعديل: isVoidTag() private بال HtmlElementNode → helper خاص بنا
        if (isVoidTag(node.getTagName())) {
            html.append(">\n");
            return;
        }
        html.append(">\n");
        // كتابة children (ما عدا attributes)
        for (ASTNode child : node.children) {          // ✅ تعديل: children بدل getChildren()
            if (!(child instanceof HtmlAttributeNode)) {
                renderStaticNode(child, html);
            }
        }
        html.append("</").append(node.getTagName()).append(">\n");
    }

    // ✅ تعديل: أضفنا helper لأن isVoidTag private بال HtmlElementNode
    private boolean isVoidTag(String tagName) {
        return tagName.equals("area") || tagName.equals("base") || tagName.equals("br")
                || tagName.equals("col") || tagName.equals("embed") || tagName.equals("hr")
                || tagName.equals("img") || tagName.equals("input") || tagName.equals("link")
                || tagName.equals("meta") || tagName.equals("param") || tagName.equals("source")
                || tagName.equals("track") || tagName.equals("wbr");
    }

    private void renderAttribute(HtmlAttributeNode node, StringBuilder html) {
        html.append(" ").append(node.getAttrName());
        String val = node.getAttrValue();
        // Strip quotes لو الـ parser حطها
        if (val.startsWith("\"") && val.endsWith("\"") && val.length() > 1) {
            val = val.substring(1, val.length() - 1);
        }
        if (!val.isEmpty()) {
            html.append("=\"").append(val).append("\"");
        }
    }

    private void renderStyleBlock(StyleBlockNode node, StringBuilder html) {
        html.append("<style>\n");
        for (ASTNode child : node.children) {          // ✅ تعديل: children بدل getChildren()
            renderStaticNode(child, html);
        }
        html.append("</style>\n");
    }


    private void renderDeclaration(DeclarationNode node, StringBuilder html) {
        html.append("  ").append(node.getProperty()).append(": ");
        html.append(node.getValue()).append(";\n");
    }

    private void renderSelector(ASTNode node, StringBuilder html) {
        // ★ CombinedSelector = أولادها هم النص الفعلي (a + :hover = a:hover)
        if (node instanceof CombinedSelectorNode) {
            for (ASTNode child : node.children) {
                renderSelector(child, html);
            }
            return;
        }

        String name = node.nodeName;
        int colonSpace = name.indexOf(": ");
        if (colonSpace >= 0) {
            html.append(name.substring(colonSpace + 2).trim());
        } else {
            int spaceIdx = name.indexOf(" ");
            if (spaceIdx >= 0) {
                html.append(name.substring(spaceIdx + 1).trim());
            } else {
                html.append(name);
            }
        }
    }
    private void renderStyleRule(StyleRuleNode node, StringBuilder html) {
        List<ASTNode> ch = node.children;
        if (ch.isEmpty()) return;

        // Render كل الـ selectors اللي قبل أول declaration
        int declStartIdx = 0;
        boolean first = true;
        for (int i = 0; i < ch.size(); i++) {
            if (isSelectorNode(ch.get(i))) {
                if (!first) html.append(", ");
                renderStaticNode(ch.get(i), html);
                first = false;
                declStartIdx = i + 1;
            } else {
                break; // وصلنا لـ declarations
            }
        }

        html.append(" {\n");
        for (int i = declStartIdx; i < ch.size(); i++) {
            renderStaticNode(ch.get(i), html);
        }
        html.append("}\n");
    }

    // Helper — يحدد هل الـ node هو selector
    private boolean isSelectorNode(ASTNode node) {
        return node instanceof TypeSelectorNode || node instanceof ClassSelectorNode
                || node instanceof IdSelectorNode || node instanceof UniversalSelectorNode
                || node instanceof CombinedSelectorNode || node instanceof PseudoClassNode
                || node instanceof PseudoElementNode || node instanceof AttributeSelectorNode;
    }
    private void renderCssValue(ASTNode node, StringBuilder html) {
        String name = node.nodeName;
        if (name.contains(":")) {
            html.append(name.substring(name.indexOf(":") + 1).trim());
        }
    }

    private String extractText(TextNode node) {
        String name = node.nodeName;
        int start = name.indexOf("\"") + 1;
        int end = name.lastIndexOf("\"");
        if (start > 0 && end > start) {
            return name.substring(start, end);
        }
        return name;
    }
}
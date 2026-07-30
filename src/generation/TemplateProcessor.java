package generation;

import AST.Core.ASTNode;
import AST.Core.PageNode;
import AST.Jinja.JinjaExtendsNode;
import AST.Jinja.JinjaBlockNode;
import AST.Jinja.JinjaEndBlockNode;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateProcessor {

    public PageNode process(PageNode childPage, GenerationContext context) {
        // 1. يبحث عن JinjaExtendsNode في child
        JinjaExtendsNode extendsNode = findExtendsNode(childPage);
        if (extendsNode == null) return childPage; // لا extends → صفحة كاملة

        // 2. ★ Validation: التحقق من وجود Base Template ★
        String parentName = extractTemplateName(extendsNode);
        PageNode parentPage = context.getTemplate(parentName);
        if (parentPage == null) {
            context.addWarning("Base template '" + parentName + "' not found for extends");
            return childPage;
        }

        // 3. يجمع block names + content من child
        Map<String, List<ASTNode>> childBlocks = collectBlocks(childPage);

        // 4. ينسخ شجرة parent (لا نعدّل الأصلية!)
        PageNode merged = deepCopy(parentPage);

        // 5. ★ Validation: التحقق من وجود Block المطلوب ★
        replaceBlocks(merged, childBlocks, context);

        context.addLog("[TemplateProcessor] Merged " + parentName);

        return merged;
    }

    private JinjaExtendsNode findExtendsNode(PageNode page) {
        for (ASTNode child : page.children) {
            if (child instanceof JinjaExtendsNode) {
                return (JinjaExtendsNode) child;
            }
        }
        return null;
    }

    private String extractTemplateName(JinjaExtendsNode node) {
        String name = node.nodeName;
        int spaceIdx = name.indexOf(" ");
        if (spaceIdx < 0) return name;
        String template = name.substring(spaceIdx + 1).trim();
        if (template.startsWith("\"") && template.endsWith("\"") && template.length() > 1) {
            template = template.substring(1, template.length() - 1);
        }
        return template;
    }

    private Map<String, List<ASTNode>> collectBlocks(PageNode page) {
        Map<String, List<ASTNode>> blocks = new HashMap<>();
        collectBlocksFromList(page.children, blocks);
        return blocks;
    }

    private void collectBlocksFromList(List<ASTNode> nodes, Map<String, List<ASTNode>> blocks) {
        for (int i = 0; i < nodes.size(); i++) {
            ASTNode node = nodes.get(i);
            if (node instanceof JinjaBlockNode) {
                JinjaBlockNode blockNode = (JinjaBlockNode) node;
                String blockName = blockNode.getBlockName();
                List<ASTNode> content = new ArrayList<>();

                // ★ Fix: الـ parser ممكن يخزّن المحتوى كأولاد لـ JinjaBlockNode ★
                if (node.children != null && !node.children.isEmpty()) {
                    for (ASTNode child : node.children) {
                        if (!(child instanceof JinjaEndBlockNode)) {
                            content.add(child);
                        }
                    }
                }

                // Fallback: المحتوى كـ siblings بين block و endblock
                if (content.isEmpty()) {
                    for (int j = i + 1; j < nodes.size(); j++) {
                        ASTNode sibling = nodes.get(j);
                        if (sibling instanceof JinjaEndBlockNode) {
                            break;
                        }
                        content.add(sibling);
                    }
                }

                blocks.put(blockName, content);
            }
            if (node.children != null && !node.children.isEmpty()) {
                collectBlocksFromList(node.children, blocks);
            }
        }
    }

    private PageNode deepCopy(PageNode page) {
        PageNode copy = new PageNode(page.getLine());
        for (ASTNode child : page.children) {
            copy.children.add(deepCopyNode(child));
        }
        return copy;
    }

    private ASTNode deepCopyNode(ASTNode node) {
        ASTNode copy = createNodeCopy(node);
        if (copy == null) return node;
        if (node.children != null) {
            for (ASTNode child : node.children) {
                copy.children.add(deepCopyNode(child));
            }
        }
        return copy;
    }

    private ASTNode createNodeCopy(ASTNode node) {
        int line = node.getLine();
        String cls = node.getClass().getSimpleName();

        try {
            Constructor<?> ctor = node.getClass().getConstructor(int.class);
            return (ASTNode) ctor.newInstance(line);
        } catch (Exception e) {}

        String semantic = extractSemanticValue(node);
        try {
            Constructor<?> ctor = node.getClass().getConstructor(String.class, int.class);
            return (ASTNode) ctor.newInstance(semantic, line);
        } catch (Exception e) {}

        String[] two = extractTwoStrings(node);
        if (two != null) {
            try {
                Constructor<?> ctor = node.getClass().getConstructor(
                        String.class, String.class, int.class);
                return (ASTNode) ctor.newInstance(two[0], two[1], line);
            } catch (Exception e) {}
        }

        return null;
    }

    private String extractSemanticValue(ASTNode node) {
        String name = node.nodeName;
        String cls = node.getClass().getSimpleName();

        switch (cls) {
            case "HtmlElementNode":
                try {
                    return (String) node.getClass().getMethod("getTagName").invoke(node);
                } catch (Exception e) { return name; }

            case "TextNode": {
                int s = name.indexOf("\"") + 1;
                int e = name.lastIndexOf("\"");
                return (s > 0 && e > s) ? name.substring(s, e) : name;
            }

            case "DoctypeNode":
                return name.substring(name.indexOf(" ") + 1);

            case "StyleRuleNode":
                return name.substring(name.indexOf(" ") + 1);

            case "TypeSelectorNode":
                return name.substring(name.indexOf(":") + 1).trim();

            case "ClassSelectorNode": {
                String val = name.substring(name.indexOf(":") + 1).trim();
                return val.startsWith(".") ? val.substring(1) : val;
            }

            case "IdSelectorNode": {
                String val = name.substring(name.indexOf(":") + 1).trim();
                return val.startsWith("#") ? val.substring(1) : val;
            }

            case "PseudoClassNode": {
                String val = name.substring(name.indexOf(" ") + 1).trim();
                return val.startsWith(":") ? val.substring(1) : val;
            }

            case "PseudoElementNode": {
                String val = name.substring(name.indexOf(" ") + 1).trim();
                return val.startsWith("::") ? val.substring(2) : val;
            }

            case "AttributeSelectorNode":
                return name.substring(name.indexOf(" ") + 1);

            case "StringCssValue":
            case "IdentifierCssValue":
            case "FunctionCallCssValue":
            case "ColorCssValue":
                return name.substring(name.indexOf(":") + 1).trim();

            case "JinjaExtendsNode": {
                String val = name.substring(name.indexOf(" ") + 1).trim();
                if (val.startsWith("\"") && val.endsWith("\"") && val.length() > 1)
                    val = val.substring(1, val.length() - 1);
                return val;
            }

            case "JinjaBlockNode":
                try { return (String) node.getClass().getMethod("getBlockName").invoke(node); }
                catch (Exception e) { return name; }

            case "JinjaForNode":
                try { return (String) node.getClass().getMethod("getForExpr").invoke(node); }
                catch (Exception e) { return name; }

            case "JinjaIfNode":
            case "JinjaElifNode":
                try { return (String) node.getClass().getMethod("getCondition").invoke(node); }
                catch (Exception e) { return name; }

            case "JinjaExpressionNode": {
                int s = name.indexOf("{{") + 2;
                int e = name.indexOf("}}");
                return (s > 1 && e > s) ? name.substring(s, e).trim() : name;
            }

            case "JinjaSetNode":
            case "JinjaImportNode":
                return name.substring(name.indexOf(" ") + 1);

            case "JinjaIncludeNode": {
                String val = name.substring(name.indexOf(" ") + 1).trim();
                if (val.startsWith("\"") && val.endsWith("\"") && val.length() > 1)
                    val = val.substring(1, val.length() - 1);
                return val;
            }

            case "JinjaMacroNode":
                try { return (String) node.getClass().getMethod("getMacroName").invoke(node); }
                catch (Exception e) { return name; }

            case "PageNode":
            case "StyleBlockNode":
            case "DeclarationListNode":
            case "UniversalSelectorNode":
            case "CombinedSelectorNode":
            case "JinjaEndBlockNode":
            case "JinjaEndForNode":
            case "JinjaEndIfNode":
            case "JinjaElseNode":
            case "JinjaEndMacroNode":
            case "NumericCssValue":
                return null;

            default:
                return name;
        }
    }

    private String[] extractTwoStrings(ASTNode node) {
        String cls = node.getClass().getSimpleName();

        switch (cls) {
            case "HtmlAttributeNode":
                try {
                    return new String[]{
                            (String) node.getClass().getMethod("getAttrName").invoke(node),
                            (String) node.getClass().getMethod("getAttrValue").invoke(node)
                    };
                } catch (Exception e) { return null; }

            case "DeclarationNode":
                try {
                    return new String[]{
                            (String) node.getClass().getMethod("getProperty").invoke(node),
                            (String) node.getClass().getMethod("getValue").invoke(node)
                    };
                } catch (Exception e) { return null; }

            case "NumericCssValue": {
                String name = node.nodeName;
                String val = name.substring(name.indexOf(":") + 1).trim();
                int i = 0;
                while (i < val.length() && (Character.isDigit(val.charAt(i))
                        || val.charAt(i) == '.' || val.charAt(i) == '-')) i++;
                if (i == 0) return null;
                return new String[]{val.substring(0, i), val.substring(i)};
            }

            default: return null;
        }
    }

    private void replaceBlocks(PageNode merged, Map<String, List<ASTNode>> childBlocks,
                               GenerationContext context) {
        replaceBlocksInList(merged.children, childBlocks, context);
    }

    private void replaceBlocksInList(List<ASTNode> nodes, Map<String, List<ASTNode>> childBlocks,
                                     GenerationContext context) {
        int i = 0;
        while (i < nodes.size()) {
            ASTNode node = nodes.get(i);
            if (node instanceof JinjaBlockNode) {
                JinjaBlockNode blockNode = (JinjaBlockNode) node;
                String blockName = blockNode.getBlockName();
                if (childBlocks.containsKey(blockName)) {
                    // ★ نبحث عن JinjaEndBlockNode كـ sibling ★
                    int endIdx = -1;
                    for (int j = i + 1; j < nodes.size(); j++) {
                        if (nodes.get(j) instanceof JinjaEndBlockNode) { endIdx = j; break; }
                    }

                    if (endIdx > i) {
                        // Sibling-based: أحذف من block لـ endblock (شامل) وأدخل المحتوى
                        for (int j = endIdx; j >= i; j--) nodes.remove(j);
                    } else {
                        // ★ Child-based: أحذف block فقط (المحتوى جواه كأولاد) ★
                        nodes.remove(i);
                    }

                    List<ASTNode> content = childBlocks.get(blockName);
                    for (int j = 0; j < content.size(); j++) nodes.add(i + j, content.get(j));
                    i += content.size();
                    continue;
                } else {
                    context.addWarning("Block '" + blockName + "' not found in child template");
                }
            }
            if (node.children != null && !node.children.isEmpty()) {
                replaceBlocksInList(node.children, childBlocks, context);
            }
            i++;
        }
    }
}
package generation;

import AST.Core.ASTNode;
import AST.Core.PageNode;
import AST.Html.*;
import AST.Jinja.*;
import AST.Css.Rules.*;
import AST.Css.Selectors.*;
import AST.Css.Values.*;
import main.pythoncompiler.ast.AssignNode;
import main.pythoncompiler.ast.FunctionDefNode;
import main.pythoncompiler.ast.CallNode;
import main.pythoncompiler.ast.LiteralNode;
import main.pythoncompiler.ast.IdentifierNode;
import main.pythoncompiler.ast.BinaryOpNode;
import main.pythoncompiler.ast.AttributeNode;
import main.pythoncompiler.ast.ReturnNode;
import main.pythoncompiler.ast.ForNode;
import main.pythoncompiler.ast.ClassDefNode;
import main.pythoncompiler.ast.UnaryOpNode;

import java.util.*;


public class ASTJsonSerializer {


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



    public String serializePythonAST(main.pythoncompiler.ast.ASTNode pythonRoot) {
        if (pythonRoot == null) return null;
        StringBuilder sb = new StringBuilder();
        serializePyNode(pythonRoot, sb, 0);
        return sb.toString();
    }


    private void serializeNode(ASTNode node, StringBuilder sb, int indent) {
        if (node == null) {
            sb.append("null");
            return;
        }

        String pad = "  ".repeat(indent);

        sb.append("{\n");

        // type
        sb.append(pad).append("  \"type\": ")
                .append(escapeJson(node.getClass().getSimpleName())).append(",\n");

        // nodeName
        sb.append(pad).append("  \"nodeName\": ")
                .append(escapeJson(node.nodeName)).append(",\n");

        // line
        sb.append(pad).append("  \"line\": ").append(node.getLine());

        // ★ حقول خاصة بكل نوع (Jinja/HTML/CSS) ★
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


    private void serializePyNode(main.pythoncompiler.ast.ASTNode node, StringBuilder sb, int indent) {
        if (node == null) {
            sb.append("null");
            return;
        }

        String pad = "  ".repeat(indent);

        sb.append("{\n");

        sb.append(pad).append("  \"type\": ")
                .append(escapeJson(node.getClass().getSimpleName())).append(",\n");

        sb.append(pad).append("  \"nodeName\": ")
                .append(escapeJson(node.nodeName)).append(",\n");


        sb.append(pad).append("  \"line\": ").append(node.lineNumber);

        String extraFields = extractPyExtraFields(node);
        if (extraFields != null && !extraFields.isEmpty()) {
            sb.append(",\n").append(extraFields);
        }

        if (node.children != null && !node.children.isEmpty()) {
            sb.append(",\n");
            sb.append(pad).append("  \"children\": [\n");

            for (int i = 0; i < node.children.size(); i++) {
                main.pythoncompiler.ast.ASTNode child = node.children.get(i);
                serializePyNode(child, sb, indent + 1);
                if (i < node.children.size() - 1) sb.append(",");
                sb.append("\n");
            }

            sb.append(pad).append("  ]");
        }

        sb.append("\n").append(pad).append("}");
    }


    private String extractExtraFields(ASTNode node) {
        String pad = "  ";

        if (node instanceof HtmlElementNode) {
            HtmlElementNode el = (HtmlElementNode) node;

            return pad + "  \"tagName\": " + escapeJson(el.getTagName())
                    + ",\n" + pad + "  \"isVoid\": " + isVoidTag(el.getTagName());
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
        return null;
    }



    private String extractPyExtraFields(main.pythoncompiler.ast.ASTNode node) {
        String pad = "  ";

        if (node instanceof AssignNode) {
            AssignNode n = (AssignNode) node;
            StringBuilder sb = new StringBuilder();
            sb.append(pad).append("  \"variableName\": ").append(escapeJson(n.variableName)).append(",\n");
            sb.append(pad).append("  \"operator\": ").append(escapeJson(n.operator));
            if (n.declaredType != null && !n.declaredType.isEmpty()) {
                sb.append(",\n").append(pad).append("  \"declaredType\": ").append(escapeJson(n.declaredType));
            }
            return sb.toString();
        }
        else if (node instanceof FunctionDefNode) {
            FunctionDefNode n = (FunctionDefNode) node;
            StringBuilder sb = new StringBuilder();
            sb.append(pad).append("  \"functionName\": ").append(escapeJson(n.functionName)).append(",\n");
            sb.append(pad).append("  \"returnType\": ").append(escapeJson(n.returnType)).append(",\n");
            sb.append(pad).append("  \"paramCount\": ").append(n.paramCount);
            return sb.toString();
        }
        else if (node instanceof CallNode) {
            CallNode n = (CallNode) node;
            StringBuilder sb = new StringBuilder();
            sb.append(pad).append("  \"functionName\": ").append(escapeJson(n.functionName)).append(",\n");
            sb.append(pad).append("  \"argCount\": ").append(n.argCount);
            return sb.toString();
        }
        else if (node instanceof LiteralNode) {
            LiteralNode n = (LiteralNode) node;
            StringBuilder sb = new StringBuilder();
            sb.append(pad).append("  \"value\": ").append(escapeJson(n.value)).append(",\n");
            sb.append(pad).append("  \"literalType\": ").append(escapeJson(n.type));
            return sb.toString();
        }
        else if (node instanceof IdentifierNode) {
            IdentifierNode n = (IdentifierNode) node;
            return pad + "  \"name\": " + escapeJson(n.name);
        }
        else if (node instanceof BinaryOpNode) {
            BinaryOpNode n = (BinaryOpNode) node;
            return pad + "  \"operator\": " + escapeJson(n.operator);
        }
        else if (node instanceof UnaryOpNode) {
            UnaryOpNode n = (UnaryOpNode) node;
            return pad + "  \"operator\": " + escapeJson(n.operator);
        }
        else if (node instanceof AttributeNode) {
            AttributeNode n = (AttributeNode) node;
            return pad + "  \"attributeName\": " + escapeJson(n.attributeName);
        }
        else if (node instanceof ReturnNode) {
            ReturnNode n = (ReturnNode) node;
            StringBuilder sb = new StringBuilder();
            sb.append(pad).append("  \"returnExprType\": ").append(escapeJson(n.returnExprType)).append(",\n");
            sb.append(pad).append("  \"enclosingFunctionName\": ").append(escapeJson(n.enclosingFunctionName));
            return sb.toString();
        }
        else if (node instanceof ForNode) {
            ForNode n = (ForNode) node;
            return pad + "  \"iteratorName\": " + escapeJson(n.iteratorName);
        }
        else if (node instanceof ClassDefNode) {
            ClassDefNode n = (ClassDefNode) node;
            return pad + "  \"className\": " + escapeJson(n.className);
        }
        return null;
    }



    private boolean isVoidTag(String tagName) {
        return tagName.equals("area") || tagName.equals("base") || tagName.equals("br")
                || tagName.equals("col") || tagName.equals("embed") || tagName.equals("hr")
                || tagName.equals("img") || tagName.equals("input") || tagName.equals("link")
                || tagName.equals("meta") || tagName.equals("param") || tagName.equals("source")
                || tagName.equals("track") || tagName.equals("wbr");
    }

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
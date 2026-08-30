
package Visitor;
import AST.Core.*;
import AST.Css.Rules.*;
import AST.Css.Selectors.*;
import AST.Css.Values.*;
import AST.Jinja.*;
import AST.Html.*;
import antlr.TemplateParser;
import antlr.TemplateParserBaseVisitor;

public class HtmlCssJinjaVisitor extends TemplateParserBaseVisitor<ASTNode> {



    private String getOriginalText(org.antlr.v4.runtime.ParserRuleContext ctx) {
        if (ctx == null || ctx.getStart() == null || ctx.getStop() == null) return "";
        int start = ctx.getStart().getStartIndex();
        int stop = ctx.getStop().getStopIndex();
        return ctx.getStart().getInputStream().getText(
                new org.antlr.v4.runtime.misc.Interval(start, stop)
        );
    }
    // Page

    @Override
    public ASTNode visitPage(TemplateParser.PageContext ctx) {

        PageNode page = new PageNode(ctx.start.getLine());
        if (ctx.doctype() != null) {
            page.addChild(visit(ctx.doctype()));
        }
        for (int i = 0; i < ctx.content().size(); i++) {
            page.addChild(visit(ctx.content(i)));
        }
        return page;
    }

    @Override
    public ASTNode visitContent(TemplateParser.ContentContext ctx) {
        return visit(ctx.node());
    }


    @Override
    public ASTNode visitDoctypeNode(TemplateParser.DoctypeNodeContext ctx) {
        return new DoctypeNode(ctx.getText(), ctx.start.getLine());
    }


    @Override
    public ASTNode visitPairedHtmlElement(TemplateParser.PairedHtmlElementContext ctx) {

        String tagName = ctx.openingTag().getChild(1).getText();

        HtmlElementNode element =
                new HtmlElementNode(tagName, ctx.openingTag().start.getLine());


        for (int i = 0; i < ctx.openingTag().getChildCount(); i++) {
            if (ctx.openingTag().getChild(i) instanceof TemplateParser.AttributeContext) {
                element.addChild(
                        visit((TemplateParser.AttributeContext) ctx.openingTag().getChild(i))
                );
            }
        }


        for (TemplateParser.ContentContext c : ctx.content()) {
            element.addChild(visit(c));
        }


        element.setClosingLine(ctx.closingTag().start.getLine());

        return element;
    }

    @Override
    public ASTNode visitSelfClosingElement(TemplateParser.SelfClosingElementContext ctx) {


        String tagName = ctx.tagName.getText();


        HtmlElementNode element = new HtmlElementNode(tagName, ctx.start.getLine());


        if (ctx.attrs != null) {
            for (TemplateParser.AttributeContext attrCtx : ctx.attribute()) {
                element.addChild(visit(attrCtx));
            }
        }

        return element;
    }
    @Override
    public ASTNode visitHtmlAttribute(TemplateParser.HtmlAttributeContext ctx) {
        String name = ctx.attrName.getText();
        String value = ctx.attrValue != null ? ctx.attrValue.getText() : "";
        return new HtmlAttributeNode(name, value, ctx.start.getLine());
    }

    @Override
    public ASTNode visitTextContentNode(TemplateParser.TextContentNodeContext ctx) {
        String text = ctx.getText().trim();
        if (text.isEmpty()) return null;
        return new TextNode(text, ctx.start.getLine());
    }

    // CSS RULES SECTION

    @Override
    public ASTNode visitStyleBlockNode(TemplateParser.StyleBlockNodeContext ctx) {

        StyleBlockNode block =
                new StyleBlockNode(ctx.STYLE_OPEN().getSymbol().getLine());

        for (TemplateParser.StyleRuleContext ruleCtx : ctx.styleRule()) {
            block.addChild(visit(ruleCtx));
        }


        block.setClosingLine(ctx.STYLE_CLOSE().getSymbol().getLine());

        return block;
    }




    @Override
    public ASTNode visitStyleRuleNode(TemplateParser.StyleRuleNodeContext ctx) {
        StyleRuleNode rule = new StyleRuleNode("Rule", ctx.start.getLine());


        if (ctx.selectorList() != null) {
            for (TemplateParser.SelectorContext selCtx : ctx.selectorList().selector()) {
                ASTNode child = visit(selCtx);
                if (child != null) rule.addChild(child);
            }
        }


        if (ctx.declarationList() != null) {
            rule.addChild(visit(ctx.declarationList()));
        }

        return rule;
    }

    @Override
    public ASTNode visitDeclarationListNode(TemplateParser.DeclarationListNodeContext ctx) {

        DeclarationListNode list = new DeclarationListNode(ctx.start.getLine());
        for (TemplateParser.DeclarationContext decCtx : ctx.declaration()) {
            list.addChild(visit(decCtx));
        }
        return list;
    }
    @Override
    public ASTNode visitDeclarationNode(TemplateParser.DeclarationNodeContext ctx) {
        String property = ctx.property.getText();


        StringBuilder valueBuilder = new StringBuilder();

        if (ctx.val != null) {
            for (int i = 0; i < ctx.val.getChildCount(); i++) {
                if (ctx.val.getChild(i) instanceof TemplateParser.StyleValuePartContext) {

                    String partText = ctx.val.getChild(i).getText().trim();
                    if (!partText.isEmpty()) {
                        if (valueBuilder.length() > 0) valueBuilder.append(" ");
                        valueBuilder.append(partText);
                    }
                }
            }
        }

        DeclarationNode node = new DeclarationNode(property, valueBuilder.toString(), ctx.start.getLine());


        if (ctx.val != null) {
            for (int i = 0; i < ctx.val.getChildCount(); i++) {
                if (ctx.val.getChild(i) instanceof TemplateParser.StyleValuePartContext) {
                    ASTNode valueNode = visit(ctx.val.getChild(i));
                    if (valueNode != null) {
                        node.addChild(valueNode);
                    }
                }
            }
        }

        return node;
    }



    // CSS SELECTORS SECTION
    @Override
    public ASTNode visitSelector(TemplateParser.SelectorContext ctx) {

        int partsCount = ctx.selectorPart().size();


        if (partsCount == 1) {

            return visit(ctx.selectorPart(0));
        }


        if (partsCount > 1) {
            CombinedSelectorNode combined = new CombinedSelectorNode(ctx.start.getLine());


            for (TemplateParser.SelectorPartContext partCtx : ctx.selectorPart()) {
                combined.addChild(visit(partCtx));
            }

            return combined;
        }

        return null;
    }
    @Override
    public ASTNode visitTypeSelector(TemplateParser.TypeSelectorContext ctx) {
        return new TypeSelectorNode(ctx.STYLE_IDENT().getText(), ctx.start.getLine());
    }

    @Override
    public ASTNode visitClassSelector(TemplateParser.ClassSelectorContext ctx) {
        return new ClassSelectorNode(ctx.STYLE_IDENT().getText(), ctx.start.getLine());
    }

    @Override
    public ASTNode visitIdSelector(TemplateParser.IdSelectorContext ctx) {
        return new IdSelectorNode(ctx.STYLE_IDENT().getText(), ctx.start.getLine());
    }

    @Override
    public ASTNode visitUniversalSelector(TemplateParser.UniversalSelectorContext ctx) {
        return new UniversalSelectorNode(ctx.start.getLine());
    }

    @Override
    public ASTNode visitPseudoClassSelector(TemplateParser.PseudoClassSelectorContext ctx) {
        String name = ctx.STYLE_IDENT().getText();
        return new PseudoClassNode(name, ctx.start.getLine());
    }

    @Override
    public ASTNode visitPseudoClassWithArgSelector(TemplateParser.PseudoClassWithArgSelectorContext ctx) {
        String name = ctx.getChild(1).getText();
        String arg = ctx.getChild(3).getText();
        return new PseudoClassNode(name + "(" + arg + ")", ctx.start.getLine());
    }

    @Override
    public ASTNode visitPseudoElementSelector(TemplateParser.PseudoElementSelectorContext ctx) {
        String name = ctx.STYLE_IDENT().getText();
        return new PseudoElementNode(name, ctx.start.getLine());
    }

    @Override
    public ASTNode visitAttributeSelectorPart(TemplateParser.AttributeSelectorPartContext ctx) {
        String attrText = ctx.attributeSelector().getText();
        return new AttributeSelectorNode("[" + attrText + "]", ctx.start.getLine());
    }

    // CSS VALUES SECTION

    @Override
    public ASTNode visitStringStyleValue(TemplateParser.StringStyleValueContext ctx) {
        String val = ctx.getText().replaceAll("^\"|\"$|^'|'$", "");
        return new StringCssValue(val, ctx.start.getLine());
    }

    @Override
    public ASTNode visitNumericStyleValue(TemplateParser.NumericStyleValueContext ctx) {
        String num = ctx.numericValueType().getText();
        String unit = (ctx.unit != null) ? ctx.unit.getText() : "";
        return new NumericCssValue(num, unit, ctx.start.getLine());
    }

    @Override
    public ASTNode visitIdentifierStyleValue(TemplateParser.IdentifierStyleValueContext ctx) {
        return new IdentifierCssValue(ctx.STYLE_IDENT().getText(), ctx.start.getLine());
    }

    @Override
    public ASTNode visitColorStyleValue(TemplateParser.ColorStyleValueContext ctx) {
        return new ColorCssValue(ctx.COLOR_HEX().getText(), ctx.start.getLine());
    }

    @Override
    public ASTNode visitFunctionStyleValue(TemplateParser.FunctionStyleValueContext ctx) {
        return new FunctionCallCssValue(ctx.functionCall().getText(), ctx.start.getLine());
    }

    @Override
    public ASTNode visitUrlStyleValue(TemplateParser.UrlStyleValueContext ctx) {
        return new FunctionCallCssValue(ctx.URL().getText(), ctx.start.getLine());
    }

    // JINJA STRUCTURE SECTION

    @Override
    public ASTNode visitJinjaExpression(TemplateParser.JinjaExpressionContext ctx) {
        return new JinjaExpressionNode(getOriginalText(ctx.body).trim(), ctx.start.getLine());
    }



    @Override
    public ASTNode visitJinjaSimpleNode(TemplateParser.JinjaSimpleNodeContext ctx) {
        String body = getOriginalText(ctx.body).trim();
        int line = ctx.start.getLine();

        if (body.startsWith("extends"))  return new JinjaExtendsNode(body.substring(7).trim(), line);
        if (body.startsWith("include"))  return new JinjaIncludeNode(body.substring(7).trim(), line);
        if (body.startsWith("import"))   return new JinjaImportNode(body.substring(6).trim(), line);
        if (body.startsWith("set"))      return new JinjaSetNode(body.substring(3).trim(), line);
        if (body.startsWith("elif"))     return new JinjaElifNode(body.substring(4).trim(), line);
        if (body.startsWith("if")) return new JinjaIfNode(body, line);
        if (body.startsWith("else"))     return new JinjaElseNode(line);
        if (body.startsWith("macro")) {
            String cleanBody = body.substring(5).trim();
            String macroName = cleanBody.split("\\(")[0].trim();
            JinjaMacroNode macroNode = new JinjaMacroNode(macroName, line);
            macroNode.parseParameters(cleanBody);  // ← أضيفي
            return macroNode;
        }
        if (body.startsWith("endmacro")) return new JinjaEndMacroNode(line);
        return new JinjaNode("JinjaSimple " + body, line) {};
    }



    @Override
    public ASTNode visitJinjaContainerNode(TemplateParser.JinjaContainerNodeContext ctx) {
        ASTNode container = visit(ctx.start);

        for (int i = 0; i < ctx.content().size(); i++) {
            container.addChild(visit(ctx.content(i)));
        }

        ASTNode endNode = visit(ctx.end);
        if (endNode != null) {
            container.addChild(endNode);
        }

        return container;
    }

    //JINJA CONTROL FLOW

    @Override
    public ASTNode visitIfStatementStart(TemplateParser.IfStatementStartContext ctx) {
        return new JinjaIfNode(getOriginalText(ctx.body).trim(), ctx.start.getLine());
    }

    @Override
    public ASTNode visitElseStatementStart(TemplateParser.ElseStatementStartContext ctx) {
        return new JinjaElseNode(ctx.start.getLine());
    }

    @Override
    public ASTNode visitElifStatementStart(TemplateParser.ElifStatementStartContext ctx) {
        return new JinjaElifNode(getOriginalText(ctx.body).trim(), ctx.start.getLine());
    }

    @Override
    public ASTNode visitIfStatementEnd(TemplateParser.IfStatementEndContext ctx) {
        return new JinjaEndIfNode(ctx.start.getLine());
    }

    @Override
    public ASTNode visitForStatementStart(TemplateParser.ForStatementStartContext ctx) {
        return new JinjaForNode(getOriginalText(ctx.body).trim(), ctx.start.getLine());
    }

    @Override
    public ASTNode visitForStatementEnd(TemplateParser.ForStatementEndContext ctx) {
        return new JinjaEndForNode(ctx.start.getLine());
    }

    @Override
    public ASTNode visitBlockStatementStart(TemplateParser.BlockStatementStartContext ctx) {
        return new JinjaBlockNode(ctx.name.getText(), ctx.start.getLine());
    }

    @Override
    public ASTNode visitBlockStatementEnd(TemplateParser.BlockStatementEndContext ctx) {
        return new JinjaEndBlockNode(ctx.start.getLine());
    }

    @Override
    public ASTNode visitMacroStatementStart(TemplateParser.MacroStatementStartContext ctx) {
        JinjaMacroNode macroNode = new JinjaMacroNode(ctx.name.getText(), ctx.start.getLine());
        macroNode.parseParameters(ctx.getText());
        return macroNode;
    }

    @Override
    public ASTNode visitMacroStatementEnd(TemplateParser.MacroStatementEndContext ctx) {
        return new JinjaNode("JinjaEndMacro", ctx.start.getLine()){};
    }
}


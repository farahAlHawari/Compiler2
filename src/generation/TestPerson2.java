package generation;

import AST.Core.ASTNode;
import AST.Core.PageNode;
import AST.Css.Rules.*;
import AST.Css.Selectors.*;
import AST.Css.Values.*;
import AST.Html.*;
import AST.Jinja.*;

public class TestPerson2 {

    public static void main(String[] args) {

        System.out.println("========================================");
        System.out.println("  TEST 1: TemplateProcessor - دمج extends/block");
        System.out.println("========================================\n");

        // نبني base.html AST يدوياً
        PageNode basePage = new PageNode(1);
        basePage.addChild(new DoctypeNode("html", 1));

        // <html>
        HtmlElementNode htmlElem = new HtmlElementNode("html", 2);
        // <head>
        HtmlElementNode headElem = new HtmlElementNode("head", 3);
        StyleBlockNode styleBlock = new StyleBlockNode(4);
        StyleRuleNode bodyRule = new StyleRuleNode("body", 5);
        bodyRule.addChild(new TypeSelectorNode("body", 5));
        DeclarationListNode declList = new DeclarationListNode(6);
        declList.addChild(new DeclarationNode("margin", "0", 6));
        declList.addChild(new DeclarationNode("font-family", "Arial", 7));
        bodyRule.addChild(declList);
        styleBlock.addChild(bodyRule);
        headElem.addChild(styleBlock);
        htmlElem.addChild(headElem);

        // <body>
        HtmlElementNode bodyElem = new HtmlElementNode("body", 10);
        // <header>
        HtmlElementNode headerElem = new HtmlElementNode("header", 11);
        headerElem.addChild(new TextNode("My Store", 11));
        bodyElem.addChild(headerElem);
        // <main> {% block content %}{% endblock %}
        HtmlElementNode mainElem = new HtmlElementNode("main", 13);
        mainElem.addChild(new JinjaBlockNode("content", 13));
        mainElem.addChild(new JinjaEndBlockNode(14));
        bodyElem.addChild(mainElem);
        htmlElem.addChild(bodyElem);
        basePage.addChild(htmlElem);

        // نبني index.html AST (child) - فيه extends + block
        PageNode indexPage = new PageNode(20);
        indexPage.addChild(new JinjaExtendsNode("base.html", 20));
        JinjaBlockNode contentBlock = new JinjaBlockNode("content", 21);
        HtmlElementNode divProducts = new HtmlElementNode("div", 22);
        divProducts.addChild(new HtmlAttributeNode("class", "products", 22));
        divProducts.addChild(new TextNode("Product list here", 22));
        contentBlock.addChild(divProducts);
        indexPage.addChild(contentBlock);
        indexPage.addChild(new JinjaEndBlockNode(23));

        // نبني GenerationContext
        GenerationContext ctx = new GenerationContext();
        ctx.addTemplateAST("base.html", basePage);
        ctx.addTemplateAST("index.html", indexPage);

        // نفّذ TemplateProcessor
        TemplateProcessor tp = new TemplateProcessor();
        PageNode merged = tp.process(indexPage, ctx);

        System.out.println("--- merged AST ---");
        merged.print("");

        // تحقق: ما في extends أو block
        boolean hasExtends = false;
        boolean hasBlock = false;
        for (ASTNode c : merged.children) {
            if (c instanceof JinjaExtendsNode) hasExtends = true;
            if (c instanceof JinjaBlockNode) hasBlock = true;
        }
        System.out.println("\nHas JinjaExtendsNode: " + hasExtends + " (should be false)");
        System.out.println("Has JinjaBlockNode: " + hasBlock + " (should be false)");

        System.out.println("\n========================================");
        System.out.println("  TEST 2: StaticRenderer - تحويل إلى HTML");
        System.out.println("========================================\n");

        StaticRenderer sr = new StaticRenderer(ctx);
        StringBuilder html = new StringBuilder();
        sr.renderStaticNode(merged, html);
        System.out.println("--- Generated HTML ---");
        System.out.println(html.toString());

        // تحقق: HTML يحتوي على أشياء مهمة
        String htmlStr = html.toString();
        System.out.println("--- Checks ---");
        System.out.println("Contains <!DOCTYPE>: " + htmlStr.contains("<!DOCTYPE html>"));
        System.out.println("Contains <html>: " + htmlStr.contains("<html>"));
        System.out.println("Contains <style>: " + htmlStr.contains("<style>"));
        System.out.println("Contains body { : " + htmlStr.contains("body"));
        System.out.println("Contains margin: 0: " + (htmlStr.contains("margin") && htmlStr.contains("0")));
        System.out.println("Contains <header>: " + htmlStr.contains("<header>"));
        System.out.println("Contains My Store: " + htmlStr.contains("My Store"));
        System.out.println("Contains <main>: " + htmlStr.contains("<main>"));
        System.out.println("Contains class=\"products\": " + htmlStr.contains("class=\"products\""));
        System.out.println("Contains Product list here: " + htmlStr.contains("Product list here"));

        System.out.println("\n========================================");
        System.out.println("  TEST 3: Validation - base غير موجود");
        System.out.println("========================================\n");

        PageNode orphanPage = new PageNode(30);
        orphanPage.addChild(new JinjaExtendsNode("missing.html", 30));
        orphanPage.addChild(new JinjaBlockNode("content", 31));
        orphanPage.addChild(new TextNode("orphan content", 31));
        orphanPage.addChild(new JinjaEndBlockNode(32));

        GenerationContext ctx2 = new GenerationContext();
        TemplateProcessor tp2 = new TemplateProcessor();
        PageNode result = tp2.process(orphanPage, ctx2);

        System.out.println("Warnings: " + ctx2.getWarnings());
        System.out.println("Has warning about missing.html: " +
                ctx2.getWarnings().stream().anyMatch(w -> w.contains("missing.html")));

        System.out.println("\n========================================");
        System.out.println("  TEST 4: صفحة بدون extends");
        System.out.println("========================================\n");

        PageNode standalone = new PageNode(40);
        standalone.addChild(new DoctypeNode("html", 40));
        HtmlElementNode div = new HtmlElementNode("div", 41);
        div.addChild(new TextNode("standalone", 41));
        standalone.addChild(div);

        TemplateProcessor tp3 = new TemplateProcessor();
        PageNode same = tp3.process(standalone, ctx);
        System.out.println("Same reference (no extends): " + (same == standalone));

        System.out.println("\n========================================");
        System.out.println("  ALL TESTS DONE");
        System.out.println("========================================");
    }
}
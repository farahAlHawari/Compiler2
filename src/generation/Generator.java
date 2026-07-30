package generation;

import AST.Core.ASTNode;
import AST.Core.PageNode;
import java.util.*;

/**
 * Person 4 — المنسّق النهائي لمرحلة Code Generation.
 * يربط ContextBuilder → TemplateProcessor → JinjaRenderer + StaticRenderer → OutputWriter.
 *
 * ⚠️ لا يعدّل على AST nodes.
 * ⚠️ لا يعالج Navigation / Routes كروابط HTML.
 * ⚠️ كل Error → Warning + skip (ما في انهيار).
 */
public class Generator {

    private final GenerationContext context;
    private TemplateProcessor templateProcessor;
    private StaticRenderer staticRenderer;
    private JinjaRenderer jinjaRenderer;
    private OutputWriter outputWriter;
    private ASTJsonSerializer serializer;

    public Generator(GenerationContext context) {
        this.context = context;
    }

    public void generate() {
        if (!context.isSemanticPassed()) {
            System.out.println("Cannot start Generation — Semantic errors exist");
            return;
        }

        context.addLog("[Generator] Started");

        // ① إنشاء processors
        templateProcessor = new TemplateProcessor();
        staticRenderer = new StaticRenderer(context);
        jinjaRenderer = new JinjaRenderer();
        serializer = new ASTJsonSerializer();
        outputWriter = new OutputWriter();

        // ② لكل Route → Template → Render
        int pageCount = 0;
        for (Map.Entry<String, String> routeEntry : context.getRoutes().entrySet()) {
            String path = routeEntry.getKey();
            String funcName = routeEntry.getValue();
            String tplName = context.getTemplateForRoute(funcName);

            if (tplName == null) {
                context.addWarning("No template mapped for route " + path
                        + " (function: " + funcName + ")");
                continue;
            }

            // ★ معالجة Parametric Routes ★
            if (path.contains("<int:")) {
                String paramName = extractParamName(path);
                List<Map<String, Object>> products = context.getProducts();

                if (products.isEmpty()) {
                    context.addWarning("Parametric route " + path
                            + " but products list is empty — skipping");
                    continue;
                }

                for (int i = 0; i < products.size(); i++) {
                    context.addLog("[Generator] Processing " + tplName
                            + " [" + funcName + " i=" + i + "]");

                    try {
                        context.pushScope(paramName, i, i);
                        context.pushScope("product", products.get(i), i);

                        PageNode ast = context.getTemplate(tplName);
                        if (ast == null) {
                            context.addWarning("AST not found for template: " + tplName);
                            continue;
                        }

                        context.setCurrentTemplate(tplName);
                        PageNode merged = templateProcessor.process(ast, context);
                        String html = jinjaRenderer.render(merged, context, staticRenderer);

                        String baseName = tplName.replace(".html", "");
                        String fileName = baseName + "_" + i + ".html";
                        context.addOutput(fileName, html);
                        context.markGenerated(tplName);
                        pageCount++;
                    } finally {
                        context.popScope();
                        context.popScope();
                    }
                }
            } else {
                // ★ Normal (non-parametric) route ★
                context.addLog("[Generator] Processing " + tplName
                        + " [" + funcName + " → " + path + "]");

                PageNode ast = context.getTemplate(tplName);
                if (ast == null) {
                    context.addWarning("AST not found for template: " + tplName);
                    continue;
                }

                context.setCurrentTemplate(tplName);
                PageNode merged = templateProcessor.process(ast, context);
                String html = jinjaRenderer.render(merged, context, staticRenderer);

                String fileName = funcName + ".html";
                context.addOutput(fileName, html);
                context.markGenerated(tplName);
                pageCount++;
            }
        }

        context.addLog("[Generator] Rendered " + pageCount + " page(s)");

        // ③ Serialization AST → JSON (Jinja فقط — Python تم بالـ Main)
        context.addLog("[Generator] Serializing template ASTs to JSON");
        String astJinjaJson = serializer.serializeJinjaASTs(context);

        // ④ كتابة كل المخرجات
        context.addLog("[Generator] Writing output files");
        outputWriter.writeAll(context, astJinjaJson);

        // ⑤ نسخ app.py
        outputWriter.copyAppPy(context);

        context.addLog("[Generator] Finished — " + pageCount + " page(s) generated");
    }

    private String extractParamName(String routePath) {
        int start = routePath.indexOf("<int:") + 5;
        int end = routePath.indexOf(">", start);
        if (start > 4 && end > start) {
            return routePath.substring(start, end);
        }
        return "i";
    }
}
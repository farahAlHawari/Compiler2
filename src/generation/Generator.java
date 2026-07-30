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

    /**
     * نقطة الدخول الرئيسية — ينفّذ كامل pipeline التوليد.
     * يُفترض أن يُستدعى بعد ContextBuilder.build() + freeze().
     */
    public void generate() {
        // ★ Semantic gate ★
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

            // تجاهل parent-only templates (مثل base.html لو ما حدا يستدعيها مباشرة)
            // base.html ما عندها extends لكنها parent — ما بن renderها لوحدها
            String parent = context.getTemplateParent(tplName);
            // إذا الـ template ما عنده parent وما عنده extends → ممكن يكون base
            // لكن إذا كان route يشير عليه، نrenderه عادي

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

                    // push scope: product + i
                    Map<String, Object> scope = new LinkedHashMap<>();
                    scope.put(paramName, i);
                    scope.put("product", products.get(i));
                    // Push كل متغير بالـ scope
                    context.pushScope(paramName, i, i);
                    context.pushScope("product", products.get(i), i);

                    PageNode ast = context.getTemplate(tplName);
                    if (ast == null) {
                        context.addWarning("AST not found for template: " + tplName);
                        context.popScope();
                        context.popScope();
                        continue;
                    }

                    context.setCurrentTemplate(tplName);
                    PageNode merged = templateProcessor.process(ast, context);
                    String html = jinjaRenderer.render(merged, context, staticRenderer);

                    // اسم الملف: product_details_0.html
                    String baseName = tplName.replace(".html", "");
                    String fileName = baseName + "_" + i + ".html";
                    context.addOutput(fileName, html);
                    context.markGenerated(tplName);
                    pageCount++;

                    context.popScope();
                    context.popScope();
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

                // اسم الملف من function name: index → index.html
                String fileName = funcName + ".html";
                context.addOutput(fileName, html);
                context.markGenerated(tplName);
                pageCount++;
            }
        }

        context.addLog("[Generator] Rendered " + pageCount + " page(s)");

        // ③ Serialization AST → JSON
        context.addLog("[Generator] Serializing ASTs to JSON");
        String astJinjaJson = serializer.serializeJinjaASTs(context);
        // Python AST — إذا متوفر من Compiler 1
        // String astPythonJson = serializer.serializePythonAST(pythonAST);

        // ④ كتابة كل المخرجات
        context.addLog("[Generator] Writing output files");
        outputWriter.writeAll(context, astJinjaJson);

        // ⑤ نسخ app.py
        outputWriter.copyAppPy(context);

        context.addLog("[Generator] Finished — " + pageCount + " page(s) generated");
    }

    /**
     * يستخرج اسم الـ parameter من route.
     * مثال: "/item/<int:i>" → "i"
     */
    private String extractParamName(String routePath) {
        int start = routePath.indexOf("<int:") + 5;
        int end = routePath.indexOf(">", start);
        if (start > 4 && end > start) {
            return routePath.substring(start, end);
        }
        return "i"; // fallback
    }
}
package generation;


import AST.Core.PageNode;
import java.util.*;


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
        templateProcessor = new TemplateProcessor();
        staticRenderer = new StaticRenderer(context);
        jinjaRenderer = new JinjaRenderer();
        serializer = new ASTJsonSerializer();
        outputWriter = new OutputWriter();
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
        context.addLog("[Generator] Serializing template ASTs to JSON");
        String astJinjaJson = serializer.serializeJinjaASTs(context);
        context.addLog("[Generator] Writing output files");
        outputWriter.writeAll(context, astJinjaJson);
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
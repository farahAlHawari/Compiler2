package generation;

import java.io.*;
import java.nio.file.*;
import java.util.Map;

/**
 * Person 4 — يكتب كل المخرجات النهائية لملفات.
 * يستقبل البيانات الجاهزة من GenerationContext.
 *
 * ⚠️ لا يُنفذ أي rendering — يكتب فقط.
 * ⚠️ كل File I/O Error → Warning + لا انهيار.
 * ⚠️ يدعم أي عدد من templates — Loop على كل outputHtml.
 */
public class OutputWriter {

    private String outputDir = "output";
    private String compilerOutputDir = "compiler_output";

    // =====================================================================
    // Public API
    // =====================================================================

    /**
     * يكتب كل المخرجات: HTML files + JSON + generation_log.
     * يُفترض أن يُستدعى بعد Generator.generate().
     *
     * @param context       السياق الجاهز بالـ rendered HTML
     * @param astJinjaJson  JSON string للـ Jinja ASTs
     */
    public void writeAll(GenerationContext context, String astJinjaJson) {
        // ① قراءة output directory من الـ context
        String ctxDir = context.getOutputDirectory();
        if (ctxDir != null && !ctxDir.isEmpty()) {
            this.outputDir = ctxDir;
        }

        // ② إنشاء المجلدات
        if (!createDirectories(context)) {
            context.addWarning("Failed to create output directories");
            return;
        }

        // ③ كتابة ملفات HTML
        writeHtmlFiles(context);

        // ④ كتابة JSON files
        writeJsonFiles(context, astJinjaJson);

        // ⑤ كتابة generation_log
        writeGenerationLog(context);
    }

    /**
     * ينسخ app.py من المسار المصدري إلى output/.
     */
    public void copyAppPy(GenerationContext context) {
        String sourcePath = context.getPythonFilePath();
        if (sourcePath == null || sourcePath.isEmpty()) {
            context.addWarning("Cannot copy app.py — source path is empty");
            return;
        }

        Path source = Paths.get(sourcePath);
        Path dest = Paths.get(outputDir, "app.py");

        if (!Files.exists(source)) {
            context.addWarning("Cannot copy app.py — source not found: " + sourcePath);
            return;
        }

        try {
            Files.createDirectories(Paths.get(outputDir));
            Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
            context.addLog("[OutputWriter] Copied app.py → " + dest);
            context.addCopiedFile(dest.toString());
        } catch (IOException e) {
            context.addWarning("Failed to copy app.py: " + e.getMessage());
        }
    }

    // =====================================================================
    // Directory Creation
    // =====================================================================

    private boolean createDirectories(GenerationContext context) {
        boolean ok = true;

        try {
            Files.createDirectories(Paths.get(outputDir));
        } catch (IOException e) {
            context.addWarning("Failed to create directory: " + outputDir);
            ok = false;
        }

        try {
            Files.createDirectories(Paths.get(compilerOutputDir));
        } catch (IOException e) {
            context.addWarning("Failed to create directory: " + compilerOutputDir);
            ok = false;
        }

        return ok;
    }

    // =====================================================================
    // HTML Files
    // =====================================================================

    /**
     * يكتب كل الصفحات المولّدة كملفات HTML.
     * Loop على كل outputHtml — لا أسماء ثابتة.
     * ⚠️ لا يكتب base.html (مدمجة داخل الصفحات).
     */
    private void writeHtmlFiles(GenerationContext context) {
        for (Map.Entry<String, String> entry : context.getOutputHtml().entrySet()) {
            String fileName = entry.getKey();

            // تأكد إن الاسم ينتهي بـ .html
            if (!fileName.endsWith(".html")) {
                fileName = fileName + ".html";
            }

            // لا نكتب base.html — مدمجة
            if (fileName.equals("base.html")) {
                context.addLog("[OutputWriter] Skipped base.html (merged into child templates)");
                continue;
            }

            String filePath = outputDir + File.separator + fileName;
            if (!writeToFile(filePath, entry.getValue())) {
                context.addWarning("Failed to write HTML: " + filePath);
            } else {
                context.addLog("[OutputWriter] Written: " + filePath);
            }
        }
    }

    // =====================================================================
    // JSON Files
    // =====================================================================

    private void writeJsonFiles(GenerationContext context, String astJinjaJson) {
        // ast_jinja.json
        if (astJinjaJson != null && !astJinjaJson.isEmpty()) {
            String path = compilerOutputDir + File.separator + "ast_jinja.json";
            if (!writeToFile(path, astJinjaJson)) {
                context.addWarning("Failed to write ast_jinja.json");
            } else {
                context.addLog("[OutputWriter] Written: " + path);
            }
        }

        // ast_python.json — من Compiler 1 عبر ContextBuilder
        String pythonJson = context.getPythonAstJson();
        if (pythonJson != null && !pythonJson.isEmpty()) {
            String path = compilerOutputDir + File.separator + "ast_python.json";
            if (!writeToFile(path, pythonJson)) {
                context.addWarning("Failed to write ast_python.json");
            } else {
                context.addLog("[OutputWriter] Written: " + path);
            }
        } else {
            context.addLog("[OutputWriter] Skipped ast_python.json (not provided by Compiler 1)");
        }
    }
    // =====================================================================
    // Generation Log
    // =====================================================================

    /**
     * يكتب generation_log.txt内容包括 كل log entries + warnings.
     */
    private void writeGenerationLog(GenerationContext context) {
        StringBuilder log = new StringBuilder();

        log.append("============================================================\n");
        log.append(" Generation Log — Compiler 2 (Code Generation)\n");
        log.append("============================================================\n\n");

        // Log entries
        log.append("--- Log Entries ---\n");
        for (String entry : context.getLogEntries()) {
            log.append(entry).append("\n");
        }

        // Warnings
        if (!context.getWarnings().isEmpty()) {
            log.append("\n--- Warnings ---\n");
            for (String w : context.getWarnings()) {
                log.append("Warning: ").append(w).append("\n");
            }
        }

        // Generated pages summary
        log.append("\n--- Generated Pages ---\n");
        for (String fileName : context.getOutputHtml().keySet()) {
            log.append("  ").append(fileName).append("\n");
        }

        // Copied files
        if (!context.getCopiedFiles().isEmpty()) {
            log.append("\n--- Copied Files ---\n");
            for (String f : context.getCopiedFiles()) {
                log.append("  ").append(f).append("\n");
            }
        }

        log.append("\n============================================================\n");

        String path = compilerOutputDir + File.separator + "generation_log.txt";
        if (!writeToFile(path, log.toString())) {
            System.err.println("CRITICAL: Failed to write generation_log.txt");
        } else {
            context.addLog("[OutputWriter] Written: " + path);
        }
    }

    // =====================================================================
    // Helpers
    // =====================================================================

    /**
     * يكتب محتوى نصي لملف. يرجع true عند النجاح.
     */
    private boolean writeToFile(String path, String content) {
        try {
            Files.createDirectories(Paths.get(path).getParent());
            try (FileWriter writer = new FileWriter(path)) {
                writer.write(content);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * يهرب نص بسيط لـ JSON (بدون quotes خارجية).
     */
    private String escapeForJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
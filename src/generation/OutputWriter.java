package generation;

import java.io.*;
import java.nio.file.*;
import java.util.Map;


public class OutputWriter {

    private String outputDir = "output";
    private String compilerOutputDir = "compiler_output";

    public void writeAll(GenerationContext context, String astJinjaJson) {

        String ctxDir = context.getOutputDirectory();
        if (ctxDir != null && !ctxDir.isEmpty()) {
            this.outputDir = ctxDir;
        }


        if (!createDirectories(context)) {
            context.addWarning("Failed to create output directories");
            return;
        }


        writeHtmlFiles(context);

        writeJsonFiles(context, astJinjaJson);


        writeGenerationLog(context);
    }

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


    private void writeHtmlFiles(GenerationContext context) {
        for (Map.Entry<String, String> entry : context.getOutputHtml().entrySet()) {
            String fileName = entry.getKey();


            if (!fileName.endsWith(".html")) {
                fileName = fileName + ".html";
            }


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



    private void writeJsonFiles(GenerationContext context, String astJinjaJson) {

        if (astJinjaJson != null && !astJinjaJson.isEmpty()) {
            String path = compilerOutputDir + File.separator + "ast_jinja.json";
            if (!writeToFile(path, astJinjaJson)) {
                context.addWarning("Failed to write ast_jinja.json");
            } else {
                context.addLog("[OutputWriter] Written: " + path);
            }
        }


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


    private String escapeForJson(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
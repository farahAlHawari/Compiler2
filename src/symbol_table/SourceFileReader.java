package symbol_table;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SourceFileReader {

    private static final Map<String, List<String>> cache = new HashMap<>();


    public static String getLine(String filePath, int lineNumber) {
        if (filePath == null || filePath.isEmpty() || lineNumber < 1) return "";

        try {
            List<String> lines = cache.get(filePath);
            if (lines == null) {
                Path path = Paths.get(filePath);
                if (!Files.exists(path)) return "";
                lines = Files.readAllLines(path);
                cache.put(filePath, lines);
            }
            if (lineNumber <= lines.size()) {
                return lines.get(lineNumber - 1).strip();
            }
        } catch (IOException e) {
            return "";
        }
        return "";
    }


    public static void clearCache() {
        cache.clear();
    }
}
package net.replaceitem.mcmodbump.context;

import net.replaceitem.mcmodbump.context.file.FileContext;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Context implements AutoCloseable {
    private final Path root;

    private final Map<Path, FileContext> openFiles = new HashMap<>();

    public Context(Path root) {
        this.root = root;
    }

    public FileContext openFile(String path) {
        var filePath = this.root.resolve(path);
        var existing = openFiles.get(filePath);
        if(existing != null) return existing;
        var context = new FileContext(filePath);
        this.openFiles.put(filePath, context);
        return context;
    }

    @Override
    public void close() {
        for (FileContext value : openFiles.values()) {
            value.close();
        }
    }
}

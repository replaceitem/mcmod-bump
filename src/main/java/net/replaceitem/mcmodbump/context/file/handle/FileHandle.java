package net.replaceitem.mcmodbump.context.file.handle;

import org.jline.jansi.Ansi;

import java.nio.file.Path;

public abstract class FileHandle implements AutoCloseable {
    protected final Path path;

    public FileHandle(Path path) {
        this.path = path;
    }

    @Override
    public void close() {
        save();
    }

    public abstract void save();

    public String getLogLabel() {
        return Ansi.ansi().fgBrightBlue().fgMagenta().a("[" + path.toString() + "]").reset().toString();
    }
}

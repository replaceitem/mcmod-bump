package net.replaceitem.mcmodbump.context.file;

import net.replaceitem.mcmodbump.context.file.handle.FileHandle;
import org.jline.jansi.Ansi;

public abstract class FileContext<T extends FileHandle> {
    protected final T handle;

    protected FileContext(T handle) {
        this.handle = handle;
    }

    protected void log(String text) {
        System.out.println(Ansi.ansi().a(handle.getLogLabel()).a(" ").a(text));
    }
}

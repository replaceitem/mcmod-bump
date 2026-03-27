package net.replaceitem.mcmodbump.context.file.handle;

import org.jline.jansi.Ansi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.UnaryOperator;

public class TextFileHandle extends FileHandle {

    private String content;

    public TextFileHandle(Path path) {
        super(path);

        try {
            content = Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(UnaryOperator<String> fn) {
        content = fn.apply(content);
    }

    @Override
    public void save() {
        if(content == null) throw new RuntimeException("File was never opened");
        try {
            Files.writeString(path, content);
            System.out.println(Ansi.ansi().a(getLogLabel()).a(" ").a("Saved"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getContent() {
        return content;
    }
}

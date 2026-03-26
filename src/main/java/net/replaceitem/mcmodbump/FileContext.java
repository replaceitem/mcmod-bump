package net.replaceitem.mcmodbump;

import org.intellij.lang.annotations.Language;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class FileContext implements AutoCloseable {

    private final Path path;
    private String content;

    public FileContext(Path path) {
        this.path = path;
        try {
            content = Files.readString(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public FileContext replace(@Language("RegExp") String regex, String replacement) {
        content = content.replaceFirst(regex, replacement);
        return this;
    }

    private static String splice(String str, int start, int end, @Nullable String replacement) {
        return str.substring(0, start) + (replacement != null ? replacement : "") + str.substring(end);
    }

    public FileContext replaceGroups(@Language("RegExp") String regex, String... replacements) {
        var matches = Pattern.compile(regex).matcher(content).results().toList();
        for (MatchResult match : matches) {
            var groupCount = match.groupCount();
            for (int i = groupCount - 1; i >= 0; i--) {
                var group = i+1;
                content = splice(content, match.start(group), match.end(group), replacements[i]);
            }
        }
        return this;
    }

    @Override
    public void close() {
        if(content == null) throw new RuntimeException("File was never opened");
        try {
            Files.writeString(path, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean find(@Language("RegExp") String regex) {
        return content.matches(regex);
    }
}

package net.replaceitem.mcmodbump.context;

import net.replaceitem.mcmodbump.context.file.JsonFileContext;
import net.replaceitem.mcmodbump.context.file.PropertiesFileContext;
import net.replaceitem.mcmodbump.context.file.TextFileContext;
import net.replaceitem.mcmodbump.context.file.handle.FileHandle;
import net.replaceitem.mcmodbump.context.file.handle.JsonFileHandle;
import net.replaceitem.mcmodbump.context.file.handle.TextFileHandle;
import org.intellij.lang.annotations.Language;
import org.jline.jansi.Ansi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Context implements AutoCloseable {
    private final Path root;

    private final Map<Path, FileHandle> openFiles = new HashMap<>();

    public Context(Path root) {
        this.root = root;
    }

    private Path resolvePath(String path) {
        return this.root.resolve(path);
    }

    protected TextFileHandle getTextFileHandle(Path path) {
        var existing = openFiles.get(path);
        if (existing != null) {
            if (!(existing instanceof TextFileHandle textFileHandle))
                throw new RuntimeException("File was previously opened as not a text file");
            return textFileHandle;
        }
        var handle = new TextFileHandle(path);
        this.openFiles.put(path, handle);
        return handle;
    }

    protected JsonFileHandle getJsonFileHandle(Path path) {
        var existing = openFiles.get(path);
        if (existing != null) {
            if (!(existing instanceof JsonFileHandle jsonFileHandle))
                throw new RuntimeException("File was previously opened as not a json file");
            return jsonFileHandle;
        }
        var handle = new JsonFileHandle(path);
        this.openFiles.put(path, handle);
        return handle;
    }

    public TextFileContext openTextFile(String path) {
        return openTextFile(resolvePath(path));
    }
    public TextFileContext openTextFile(Path path) {
        return new TextFileContext(getTextFileHandle(path));
    }

    public PropertiesFileContext openPropertiesFile(String path) {
        return openPropertiesFile(resolvePath(path));
    }
    public PropertiesFileContext openPropertiesFile(Path path) {
        return new PropertiesFileContext(getTextFileHandle(path));
    }

    public JsonFileContext openJsonFile(String path) {
        return openJsonFile(resolvePath(path));
    }
    public JsonFileContext openJsonFile(Path path) {
        return new JsonFileContext(getJsonFileHandle(path));
    }

    @Override
    public void close() {
        for (FileHandle value : openFiles.values()) {
            value.close();
        }
    }

    public void saveFiles() {
        for (FileHandle value : openFiles.values()) {
            value.save();
        }
    }

    public void runShell(String... args) {
        var builder = new ProcessBuilder(args);
        log(Ansi.ansi().a("Running command: ").fgBrightBlack().a(String.join(" ", builder.command())).toString());
        builder.redirectErrorStream(true);
        try {
            Process process = builder.start();
            try (var reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(Ansi.ansi().fgBrightBlack().a("  | ").reset().a(line));
                }
            }
            var exitCode = process.exitValue();
            if (exitCode != 0) throw new RuntimeException("Process exited with code " + exitCode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void runGradle(String... args) {
        var osName = System.getProperty("os.name").toLowerCase();
        String gradlewExecutable;
        var tempGradlePath = Path.of("gradlew.tmp.bat");
        try {
            if (osName.contains("win")) {
                // https://github.com/gradle/gradle/issues/7883
                var copiedGradlewBat = Files.copy(Path.of("gradlew.bat"), tempGradlePath);
                gradlewExecutable = copiedGradlewBat.toString();
            } else {
                gradlewExecutable = "./gradlew";
            }
            this.runShell(Stream.concat(Stream.of(gradlewExecutable), Arrays.stream(args)).toArray(String[]::new));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                Files.deleteIfExists(tempGradlePath);
            } catch (IOException e) {
                e.printStackTrace(System.out);
            }
        }
    }

    protected void log(String text) {
        System.out.println(Ansi.ansi().fgBrightBlue().fg(Ansi.Color.GREEN).a("[Context] ").reset().a(text));
    }

    public Stream<Path> findAllFiles(String rootPath, @Language("RegExp") String regex) {
        return findAllFiles(resolvePath(rootPath), regex);
    }
    public Stream<Path> findAllFiles(Path rootPath, @Language("RegExp") String regex) {
        Pattern pattern = Pattern.compile(regex);
        try {
            return Files.find(rootPath, 32, (file, attrs) -> attrs.isRegularFile() && pattern.matcher(file.getFileName().toString()).find());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

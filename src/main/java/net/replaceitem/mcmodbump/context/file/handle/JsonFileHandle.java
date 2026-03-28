package net.replaceitem.mcmodbump.context.file.handle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonFileHandle extends FileHandle {
    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
    private final JsonElement json;

    public JsonFileHandle(Path path) {
        super(path);
        try(var jsonReader = new JsonReader(new BufferedReader(new InputStreamReader(Files.newInputStream(path))))) {
            this.json = GSON.fromJson(jsonReader, JsonElement.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save() {
        try(var jsonWriter = new JsonWriter(new BufferedWriter(new OutputStreamWriter(Files.newOutputStream(path))))) {
            jsonWriter.setIndent(" ".repeat(4));
            GSON.toJson(this.json, jsonWriter);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JsonElement getRootElement() {
        return json;
    }
}

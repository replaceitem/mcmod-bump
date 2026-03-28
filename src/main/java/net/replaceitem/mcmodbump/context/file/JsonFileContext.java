package net.replaceitem.mcmodbump.context.file;

import com.google.gson.JsonElement;
import net.replaceitem.mcmodbump.context.file.handle.JsonFileHandle;

import java.util.Arrays;
import java.util.List;

public class JsonFileContext {
    private final JsonFileHandle handle;

    public JsonFileContext(JsonFileHandle handle) {
        this.handle = handle;
    }

    public void setValue(String path, String value) {
        var jsonPath = parsePath(path);
        var objectPath = jsonPath.subList(0, jsonPath.size() - 1);
        var leafPath = jsonPath.getLast();
        var elem = getElement(objectPath);
        elem.getAsJsonObject().addProperty(leafPath, value);
    }

    public void removeValue(String path) {
        var jsonPath = parsePath(path);
        var objectPath = jsonPath.subList(0, jsonPath.size() - 1);
        var leafPath = jsonPath.getLast();
        var elem = getElement(objectPath);
        elem.getAsJsonObject().remove(leafPath);
    }

    private List<String> parsePath(String path) {
        return Arrays.stream(path.split("\\.")).toList();
    }

    private JsonElement getElement(List<String> path) {
        var elem = handle.getRootElement();
        for (String s : path) {
            elem = elem.getAsJsonObject().get(s);
        }
        return elem;
    }
}

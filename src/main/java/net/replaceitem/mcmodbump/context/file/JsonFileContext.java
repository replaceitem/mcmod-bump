package net.replaceitem.mcmodbump.context.file;

import com.google.gson.JsonElement;
import net.replaceitem.mcmodbump.context.file.handle.JsonFileHandle;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class JsonFileContext extends FileContext<JsonFileHandle> {

    public JsonFileContext(JsonFileHandle handle) {
        super(handle);
    }

    public JsonPath path(String path) {
        return new JsonPath(parsePath(path));
    }

    public class JsonPath {
        private final List<String> path;

        public List<String> tailPath() {
            return path.subList(0, path.size() - 1);
        }

        public String leafPath() {
            return path.getLast();
        }

        public JsonPath(List<String> path) {
            this.path = path;
        }

        public void setValue(String value) {
            JsonElement leafParent = getLeafParent();
            if(leafParent != null && leafParent.isJsonObject()) {
                leafParent.getAsJsonObject().addProperty(leafPath(), value);
            }
        }

        public void setValueIfPresent(String value) {
            JsonElement leafParent = getLeafParent();
            if(leafParent != null && leafParent.isJsonObject() && leafParent.getAsJsonObject().has(leafPath())) {
                leafParent.getAsJsonObject().addProperty(leafPath(), value);
            }
        }

        public void removeValue() {
            JsonElement leafParent = getLeafParent();
            if(leafParent != null && leafParent.isJsonObject()) {
                leafParent.getAsJsonObject().remove(leafPath());
            }
        }

        public boolean exists() {
            JsonElement leafParent = getLeafParent();
            return leafParent != null && leafParent.isJsonObject() && leafParent.getAsJsonObject().has(leafPath());
        }

        @Nullable
        private JsonElement getLeafParent() {
            return getElement(tailPath());
        }

        @Nullable
        private JsonElement getElement(List<String> path) {
            var elem = handle.getRootElement();
            for (String s : path) {
                if(elem == null) break;
                if(!elem.isJsonObject()) return null;
                elem = elem.getAsJsonObject().get(s);
            }
            return elem;
        }
    }


    private List<String> parsePath(String path) {
        return Arrays.stream(path.split("\\.")).toList();
    }
}

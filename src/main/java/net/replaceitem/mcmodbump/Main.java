package net.replaceitem.mcmodbump;

import java.nio.file.Path;
import java.util.Map;

public class Main {
    public static final Map<String,Update> UPDATES = Map.of(
            "26.1", new Update_26_1()
    );


    static void main(String[] args) {
        var version = args[0];
        var update = UPDATES.get(version);
        if(update == null) throw new RuntimeException("Could not find version " + version);

        try(var context = new Context(Path.of("."))) {
            update.update(context);
        }
    }
}

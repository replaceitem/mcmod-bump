package net.replaceitem.mcmodbump;

import net.replaceitem.mcmodbump.context.Context;
import net.replaceitem.mcmodbump.update.Update;
import net.replaceitem.mcmodbump.updates.Update_26_1;

import java.nio.file.Path;
import java.util.Map;

public class Main {
    static void main(String[] args) {
        var version = args[0];
        var update = Updates.UPDATES.get(version);
        if(update == null) throw new RuntimeException("Could not find version " + version);

        try(var context = new Context(Path.of("."))) {
            update.update(context);
        }
    }
}

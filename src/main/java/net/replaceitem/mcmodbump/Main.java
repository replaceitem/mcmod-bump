package net.replaceitem.mcmodbump;

import net.replaceitem.mcmodbump.context.Context;

import java.nio.file.Path;

public class Main {
    static void main(String[] args) {
        var version = args[0];
        var migration = Migrations.MIGRATIONS.get(version);
        if(migration == null) throw new RuntimeException("Could not find version " + version);

        try(var context = new Context(Path.of("."))) {
            migration.update(context);
        }
    }
}

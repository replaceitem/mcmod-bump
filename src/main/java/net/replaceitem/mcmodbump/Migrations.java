package net.replaceitem.mcmodbump;

import net.replaceitem.mcmodbump.migration.Migration;
import net.replaceitem.mcmodbump.updates.Migration_26_1;

import java.util.Map;

public class Migrations {
    public static final Map<String, Migration> MIGRATIONS = Map.ofEntries(
            Map.entry("26.1", new Migration_26_1())
    );
}

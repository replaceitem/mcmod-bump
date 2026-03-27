package net.replaceitem.mcmodbump;

import net.replaceitem.mcmodbump.update.Update;
import net.replaceitem.mcmodbump.updates.Update_26_1;

import java.util.Map;

public class Updates {
    public static final Map<String, Update> UPDATES = Map.ofEntries(
            Map.entry("26.1", new Update_26_1())
    );
}

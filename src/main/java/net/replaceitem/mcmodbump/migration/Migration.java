package net.replaceitem.mcmodbump.migration;

import net.replaceitem.mcmodbump.context.Context;

public interface Migration {
    void update(Context context);
}

package net.replaceitem.mcmodbump.util;

import org.jspecify.annotations.Nullable;

public class StringUtil {
    public static String splice(String str, int start, int end, @Nullable String replacement) {
        return str.substring(0, start) + (replacement != null ? replacement : "") + str.substring(end);
    }
}

package net.replaceitem.mcmodbump.context.file;

import net.replaceitem.mcmodbump.context.file.handle.TextFileHandle;

import java.util.regex.Pattern;

public class PropertiesFileContext extends TextFileContext {


    public PropertiesFileContext(TextFileHandle handle) {
        super(handle);
    }

    public void setProperty(String property, String replacement) {
        this.replaceGroupsImpl("^\\s*%s *= *([^#]*?) *(?:#.*)?$".formatted(Pattern.quote(escapeKey(property))), escapeValue(replacement));
        log("Set property " + property + " to " + replacement);
    }

    public boolean hasProperty(String property) {
        return this.find("^\\s*%s *=".formatted(Pattern.quote(escapeKey(property))));
    }

    public void renameProperty(String property, String newName) {
        this.replaceGroupsImpl("^\\s*(%s) *=".formatted(Pattern.quote(escapeKey(property))), escapeKey(newName));
        log("Renamed property " + property + " to " + newName);
    }

    private static String escapeKey(String str) {
        return escape(str, true);
    }
    private static String escapeValue(String str) {
        return escape(str, false);
    }

    private static String escape(String str, boolean escapeSpace) {
        int length = str.length();
        StringBuilder outBuffer = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char c = str.charAt(i);
            outBuffer.append(switch (c) {
                case ' ' -> (i == 0 || escapeSpace) ? ("\\" + c) : ("" + c);
                case '\t' -> "\\t";
                case '\n' -> "\\n";
                case '\r' -> "\\r";
                case '\f' -> "\\f";
                case '=', ':', '#', '!', '\\' -> "\\" + c;
                default -> "" + c;
            });
        }
        return outBuffer.toString();
    }
}

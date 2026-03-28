package net.replaceitem.mcmodbump.context.file;

import net.replaceitem.mcmodbump.context.file.handle.TextFileHandle;
import net.replaceitem.mcmodbump.util.StringUtil;
import org.intellij.lang.annotations.Language;

import java.util.Arrays;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

public class TextFileContext extends FileContext<TextFileHandle> {

    public TextFileContext(TextFileHandle handle) {
        super(handle);
    }


    public void replace(@Language("RegExp") String regex, String replacement) {
        this.replaceImpl(regex, replacement);
        log("Replaced " + regex + " with " + replacement);
    }
    protected void replaceImpl(@Language("RegExp") String regex, String replacement) {
        handle.update(content -> Pattern.compile(regex, Pattern.MULTILINE).matcher(content).replaceFirst(replacement));
    }

    public void replaceGroups(@Language("RegExp") String regex, String... replacements) {
        this.replaceGroupsImpl(regex, replacements);
        log("Replaced groups in " + regex + " with " + Arrays.toString(replacements));
    }
    protected void replaceGroupsImpl(@Language("RegExp") String regex, String... replacements) {
        handle.update(content -> {
            var matches = Pattern.compile(regex, Pattern.MULTILINE).matcher(content).results().toList();
            for (int matchIndex = matches.size() - 1; matchIndex >= 0; matchIndex--) {
                MatchResult match = matches.get(matchIndex);
                var groupCount = match.groupCount();
                for (int groupIndex = groupCount - 1; groupIndex >= 0; groupIndex--) {
                    var group = groupIndex + 1;
                    content = StringUtil.splice(content, match.start(group), match.end(group), replacements[groupIndex]);
                }
            }
            return content;
        });
    }

    public boolean find(@Language("RegExp") String regex) {
        return Pattern.compile(regex, Pattern.MULTILINE).matcher(handle.getContent()).find();
    }
}

package net.replaceitem.mcmodbump.updates;

import net.replaceitem.mcmodbump.context.Context;
import net.replaceitem.mcmodbump.migration.Migration;

public class Migration_26_1 implements Migration {
    @Override
    public void update(Context context) {
        var gradleProperties = context.openPropertiesFile("gradle.properties");
        var buildGradle = context.openTextFile("build.gradle");

        // fabric api mod id update
        if(gradleProperties.hasProperty("fabric_version")) {
            gradleProperties.renameProperty("fabric_version", "fabric_api_version");
            buildGradle.replaceGroups("project\\.(fabric_version)", "fabric_api_version");
        }

        // version bumps
        gradleProperties.setProperty("minecraft_version", "26.1");
        gradleProperties.setProperty("loader_version", "0.18.4");
        gradleProperties.setProperty("loom_version", "1.15-SNAPSHOT");
        gradleProperties.setProperty("fabric_api_version", "0.144.0+26.1");

        buildGradle.replaceGroups("^\\s*id +['\"]((?:net\\.fabricmc\\.)?fabric-loom(?:-remap)?)['\"]", "net.fabricmc.fabric-loom");

        buildGradle.replaceGroups("(modImplementation) *\\(? *[\"']", "implementation");
        buildGradle.replaceGroups("(modCompileOnly) *\\(? *[\"']", "compileOnly");

        buildGradle.replace("^\\s*mappings +loom\\.officialMojangMappings\\( *\\) *\\R", "");
        buildGradle.replaceGroups("def +targetJavaVersion *= *(\\d+)", "25");
        buildGradle.replaceGroups("it.options.release *= *(\\d+)", "25");

        // accesswideners
        context.findAllFiles("src", "\\.accesswidener$").forEach(path -> {
            var accesswidener = context.openTextFile(path);
            accesswidener.replaceGroups("accessWidener\\s+v\\d\\s+(named)", "official");
        });

        // minotaur
        buildGradle.replaceGroups("uploadFile *= *(remapJar)", "jar");

        // fabric.mod.json
        context.findAllFiles("src", "^fabric\\.mod\\.json$").forEach(path -> {
            var fabricModJson = context.openJsonFile(path);
            fabricModJson.path("depends.minecraft").setValueIfPresent(">=26.1");
            fabricModJson.path("depends.fabricloader").setValueIfPresent(">=0.18.4");
            fabricModJson.path("depends.java").setValueIfPresent(">=25");

            if(fabricModJson.path("depends.fabric").exists() || fabricModJson.path("depends.fabric-api").exists()) {
                fabricModJson.path("depends.fabric").removeValue();
                fabricModJson.path("depends.fabric-api").setValue(">=0.144.0");
            }
        });

        // modid.mixins.json
        context.findAllFiles("src", "\\.mixins\\.json$").forEach(path -> {
            var mixinsJson = context.openJsonFile(path);
            mixinsJson.path("compatibilityLevel").setValue("JAVA_25");
        });

        // gradle update
        var gradleWrapperProperties = context.openPropertiesFile("gradle/wrapper/gradle-wrapper.properties");
        gradleWrapperProperties.setProperty("distributionUrl", "https://services.gradle.org/distributions/gradle-9.4.0-bin.zip");

        context.saveFiles();

        context.runGradle("wrapper");
    }
}

package net.replaceitem.mcmodbump.updates;

import net.replaceitem.mcmodbump.context.Context;
import net.replaceitem.mcmodbump.migration.Migration;

public class Migration_26_1 implements Migration {
    @Override
    public void update(Context context) {
        var gradleProperties = context.openPropertiesFile("gradle.properties");
        gradleProperties.setProperty("minecraft_version", "26.1");
        gradleProperties.setProperty("loader_version", "0.18.4");
        gradleProperties.setProperty("loom_version", "1.15-SNAPSHOT");
        gradleProperties.setProperty("fabric_api_version", "0.144.0+26.1");

        var gradleWrapperProperties = context.openPropertiesFile("gradle/wrapper/gradle-wrapper.properties");
        gradleWrapperProperties.setProperty("distributionUrl", "https://services.gradle.org/distributions/gradle-9.4.0-bin.zip");

        var buildGradle = context.openTextFile("build.gradle");
        buildGradle.replaceGroups("^ *id +['\"]((?:net\\.fabricmc\\.)?fabric-loom(?:-remap)?)['\"]", "net.fabricmc.fabric-loom");
        buildGradle.replaceGroups("(modImplementation) *\\(? *[\"']", "implementation");
        buildGradle.replace("^ *mappings +loom\\.officialMojangMappings\\( *\\) *\\R", "");
        buildGradle.replaceGroups("def +targetJavaVersion *= *(\\d+)", "25");

        // fabric api mod id update
        if(gradleProperties.hasProperty("fabric_version")) {
            gradleProperties.renameProperty("fabric_version", "fabric_api_version");
            buildGradle.replaceGroups("project\\.(fabric_version)", "fabric_api_version");
        }

        // accesswideners
        context.findAllFiles("src", "\\.accesswidener$").forEach(path -> context.openTextFile(path).replaceGroups("accessWidener\\s+v\\d\\s+(named)", "official"));

        // minotaur
        buildGradle.replaceGroups("uploadFile *= *(remapJar)", "jar");


        context.saveFiles();

        context.runGradle("wrapper");
    }
}

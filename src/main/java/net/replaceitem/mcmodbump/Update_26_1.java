package net.replaceitem.mcmodbump;

public class Update_26_1 implements Update {

    @Override
    public void update(Context context) {
        var gradleProperties = context.openFile("gradle.properties");
        gradleProperties.replaceGroups("minecraft_version *= *(.*)", "26.1")
                .replaceGroups("loader_version *= *(.*)", "0.18.4")
                .replaceGroups("loom_version *= *(.*)", "1.15-SNAPSHOT")
                .replaceGroups("fabric_api_version *= *(.*)", "0.144.0+26.1");

        var buildGradle = context.openFile("build.gradle");


        if(gradleProperties.find("fabric_version=")) {
            gradleProperties.replaceGroups("(fabric_version)=", "fabric_api_version");
            buildGradle.replaceGroups("project\\.(fabric_version)", "fabric_api_version");
        }
    }
}

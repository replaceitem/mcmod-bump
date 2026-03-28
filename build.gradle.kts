plugins {
    id("java")
    id("com.gradleup.shadow") version "9.4.1"
}

group = "net.replaceitem"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:26.1.0")
    compileOnly("org.jspecify:jspecify:1.0.0")
    implementation("org.jline:jansi:4.0.9")
    implementation("com.google.code.gson:gson:2.13.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(
            "Main-Class" to "net.replaceitem.mcmodbump.Main"
        )
    }
}
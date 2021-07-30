plugins {
    java
    application
}

application {
    mainClass.set("fr.rader.bob.Main")
}

group = "fr.rader"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // gson
    implementation("com.google.code.gson:gson:2.8.7")

    // lwjgl
    implementation("org.lwjgl.lwjgl:lwjgl:2.9.3")
    implementation("org.lwjgl.lwjgl:lwjgl_util:2.9.3")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "fr.rader.bob.Main"
    }
}
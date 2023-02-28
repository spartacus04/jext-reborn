plugins {
    java
    kotlin("jvm") version "1.8.10"
    id("com.github.johnrengelman.shadow") version "8.1.0"
    `maven-publish`
}

repositories {
    mavenLocal()
    mavenCentral()
    maven { url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") }
    maven { url = uri("https://repo.dmulloy2.net/nexus/repository/public/") }
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.3-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.bstats:bstats-bukkit:3.0.1")
}

group = "me.spartacus04.jext"
description = "jukebox-extended-reborn"
java.sourceCompatibility = JavaVersion.VERSION_1_8
version = "1.0.3"

tasks {
    shadowJar {
        archiveFileName.set("${rootProject.name}_${project.version}.jar")
        val dependencyPackage = "${rootProject.group}.dependencies.${rootProject.name.toLowerCase()}"

        relocate("kotlin", "${dependencyPackage}.kotlin")
        relocate("com/google/gson", "${dependencyPackage}.gson")
        relocate("org/intellij/lang", "${dependencyPackage}.lang")
        relocate("org/jetbrains/annotations", "${dependencyPackage}.annotations")
        relocate("org/bstats", "${dependencyPackage}.bstats")
        exclude("ScopeJVMKt.class")
        exclude("DebugProbesKt.bin")
        exclude("META-INF/**")
    }
}

artifacts {
    archives(tasks.shadowJar)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group.toString()
            artifactId = rootProject.name.toLowerCase()
            version = rootProject.version.toString()
            from(components["java"])
        }
    }
}
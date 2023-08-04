import org.jetbrains.dokka.base.DokkaBase
import org.jetbrains.dokka.base.DokkaBaseConfiguration

plugins {
    java
    kotlin("jvm") version "1.9.0"
    id("com.github.johnrengelman.shadow") version "8.1.1"

    id("org.jetbrains.dokka") version "1.8.20"

    `maven-publish`
    id("io.papermc.hangar-publish-plugin") version "0.0.5"
    id("com.modrinth.minotaur") version "2.8.3"
}

buildscript {
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:1.8.20")
    }
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.xenondevs.xyz/releases/")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.0.0")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.3")
    compileOnly("org.scala-lang:scala-library:2.13.11")
    compileOnly("com.github.techFortress:GriefPrevention:16.18.1")
    implementation("org.bstats:bstats-bukkit:3.0.2")
    implementation("io.github.bananapuncher714:nbteditor:7.18.6")
    implementation("xyz.xenondevs.invui:invui:1.13")
    implementation("xyz.xenondevs.invui:invui-kotlin:1.13")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("com.google.code.gson:gson:2.10.1")
}

group = "me.spartacus04.jext"

version = if (property("version_patch") == "0") {
    "${property("version_major")}.${property("version_minor")}"
} else {
    "${property("version_major")}.${property("version_minor")}.${property("version_patch")}"
}

description = "jukebox-extended-reborn"
java.targetCompatibility = JavaVersion.VERSION_1_8
java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks {
    shadowJar {
        archiveFileName.set("${rootProject.name}_${project.version}.jar")
        val dependencyPackage = "${rootProject.group}.dependencies.${rootProject.name.lowercase()}"
        from(subprojects.map { it.sourceSets.main.get().output })

        relocate("kotlin", "${dependencyPackage}.kotlin")
        relocate("com/google/gson", "${dependencyPackage}.gson")
        relocate("org/intellij/lang", "${dependencyPackage}.lang")
        relocate("org/jetbrains/annotations", "${dependencyPackage}.annotations")
        relocate("org/bstats", "${dependencyPackage}.bstats")
        relocate("io/github/bananapuncher714/nbteditor", "${dependencyPackage}.nbteditor")
        relocate("xyz/xenondevs/invui", "${dependencyPackage}.invui")
        relocate("xyz/xenondevs/inventoryaccess", "${dependencyPackage}.inventoryaccess")
        exclude("colors.bin")
        exclude("ScopeJVMKt.class")
        exclude("DebugProbesKt.bin")
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

artifacts.archives(tasks.shadowJar)

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.rootProject.version)
    }
}

// publish

tasks.dokkaHtml {
    val githubTag = System.getenv("githubTag")

    if(githubTag != null) {
        version = "$version - $githubTag"
    }

    pluginConfiguration<DokkaBase, DokkaBaseConfiguration> {
        customStyleSheets = listOf(file("docsAssets/logo-styles.css"))
        customAssets = listOf(file("icon.png"))
        footerMessage = "Jukebox Extended Reborn is licensed under the <a href=\"https://github.com/spartacus04/jext-reborn/blob/master/LICENSE.MD\">MIT</a> License."
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = rootProject.group.toString()
            artifactId = rootProject.name.lowercase()
            version = "${rootProject.version}"

            from(components["java"])
        }
    }
}

hangarPublish {
    val hangarApiKey = System.getenv("hangarApiKey")
    val hangarChangelog = System.getenv("hangarChangelog")

    publications.register("plugin") {
        version.set("${project.version}")
        namespace(
            "${property("hangar_username")}",
            "${property("hangar_slug")}"
        )
        channel.set("${property("hangar_channel")}")
        changelog.set(hangarChangelog)

        apiKey.set(hangarApiKey)

        platforms {
            register(io.papermc.hangarpublishplugin.model.Platforms.PAPER) {
                jar.set(tasks.shadowJar.flatMap { it.archiveFile })
                platformVersions.set("${property("minecraft_versions")}".split(","))

                this.dependencies {
                    hangar("dmulloy2", "ProtocolLib") {
                        required.set(true)
                    }
                }
            }
        }
    }
}


modrinth {
    val modrinthApiKey = System.getenv("modrinthApiKey")
    val modrinthChangelog = System.getenv("modrinthChangelog")

    token.set(modrinthApiKey)
    projectId.set("${property("modrinth_projectId")}")
    versionNumber.set(rootProject.version.toString())
    versionType.set("${property("modrinth_channel")}")
    uploadFile.set(tasks.shadowJar.flatMap { it.archiveFile })
    gameVersions.set("${property("minecraft_versions")}".split(","))
    loaders.set("${property("modrinth_loaders")}".split(","))

    changelog.set(modrinthChangelog)

    syncBodyFrom.set(rootProject.file("README.md").readText())
}

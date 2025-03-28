import proguard.gradle.ProGuardTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    java
    kotlin("jvm") version "2.1.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"

    id("org.jetbrains.dokka") version "2.0.0"

    `maven-publish`
    id("io.papermc.hangar-publish-plugin") version "0.1.3"
    id("com.modrinth.minotaur") version "2.8.7"
}

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.dokka:dokka-base:2.0.0")
        classpath("com.guardsquare:proguard-gradle:7.6.1") {
            exclude("com.android.tools.build")
        }
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
    maven("https://repo.opencollab.dev/main/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.3")
    compileOnly("org.scala-lang:scala-library:2.13.16")
    compileOnly("com.github.techFortress:GriefPrevention:17.0.0")
    compileOnly("com.github.koca2000:NoteBlockAPI:1.6.2")
    compileOnly("org.geysermc.geyser:api:2.6.1-SNAPSHOT") {
        attributes {
            attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 17)
        }
    }

    implementation("org.bstats:bstats-bukkit:3.1.0")
    implementation("io.github.bananapuncher714:nbteditor:7.19.7")
    implementation("xyz.xenondevs.invui:invui:1.44")
    implementation("xyz.xenondevs.invui:invui-kotlin:1.44")
    implementation("com.google.code.gson:gson:2.12.1")
    implementation("com.github.Anon8281:UniversalScheduler:0.1.6")

    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1")
}

group = "me.spartacus04.jext"

version = System.getenv("jextVersion") ?: "dev"

description = "jukebox-extended-reborn"
java.targetCompatibility = JavaVersion.VERSION_1_8
java.sourceCompatibility = JavaVersion.VERSION_1_8

tasks {
    shadowJar {
        archiveFileName.set("${rootProject.name}_${project.version}-shadowed.jar")
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
        relocate("com/github/Anon8281/universalScheduler", "${dependencyPackage}.universalScheduler")
        relocate("_COROUTINE", "${dependencyPackage}._COROUTINE")

        exclude("colors.bin")
        exclude("ScopeJVMKt.class")
        exclude("DebugProbesKt.bin")
        exclude("META-INF/**")

        minimize{
            exclude(dependency("xyz.xenondevs.invui:.*:.*"))
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_1_8)
    }
}

artifacts.archives(tasks.shadowJar)

tasks.register<ProGuardTask>("proguardJar") {
    outputs.upToDateWhen { false }
    dependsOn("clean")
    dependsOn("shadowJar")

    injars(tasks.shadowJar.flatMap { it.archiveFile })

    configuration("proguard-rules.pro")

    outjars("build/libs/${rootProject.name}_${project.version}.jar")
}

tasks.processResources {
    filesMatching("plugin.yml") {
        expand("version" to project.rootProject.version)
    }

    filesMatching("extension.yml") {
        expand("version" to project.rootProject.version)
    }
}

// publish

dokka {
    pluginsConfiguration.html {
        customStyleSheets.from(file("docsAssets/logo-styles.css"))
        customAssets.from(file("icon.png"))
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
        id.set("${property("hangar_slug")}")
        channel.set("${property("hangar_channel")}")
        changelog.set(hangarChangelog)

        apiKey.set(hangarApiKey)

        platforms {
            paper {
                url.set("${property("modrinth_url")}")
                platformVersions.set("${property("minecraft_versions")}".split(","))

                dependencies {
                    hangar("ProtocolLib") {
                        required.set(true)
                    }
                    this.url("NoteBlockAPI", "https://modrinth.com/plugin/noteblockapi") {
                        required.set(false)
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
    uploadFile.set(tasks.getByName("proguardJar").outputs.files.singleFile)
    gameVersions.set("${property("minecraft_versions")}".split(","))
    loaders.set("${property("modrinth_loaders")}".split(","))

    dependencies {
        optional.project("noteblockapi")
    }

    changelog.set(modrinthChangelog)

    syncBodyFrom.set(rootProject.file("README.md").readText())
}

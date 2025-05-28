plugins {
    id("java")
    id("fabric-loom") version "1.10-SNAPSHOT"
}

group = property("maven_group")!!
version = "${property("mod_version")}+mc${property("minecraft_version")}"

repositories {
    maven("https://maven.terraformersmc.com")
    maven("https://maven.shedaniel.me")
}

dependencies {
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

    modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
    modImplementation("com.terraformersmc:modmenu:${property("mod_menu_version")}")

    modApi("me.shedaniel.cloth:cloth-config-fabric:${property("cloth_config_version")}") {
        exclude("net.fabricmc.fabric-api")
    }

    include(implementation(annotationProcessor("io.github.llamalad7:mixinextras-fabric:0.3.5")!!)!!)
}

tasks {
    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(mutableMapOf("version" to project.version))
        }
    }

    jar {
        from("LICENSE")
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

loom {
    accessWidenerPath.set(file("src/main/resources/pegsemotes.accesswidener"))
}

plugins {
	id("java")
	id("fabric-loom") version "1.0-SNAPSHOT"
}

group = property("maven_group")!!
version = "${property("mod_version")}+mc${property("minecraft_version")}"

repositories {
	maven("https://maven.terraformersmc.com")
}

dependencies {
	// To change the versions see the gradle.properties file
	minecraft("com.mojang:minecraft:${property("minecraft_version")}")
	mappings("net.fabricmc:yarn:${property("yarn_mappings")}:v2")
	modImplementation("net.fabricmc:fabric-loader:${property("loader_version")}")

	modImplementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_version")}")
	modImplementation("com.terraformersmc:modmenu:${property("mod_menu_version")}")
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
	withSourcesJar()
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

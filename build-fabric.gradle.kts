plugins {
    id("fabric-loom").version("1.11-SNAPSHOT")
}

base {
    archivesName = "${project.property("archives_base_name")}"
}

stonecutter {
    val loader = "fabric"
    constants.match(
        loader,
        "fabric",
        "neoforge"
    )
}

fun getModVersion(): String {
    var version = project.property("mod_version") as String
    if (System.getenv("BUILD_RELEASE") != "true" && System.getenv("JITPACK") != "true") {
        val buildNumber = System.getenv("GITHUB_RUN_NUMBER")
        version += if (buildNumber != null) ("-build.$buildNumber") else "-snapshot"
    }
    return version
}

group = "${project.property("maven_group")}"
version = "v${getModVersion()}-fabric"

repositories {
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }
}

dependencies {
    minecraft("com.mojang:minecraft:${project.property("minecraft_version")}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${project.property("loader_version")}")

    // Dependencies
    modRuntimeOnly("net.fabricmc.fabric-api:fabric-api:${project.property("fabric_api_version")}")
    modImplementation("maven.modrinth:xaeros-minimap:${project.property("xaeros_minimap_version")}")
}

loom {
    runConfigs.configureEach {
        vmArgs("-Dmixin.debug.export=true")
    }
}

tasks.processResources {
    val modId = project.property("mod_id")
    val modName = project.property("mod_name")
    val modVersion = getModVersion()

    inputs.property("id", modId)
    inputs.property("name", modName)
    inputs.property("version", modVersion)

    filesMatching("fabric.mod.json") {
        val valueMap = mapOf(
            "id" to modId,
            "name" to modName,
            "version" to modVersion
        )
        expand(valueMap)
    }
    exclude("META-INF")
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(21)
}

// ensure that the encoding is set to UTF-8, no matter what the system default is
// this fixes some edge cases with special characters not displaying correctly
// see http://yodaconditions.net/blog/fix-for-java-file-encoding-problems-with-gradle.html
tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    inputs.property("archivesName", project.base.archivesName)

    from(rootDir.resolve("LICENSE")) {
        rename { "${it}_${inputs.properties["archivesName"]}" }
    }
}

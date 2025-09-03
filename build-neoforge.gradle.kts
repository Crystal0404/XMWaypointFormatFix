plugins {
    id("java-library")
    id("net.neoforged.moddev").version("2.0.107")
}

java.toolchain.languageVersion.set(JavaLanguageVersion.of(21))

base {
    archivesName = "${project.property("archives_base_name")}"
}

stonecutter {
    val loader = "neoforge"
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
version = "v${getModVersion()}-neoforge"

repositories {
    maven {
        name = "Modrinth"
        url = uri("https://api.modrinth.com/maven")
    }
}

dependencies {
    implementation("maven.modrinth:xaeros-minimap:${project.property("xaeros_minimap_version")}")
}

neoForge {
    version = "${project.property("neoforge_version")}"
    runs {
        create("client") {
            client()
            systemProperty("neoforge.enabledGameTestNamespaces", "${project.property("mod_id")}")
        }

        configureEach {
            jvmArgument("-Dmixin.debug.export=true")
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.ERROR
        }
    }

    mods {
        create("${project.property("mod_id")}") {
            sourceSet(sourceSets.main.get())
        }
    }
}

configurations {
    create("localRuntime")
    named("runtimeClasspath") {
        extendsFrom(configurations["localRuntime"])
    }
}

tasks.processResources {
    val modId = project.property("mod_id")
    val modName = project.property("mod_name")
    val modVersion = "${getModVersion()}+mc${project.property("minecraft_version")}"

    inputs.property("id", modId)
    inputs.property("name", modName)
    inputs.property("version", modVersion)

    filesMatching("META-INF/neoforge.mods.toml") {
        val valueMap = mapOf(
            "id" to modId,
            "name" to modName,
            "version" to modVersion
        )
        expand(valueMap)
    }
    exclude("fabric.mod.json")
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

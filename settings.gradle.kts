pluginManagement {
    repositories {
        maven { url = uri("https://maven.fabricmc.net/") }
        if (System.getenv("CI") != "true" /* not run in github actions */ ) {
            // If you're not from China, please remove this, it will slow down your downloads
            maven {
                name = "Ali"
                url = uri("https://maven.aliyun.com/repository/gradle-plugin")
                content {
                    excludeGroup("me.modmuss50")
                    excludeGroup("dev.kikugie")
                }
            }
        }
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://maven.kikugie.dev/snapshots") }
    }
}

plugins {
    id("dev.kikugie.stonecutter").version("0.7.10")
    id("org.gradle.toolchains.foojay-resolver-convention").version("0.9.0")
}

stonecutter {
    create(rootProject) {
        versions("1.21.1-fabric").buildscript("build-fabric.gradle.kts")
        version("1.21.1-neoforge").buildscript("build-neoforge.gradle.kts")
    }
}

rootProject.name = "XMWaypointFormatFix"

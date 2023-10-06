pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }

    plugins {
        kotlin("jvm").version(extra["kotlin_version"] as String)
        id("org.jetbrains.compose").version(extra["compose_version"] as String)
        id("io.ktor.plugin").version(extra["ktor_version"] as String)
    }
}

rootProject.name = "cs346-project"
include("application", "models", "server")
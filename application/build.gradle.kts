import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val ktor_version: String by project
val kotlin_version: String by project
val material3_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    //val voyagerVersion = "1.0.0-rc05"
    //implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
    //implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:$voyagerVersion")
    //implementation("cafe.adriel.voyager:voyager-tab-navigator:$voyagerVersion")
    implementation(compose.desktop.currentOs)
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("org.jetbrains.compose.material3:material3:$material3_version")
    implementation("com.arkivanov.decompose:decompose:1.0.0-beta-01")
    implementation("com.arkivanov.decompose:extensions-compose-jetbrains:1.0.0-beta-01")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "cs346-project"
            packageVersion = "1.0.0"
        }
    }
}

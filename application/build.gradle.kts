import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val ktor_version: String by project
val kotlin_version: String by project
val material3_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.9.0"
    id("org.jetbrains.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-cio:$ktor_version")
    implementation("io.ktor:ktor-client-json:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation(project(path =":models", configuration = "default"))
    implementation("io.ktor:ktor-serialization-jackson:2.3.5")
    testImplementation(kotlin("test"))
    implementation(group = "org.json", name = "json", version = "20231013")
    implementation("org.jetbrains.compose.material3:material3:$material3_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation ("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.0")
    implementation("com.squareup.okhttp3:okhttp:4.9.1")
    implementation("org.seleniumhq.selenium:selenium-api:4.0.0")
    implementation("org.seleniumhq.selenium:selenium-chrome-driver:4.0.0")
    implementation("org.burnoutcrew.composereorderable:reorderable:0.9.6")
//    implementation("ch.qos.logback:logback-classic:1.2.9")
    implementation("org.slf4j:slf4j-simple:2.0.7")
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("org.jetbrains.exposed", "exposed-core", "0.40.1")
    implementation("org.jetbrains.exposed", "exposed-dao", "0.40.1")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.40.1")
    implementation("org.jetbrains.exposed", "exposed-java-time", "0.40.1")
    implementation ("com.zaxxer:HikariCP:4.0.3")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    testImplementation(project(mapOf("path" to ":server")))
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "cs346-project"
            packageVersion = "1.0.0"
            includeAllModules = true
        }


    }
}

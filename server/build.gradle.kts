val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm")
    id("io.ktor.plugin")
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("com.example.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation(project(path =":models", configuration = "default"))
    implementation("io.ktor:ktor-server-default-headers-jvm:$kotlin_version")
    implementation("io.ktor:ktor-server-status-pages:$kotlin_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$kotlin_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$kotlin_version")
    implementation("io.ktor:ktor-server-host-common-jvm:2.3.5")
    implementation("io.ktor:ktor-server-status-pages-jvm:2.3.5")
    testImplementation("io.ktor:ktor-server-tests-jvm")
    implementation ("com.zaxxer:HikariCP:4.0.3")
    implementation("org.jetbrains.exposed", "exposed-core", "0.40.1")
    implementation("org.jetbrains.exposed", "exposed-dao", "0.40.1")
    implementation("org.jetbrains.exposed", "exposed-jdbc", "0.40.1")
    implementation("org.jetbrains.exposed", "exposed-java-time", "0.40.1")
    implementation("mysql:mysql-connector-java:8.0.23")
    implementation("org.jsoup:jsoup:1.15.3")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

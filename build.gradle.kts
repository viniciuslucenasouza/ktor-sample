
val kotlin_version: String by project
val logback_version: String by project
val flaxoos_extra_plugins_version: String by project
val esper_version = "8.8.0"

plugins {
    kotlin("jvm") version "2.0.20"
    id("io.ktor.plugin") version "2.3.12"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
    maven { url = uri("https://packages.confluent.io/maven/") }
}

dependencies {
    implementation("io.github.flaxoos:ktor-server-kafka-jvm:$flaxoos_extra_plugins_version")
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-config-yaml")

    testImplementation("io.ktor:ktor-server-test-host:$kotlin_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    implementation("com.espertech:esper-common:$esper_version")
    implementation("com.espertech:esper-compiler:$esper_version")
    implementation("com.espertech:esper-runtime:$esper_version")
}



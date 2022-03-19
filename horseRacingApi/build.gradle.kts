val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.6.10"
}

group = "com.racingapi.horse"
version = "0.0.1"
application {
    mainClass.set("com.racingapi.horse.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")
    implementation("io.ktor:ktor-jackson:$ktor_version")
    implementation("org.valiktor:valiktor-core:0.12.0")
    implementation("org.jsoup:jsoup:1.14.3")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}